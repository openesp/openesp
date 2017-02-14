# Open Enterprise Search Platform (OpenESP)

## Retirement notice
>**NOTE: This project is no longer actively maintained.**  
The OpenESP project was originally started in 2010 to make it easier to deploy 
Solr in a well configured Tomcat container, with start scripts, installer, 
security and documentation.

>Solr has since version 5.x been shipped as a standalone app with excellent
start scripts (`bin/solr`), a central config file (`solr.in.sh`), SSL support,
authentication support, Linux installer script as well as a well tuned 
production ready Jetty engine. Other containers such as Tomcat is not supported. 
Because of all this, we no longer see the need for OpenESP, and will contribute
our efforts directly into improving Solr.  

>As there will be no further security patches or bug fixes for Solr 4.x, we 
recommend existing OpenESP users to upgrade to a recent version of Solr from
http://lucene.apache.org/solr/

  
   
   
----
Original README content:
## Welcome
Welcome to OpenESP, the Open Source Enterprise Search Platform.
The project develops an [Apache 2.0 licensed](http://www.apache.org/licenses/LICENSE-2.0.html) distro of [Apache Solr](http://lucene.apache.org/solr/).
With "distro", we mean Solr as the core plus other components which are
frequently needed in an enterprise search setting.

We believe the next logical step in the Lucene/Solr "revolution" is to
bring to the masses a free, open distro which enables enterprises around
the globe to be up and running with searching their databases, file systems,
ECMs and other repositories within minutes after downloading OpenESP. For that
reason we include [Tomcat](http://tomcat.apache.org/) and [ManifoldCF](http://manifoldcf.apache.org/) out of the box.

The project is maintained by committers from the [Open Enterprise Search Network](http://openesn.net/).
Product homepage is at [www.openesp.org](http://openesn.net/openesp.html) and the code is here at GitHub.
We very much welcome bug reports and [pull requests](https://help.github.com/articles/using-pull-requests) from anyone using the product.

## Downloads
Head over to the release section to download the [Latest binary Release](https://github.com/openesp/openesp/releases/latest)

[![Download button](https://raw.githubusercontent.com/openesp/openesp/master/documentation/download-button.png)](https://github.com/openesp/openesp/releases/latest)

## Developers
If you want to hack the sources, then clone the repo and build from source.

Please head to the [Development](https://github.com/openesp/openesp/wiki/Development) section for instructions
on how to build OpenESP from source.

[![Build Status](https://travis-ci.org/openesp/openesp.svg?branch=master)](https://travis-ci.org/openesp/openesp)

## Installing
Using the GUI installers, you select all options in the installer windows, and you will end up with
OpenESP installed as a service which can be started/stopped as any other service.

## Running
If installed as a service, start/stop OpenESP as any other service on your system.

If unpacked from ZIP or built from source:

Linux/OSX:

    ./openesp/bin/run.sh

Windows:

    openesp\bin\run.cmd

## The openespctl script
After install, you may want to do certain tasks, and there is a script called openespctl available for certain common tasks. Type ```./bin/openespctl help``` for a usage explanation.

### Enable and disable apps
if you want to disable ManifolfCF (MCF) because you won't need it, simply run

    ./bin/openespctl disable mcf
    
and then restart tomcat

### Change port number
To change the port number from default 18080 after install, do

    ./bin/openespctl port 8983

## File structure
### Deployment layout
Once deployed, the structure of OpenESP folders is as follows:
```
openesp
  ├── bin        : Start scripts like openespctl
  ├── conf       : Configuration for Solr and MCF
  ├── doc        : Admin guide
  ├── lib        : Jar file plugins for Solr and MCF
  ├── logs       : Log output folders
  ├── tomcat     : Tomcat application server
  └── webapps    : Location of web application (war) files
```

## Roadmap
Please check the [issue tracker](https://github.com/openesp/openesp/issues) for bugs and future plans.
The next major features planned include

* SSL security
* Search frontend

## Engage with the community
If you use the product, please engage with the community

### Discussions
Join the [community at Google+](https://plus.google.com/communities/103998803341318319412) to ask questions and join discussions. For pure Tomcat, Solr or Manifold questions you may also engage directly with their respective communities.

### File an issue
If you found a bug or have a feature request, please [file an issue](https://github.com/openesp/openesp/issues)

### Contribute back your changes
Contributing through GitHub is easy. Whenever you have done some changes you'd like to see in the next release, issue a [pull request](https://help.github.com/articles/using-pull-requests). Note that you do not need to file a separate issue when using pull requests.

Welcome as a user and as a contributor!
