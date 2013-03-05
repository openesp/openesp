def cli = new CliBuilder(usage: 'install-windows-service.bat [-a] -s name -jm jvmMem -jh javaHome -p installPath -sh solrHome -sd solrDataDir -sl solrLogDir -ss startService -sc solrCloud -zr dzkrun -zh dzkhost',
                         header: 'Windows Service Installer')
cli.a longOpt: 'auto','auto install the service'
cli.s longOpt: 'serviceName', required:true, args: 1, 'Windows service name, REQUIRED'
cli.jm longOpt: 'jvmMem', args: 1, 'JVM memory, REQUIRED'
cli.jh longOpt: 'javaHome', args: 1, 'value of JAVA_HOME, REQUIRED'
cli.p longOpt: 'installPath', args: 1, 'OpenESP install path, REQUIRED'
cli.sh longOpt: 'solrHome', args: 1, 'Solr home directory, REQUIRED'
cli.sd longOpt: 'solrDataDir', args: 1, 'Solr data directory, REQUIRED'
cli.sl longOpt: 'solrLogDir', args: 1, 'Solr log directory, REQUIRED'
cli.ss longOpt: 'startService', args: 1, 'Start service after install: true|false, REQUIRED'
cli.sc longOpt: 'solrCloud', args: 1, 'Solr Cloud Enabled: true|false, REQUIRED'
cli.zr longOpt: 'dzkrun', args: 1, 'Start local zookeeper: true|false, REQUIRED'
cli.zh longOpt: 'dzkhost', args: 1, '+ Delimited list of zookeeper hosts, ex: host1:por1+host2.port2+host3.port3, REQUIRED'
cli.h longOpt: 'help', 'print usage information'
def opt = cli.parse(args)
def errMsg1 = "Invalid arguments.\nusage: ${cli.usage}\n" + 
        "Try `install-windows-service.bat --help' for more information."
def errMsg2 = "Invalid arguments. \nWhen not using option -auto all other arguments are REQUIRED\n\nusage: ${cli.usage}\n\n" + 
        "Try `install-windows-service.bat --help' for more information."

def serviceName="OpenESP"
def jvmMem = 2048
def env = System.getenv()
def javaHome = env['JAVA_HOME']
def scriptDir = new File(getClass().protectionDomain.codeSource.location.path).parent
def installPath = new File(scriptDir).getAbsoluteFile().getParentFile().getCanonicalPath()
def solrHome = installPath + "\\conf\\solr" 
def solrDataDir = installPath + "\\data\\solr"
def solrLogDir = installPath + "\\logs\\solr"
def startService = true
def solrCloud = false
def dzkrun = false
def dzkhost = ""

def executable = scriptDir + "\\createService.bat"

executable = "\""+executable.replaceAll("%20", " ")+"\""
scriptDir = scriptDir.replaceAll("%20", " ")
installPath = installPath.replaceAll("%20", " ")
solrHome = solrHome.replaceAll("%20", " ")
solrDataDir = solrDataDir.replaceAll("%20", " ")
solrLogDir = solrLogDir.replaceAll("%20", " ")

def javaOpts=""

//Read extra java_opts from service_java_opts.properties

def file = new File(scriptDir + "\\service_java_opts.properties")
file.eachLine { line ->
	javaOpts = javaOpts + line + ";"
}



if (!opt) {
    //should never happen, since I don't have required parameters in CliBuilder
    println "error processing arguments\n"
} else if(opt.auto) {

		serviceName = opt.s

		def servCmd = executable + " " + jvmMem + " \""+installPath+"\" \"" + javaOpts +  "\" \"" + solrHome+"\" \"" + solrDataDir+"\" \"" + solrLogDir + "\" " + startService +" \"" + javaHome + "\" " + serviceName + " " + solrCloud + " " + dzkrun + " " + dzkhost 

		println servCmd.execute().text


} else if (opt.h) {
    cli.usage()
} else if (!opt.auto) {
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

	if(!opt.jvmMem || !opt.javaHome || !opt.installPath || !opt.solrHome || !opt.solrDataDir || !opt.solrLogDir || !opt.startService || !opt.solrCloud || !opt.dzkrun || !opt.dzkhost)
		println errMsg2
	else 	{
				def servCmd = executable + " " + jvmMem + " \""+installPath+"\" \"" + javaOpts +  "\" \"" + solrHome+"\" \"" + solrDataDir+"\" \"" + solrLogDir + "\" " + startService +" \"" + javaHome + "\" " + serviceName + " " + solrCloud + " " + dzkrun + " " + dzkhost 

		println servCmd.execute().text
	}

} else  {     
    println errMsg1
}

		cli=null
		System.gc()