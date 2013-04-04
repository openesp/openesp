import java.io.*;

public class PortReplace {

String filen;
String defPort;
String defSslPort;
String port;
String sslport;

PortReplace(String f, String dp, String ds, String p, String sp) {
  filen=f;
  defPort=dp;
  defSslPort=ds;
  port=p;
  sslport=sp;
}

void proceed() {
  try {
             File file = new File(filen);
             BufferedReader reader = new BufferedReader(new FileReader(filen));
             String line = "", oldtext = "";
             while((line = reader.readLine()) != null)
                 {
                 oldtext += line + "\r\n";
             }
             reader.close();
             // replace a word in a file
             //String newtext = oldtext.replaceAll("drink", "Love");
            
             //To replace a line in a file
             String newtext = oldtext.replaceAll(defPort, port);
             String newtext1= newtext.replaceAll(defSslPort, sslport);
            
             FileWriter writer = new FileWriter(filen);
             writer.write(newtext1);writer.close();

  }
  catch(Exception e) {
   System.out.println(e.toString());
  }
}

public static void main(String[] args){
 
        String file = args[0];
        String defPort="18080";
        String defSslPort="18443";
        String port=args[1];
        String sslport=args[2];
        
        PortReplace portReplace = new PortReplace(file, defPort, defSslPort, port, sslport);
        portReplace.proceed();
}

}