def cli = new CliBuilder(usage: 'uninstall-windows-service.bat -s name', 
                         header: 'Windows Service Uninstaller')
cli.s longOpt: 'serviceName', required:true, args: 1, 'Windows service name, REQUIRED'
cli.h longOpt: 'help', 'print usage information'
def opt = cli.parse(args)
def errMsg1 = "Invalid arguments.\nusage: ${cli.usage}\n" + 
        "Try `uninstall-windows-service.bat --help' for more information."

def serviceName=""
def scriptDir = new File(getClass().protectionDomain.codeSource.location.path).parent
def installPath = new File(scriptDir).getAbsoluteFile().getParentFile().getCanonicalPath().replaceAll("%20", " ")

def executable = "\""+ installPath + "\\tomcat\\bin\\tomcat7.exe\""


if (!opt) {
    //should never happen, since I don't have required parameters in CliBuilder
    println "error processing arguments\n"
}  else if (opt.h) {
    cli.usage()
} else if (opt.s) {

		serviceName = opt.s
		def servCmd = executable + " //DS//" +serviceName 
		//println servCmd
		println servCmd.execute().text
} else  {     
    println errMsg1
}

		cli=null
		System.gc()