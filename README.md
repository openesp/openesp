# Open Enterprise Search Platform (OpenESP)
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
Download a (beta) version of OpenESP 0.3 from [this location](https://www.dropbox.com/sh/ylblwnhk1r7gjs0/vMcnjf2lln).

* [openesp-0.3.zip](https://www.dropbox.com/sh/ylblwnhk1r7gjs0/033BCUpg5I/openesp-0.3.zip) - Pure binary zip distribution (no installer, simply unpack and run)
* [OpenESP-Setup-0.3.exe](https://www.dropbox.com/sh/ylblwnhk1r7gjs0/U1GRSyO1fB/OpenESP-Setup-0.3.exe) - Native Windows installer
* [openesp-install-0.3.jar](https://www.dropbox.com/sh/ylblwnhk1r7gjs0/Cl8UxzhsWR/openesp-install-0.3.jar) - Cross-platform installer (for Linux, Mac, Windows)

To start the cross-platform installer you need Java preinstalled. Double-click or run ```java -jar openesp-install-0.3.jar
``` to run.

## Building
If you prefer to build the product from source or hack yourself, please first download the source distribution

* Click the "ZIP" icon to download a source distribution ZIP file snapshot
* Or clone the Git repo using command ```git clone git@github.com:openesp/openesp.git```

Now you have the source. The project uses [Gradle](http://www.gradle.org/) build system. If you don't have Gradle installed already, simply run the following wrapper command:

Linux/OSX:

    ./gradlew
    
Windows:

    gradlew.bat

Alternatively, if you have Gradle already, simply run 

    gradle

You'll then get the distro in build/distributions/openesp-x.y.zip

**NOTE:** The currente source will not build the installer, only the zip distro. So if you require an installer, please use one of the downloads above.

## Running
From build directory:

    ./build/openesp/bin/run.sh
    
Windows:

    build\openesp\bin\run.bat

From distribution zip:

Linux/OSX:

    jar xf openesp-x.y.zip
    ./openesp/bin/run.sh

Windows:

    jar xf openesp-x.y.zip
    openesp\bin\run.bat

Now you can visit OpenESP's admin screen at http://localhost:18080/

If you downloaded the installer you will also be able to install as a service on Windows or Linux and then run in the background.

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

### Development (source) layout
Here's the layout of the file layout of the source tree
```
openesp
  ├── build.gradle         : build script
  ├── gradle.properties    : global properties, like component versions
  ├── initial              : this is the initial folder structure which will be included in distro
  ├── overlay              : everything in this folder will be overlaid AFTER assembling Solr, Tomcat etc
  ├── documentation        : for developing user documentation. PDF should be placed in initial/doc
  ├── openesp-solr         : Sub project for building (or downloading) Apache Solr
  ├── openesp-mcf          : Sub project for building (or downloading) Apache MCF
  ├── build.gradle         : The makefile for gradle
  ├── gradle.properties    : Specify global properties here, such as versions for Solr, Tomcat etc
  └── build                : After running gradle, the release artifacts are generated here
```

## Roadmap
Please check the [issue tracker](https://github.com/openesp/openesp/issues) for bugs and future plans.
The next major features planned include

* Installer
* SSL security

## Engage with the community
If you use the product, please engage with the community

### Discussions
Join the [community at Google+](https://plus.google.com/communities/103998803341318319412) to ask questions and join discussions. For pure Tomcat, Solr or Manifold questions you may also engage directly with their respective communities.

### File an issue
If you found a bug or have a feature request, please [file an issue](https://github.com/openesp/openesp/issues)

### Contribute back your changes
Contributing through GitHub is easy. Whenever you have done some changes you'd like to see in the next release, issue a [pull request](https://help.github.com/articles/using-pull-requests). Note that you do not need to file a separate issue when using pull requests.

Welcome as a user and as a contributor!
