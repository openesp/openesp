// Set defaults
def sep = System.getProperties().get("file.separator")
def pathSep = System.getProperties().get("path.separator")

def serviceName="OpenESP"
def serviceDescription="Open Enterprise Search Platform (Tomcat/Solr)"
def jvmMem = "1024m"
def env = System.getenv()
def javaHome = env['JAVA_HOME']
def javaOptsOrig = env['JAVA_OPTS']
def scriptDir = new File(getClass().protectionDomain.codeSource.location.path).parent.replaceAll("%20", " ")
def installPath = new File(scriptDir).getAbsoluteFile().getParentFile().getCanonicalPath().replaceAll("%20", " ")
def tomcatPath = [installPath, 'tomcat'].join(sep)
def solrHome = [installPath, 'conf', 'solr'].join(sep)
def solrDataDir = [installPath, 'data', 'solr'].join(sep)
def solrLogDir = [installPath, 'logs', 'solr'].join(sep)
def mcfConf = [installPath, 'conf', 'mcf', 'properties.xml'].join(sep)
def startService = true
def solrCloud = false
def dzkrun = false
def dzkhost = ""
def executable = [scriptDir, 'OpenESP.exe'].join(sep).replaceAll("%20", " ")

// Parse command line
def cli = new CliBuilder(usage: 'install-windows-service [-h] [options]', header: 'Windows Service Installer')

cli.a longOpt: 'auto', 'auto install the service, guessing the otions as listed below'
cli.s longOpt: 'serviceName', args: 1, "Windows service name, (default $serviceName)"
cli.jm longOpt: 'jvmMem', args: 1, "JVM memory (default $jvmMem)"
cli.jh longOpt: 'javaHome', args: 1, "manually set JAVA_HOME (default $javaHome)"
cli.p longOpt: 'installPath', args: 1, "OpenESP install path (default $installPath)"
cli.sh longOpt: 'solrHome', args: 1, "Solr home directory (default $solrHome)"
cli.sd longOpt: 'solrDataDir', args: 1, "Solr data directory (default $solrDataDir)"
cli.sl longOpt: 'solrLogDir', args: 1, "Solr log directory (default $solrLogDir)"
cli.ss longOpt: 'startService', args: 1, "Start service after install: true|false (default $startService)"
cli.sc longOpt: 'solrCloud', args: 1, "Solr Cloud Enabled: true|false (default $solrCloud)"
cli.zr longOpt: 'dzkrun', args: 1, "Start local zookeeper: true|false (default $dzkrun)"
cli.zh longOpt: 'dzkhost', args: 1, 'comma separated list of zookeeper hosts, ex: host1:1234,host2:1234'
cli.h longOpt: 'help', 'print usage information'

def opt = cli.parse(args)
def errMsg2 = "Invalid arguments.\nUsage: ${cli.usage}\n"

if (opt && opt.h) {
  cli.usage()
  System.exit(0)
} else if (!opt) {
  println "\nError parsing arguments. Use -h for help."
  System.exit(2)
}


// Set various options
if(opt.s)
	serviceName = opt.s
if(opt.jvmMem)
	jvmMem=opt.jvmMem
if(opt.javaHome)
	javaHome=opt.javaHome
if(opt.installPath)
	installPath=opt.installPath
if(opt.solrHome)
	solrHome=opt.solrHome
if(opt.solrDataDir)
	solrDataDir=opt.solrDataDir
if(opt.solrLogDir)
	solrLogDir=opt.solrLogDir
if(opt.startService)
	startService=opt.startService
if(opt.solrCloud)
	solrCloud=opt.solrCloud
if(opt.dzkrun)
	dzkrun=opt.dzkrun
if(opt.dzkhost)
	dzkhost=opt.dzkhost


//
//  Compile JAVA_OPTS from various sources
//
def javaOpts=[]
def fileOpts=[]
def zkOpts=[]

def file = new File([scriptDir, 'service_java_opts.properties'].join(sep))
file.eachLine { line ->
	fileOpts.add(line)
}

solrOpts =   ["-Dsolr.solr.home=${solrHome}",
              "-Dsolr.data.dir=${solrDataDir}",
              "-Dsolr.log.dir=${solrLogDir}",
              "-Dlog4j.configuration=file:///$solrHome/log4j.properties"
              ]

mcfOpts =    ["-Dorg.apache.manifoldcf.configfile=${mcfConf}"]

commonOpts = ["-Dcatalina.base=${tomcatPath}",
              "-Dcatalina.home=${tomcatPath}",
              "-Djava.endorsed.dirs="+[tomcatPath,'endorsed'].join(sep),
              "-Djava.io.tmpdir="+[tomcatPath,'temp'].join(sep),
              "-Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager",
              "-Djava.util.logging.config.file="+[tomcatPath,'conf','logging.properties'].join(sep),
              "-Dopenesp.home=${installPath}",
              "-Dlog4j.debug=true"
              ]

if (solrCloud == "true") {
  if (dzkrun == "true") zkOpts.add('-DzkRun')
  if (dzkhost) zkOpts.add("-DzkHost=${dzkhost}")
}

javaOpts = commonOpts + solrOpts + mcfOpts + fileOpts + zkOpts
javaOptsStr = javaOpts.join(";")
if (javaOptsOrig) {
  javaOptsStr += ";" + javaOptsOrig
}


//
// Compile classpath
//
javaClassPath = [[tomcatPath,'bin','bootstrap.jar'].join(sep),
                 [tomcatPath,'bin','tomcat-juli.jar'].join(sep)].join(pathSep)

cmdargs = ["--DisplayName=${serviceName}",
        "--Description=${serviceDescription}",
        "--Install="+[installPath,'bin',serviceName+'.exe'].join(sep),
        "--LogPath="+[tomcatPath,'logs'].join(sep),
        "--Classpath=${javaClassPath}",
        '--StartMode=jvm',
        '--StopMode=jvm',
        '--Jvm=auto',
        '--Startup=auto',
        '--StartClass=org.apache.catalina.startup.Bootstrap',
        '--StopClass=org.apache.catalina.startup.Bootstrap',
        '--StartParams=start',
        '--StopParams=stop',
        "--JvmMs=${jvmMem}",
        "--JvmMx=${jvmMem}",
        "--JvmOptions=${javaOptsStr}"
        ]


if(opt.auto) {
	if(!javaHome) {
    println "No Java Home found. Plese either pass --javahome or set environment variable JAVA_HOME"
    System.exit(2)
	}
} else {
	if(!opt.jvmMem || !opt.javaHome || !opt.installPath || !opt.solrHome || 
	   !opt.solrDataDir || !opt.solrLogDir || !opt.startService || 
	   !opt.solrCloud || !opt.dzkrun || !opt.dzkhost) {
		println errMsg2
    System.exit(2)
  }
}

servCmd = [executable,'install'] + cmdargs
println "Executing command "+servCmd.join(" ")+"\n"

try {
  res = servCmd.execute()
  res.waitFor()
  if (res.exitValue() == 0) {
    println res.in.text
    println "\nSuccessfully installed service."
    
    if (startService) {
      println "net start $serviceName".execute().text
    }
  } else {
    println "ERROR installing service:\n" + res.err.text
  }
} catch (Exception e) {
  e.printStackTrace()
}