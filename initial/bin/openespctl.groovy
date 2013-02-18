openesp = new File(".").getAbsoluteFile().getParentFile().getCanonicalPath()
sep = System.getProperties().get("file.separator")

apps = ['solr', 'mcf']

def cli = new CliBuilder(usage: 'openespctl [-ed] <app>')
cli.with {
     h longOpt:'help', 'Usage information'
     e longOpt:'enable', 'Enable application'
     d longOpt:'disable', 'Disable application'
}
def opt = cli.parse(args)
def extraArguments = opt.arguments()
if( args.length == 0 || opt.h) {
  cli.usage()
  return
}

// Enable/disable service
if( opt.e || opt.d ) {
  def app = extraArguments[0]
  if (apps.contains(app)) {
    f = [openesp, 'tomcat', 'conf', 'Catalina', 'localhost', app].join(sep)
    def success
    if (opt.d) {success = new File(f+".xml").renameTo(f+".xml_disabled")}
    if (opt.e) {success = new File(f+".xml_disabled").renameTo(f+".xml")}
    if (success) {
      println "${opt.e ? "Enabled" : "Disabled"} app ${app}. Please restart to take effect."
    } else {
      println "ERROR: App ${app} is already ${opt.e ? "enabled" : "disabled"}"
    }
  } else {
    println "Unknown app ${app}, please use one of ${apps}"
  }
}
