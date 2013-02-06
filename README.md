Open Enterprise Search Platform (OpenESP)
=========================================
Welcome to OpenESP, the Open Source Enterprise Search Platform.
The project develops an Apache 2.0 licensed distro of Apache Solr.
With "distro", we mean Solr as the core plus other components which are
frequently needed in an enterprise search setting.

We believe the next logical step in the Lucene/Solr "revolution" is to
bring to the masses a free, open distro which enables enterprises around
the globe to be up and running with searching their databases, file systems,
ECMs and other repositories within minutes after downloading OpenESP.

The project is initiated and sponsored by Cominvent AS (www.cominvent.com)
and lives on www.openesp.org and here on GitHub.

We adopt the Apache Software Foundation project develpment methodology,
doing all development in the open.

Welcome as a user and as a contributor!

Building
========
The project uses Gradle and Ant as build systems. Run it as normal, or
if you don't have Gradle installed already, simply run the service wrapper:

Linux/OSX:

    ./gradlew
    
Windows:

    gradlew.bat

Alternatively, if you have Gradle already, simply run 

    gradle

You'll then get the distro in build/distributions/openesp-x.y.zip

Structure
=========

* initial: this is the initial folder structure which will be included in distro
* overlay: everything in this folder will be overlaid AFTER assembling Solr, Tomcat etc
* documentation: for developing user documentation. PDF should be placed in initial/doc
* openesp-solr: Sub project for building (or downloading) Apache Solr
* openesp-mcf: Sub project for building (or downloading) Apache MCF (not yet active)
* build.gradle: The makefile for gradle
* gradle.properties: Specify global properties here, such as versions for Solr, Tomcat etc
* build: After running gradle, the release artifacts are generated here

Roadmap
=======
Please check the [issue tracker](https://github.com/openesp/openesp/issues) for bugs and future plans