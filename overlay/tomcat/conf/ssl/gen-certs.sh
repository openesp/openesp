#!/bin/bash
# Commands necessary to generate server and client SSL certificates for Tomcat
# Pulled from http://www.maximporges.com/2009/11/18/configuring-tomcat-ssl-clientserver-authentication/
rm *.jks *.pem *.cer serial* index.txt* >/dev/null 2>&1
echo "Generating keystores server.jks and client.jks"
keytool -genkeypair -alias serverkey -keyalg RSA -dname "CN=OpenESP Server,OU=Application Development,O=Cominvent,L=,S=Oslo,C=NO" -keypass password -keystore server.jks -storepass password
keytool -genkeypair -alias clientkey -keyalg RSA -dname "CN=OpenESP Client,OU=Application Development,O=Cominvent,L=,S=Oslo,C=NO" -keypass password -storepass password -keystore client.jks

echo "Export public version of client cert, import to server keystore"
keytool -exportcert -alias clientkey -file client-public.cer -keystore client.jks -storepass password
keytool -importcert -keystore server.jks -alias clientcert -file client-public.cer -storepass password -noprompt
keytool -list -keystore server.jks -storepass password

echo "Export public version of server cert, import to client keystore"
keytool -exportcert -alias serverkey -file server-public.cer -keystore server.jks -storepass password
keytool -importcert -keystore client.jks -alias servercert -file server-public.cer -storepass password -noprompt
keytool -list -keystore client.jks -storepass password

#
# Create a self-signed client certificate for use from a browser. Name it browser-cert.cer
#
# First we create a CA
echo "Create a CA (ca.pem)"
touch index.txt
echo '01' > serial
openssl req -new -nodes -out ca.pem -x509 -extensions v3_ca -keyout ca.key.pem -days 3650 -config caconfig.cnf

# Next create a client cert for browser usage and sign it with our new CA
echo "Create browser certificate signed by CA (browser-client.pem)"
openssl req -new -nodes -out browser-client.req.pem -keyout browser-client.key.pem -days 3650 -config caconfig.cnf
openssl ca -batch -out browser-client.pem -days 3650 -config caconfig.cnf -infiles browser-client.req.pem
# Also create a P12 equivalent for importing in other browsers
openssl pkcs12 -export -out browser-client.pfx -inkey browser-client.key.pem -in browser-client.pem -certfile ca.pem -password pass:password

# Import the CA and browser cert to our keychain
echo "Import CA cert on server keychain"
keytool -importcert -keystore server.jks -alias ca-cert -file ca.pem -storepass password -noprompt
echo "Import browser cert on server keychain"
keytool -importcert -keystore server.jks -alias openesp-browser-cert -file browser-client.pem -storepass password -noprompt

echo "Cleaning up the mess"
rm browser-client.req.pem serial* index.txt* 01.pem >/dev/null 2>&1