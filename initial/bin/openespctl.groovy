import groovy.xml.XmlUtil
import groovy.xml.StreamingMarkupBuilder
import groovy.util.slurpersupport.GPathResult

// Find script home and set that on base class
def scriptDir = new File(getClass().protectionDomain.codeSource.location.path).parent
def openespDir = new File(scriptDir).getAbsoluteFile().getParentFile().getCanonicalPath()
def openEspEnv = System.getenv().get("OPENESP_HOME")
CtlBase.setHome(openEspEnv == null ? openespDir : openEspEnv)

// List available commands
commands = ['help', 'disable', 'enable', 'upgrade', 'port', 'help']

// Only legal 
if( args.size() < 1 
    || args[0] == "help" && (args.size() < 2 )) 
{
  usage()
  return
}

// Parse initial command
def cmd = "N/A"
for(a in args) {
  if (commands.contains(a)) {
    cmd = a
    break
  }
}

if (cmd == "help") {
  cmd = args[1]
  args = ["-h", cmd].toListString()
}
switch (cmd) {
  case "enable":
  case "disable":
    AppControl.main(args)
    break
  
  case "port":
    PortReplace.main(trimargs(args))
    break

  case "upgrade":
    Upgrader.main(trimargs(args))
    break
  
  default:
    println "Unknown command."
    usage()   
    return
}
System.exit(0)

def trimargs(args) {
  return args.size() > 1 ? args[1..-1].toArray(new String[1]) : ""
}

def usage() {
  println "Usage: openespctl [options] <command> [command arguments]"
  println "Valid commands are ${commands}."
  println "Use help <command> for more help on a command\n"
  println "Examples:"
  println "  openespctl help disable"
  return
}




// **************************************
// ************** CLASSES ***************
// **************************************

/**
 * Base class defining some static variables
 */
public class CtlBase {
  static sep = System.getProperties().get("file.separator")
  static apps = ['solr', 'mcf']
  static openesp = null

  static{
    if (openesp == null) {
      def defOpenespDir = new File(".").getAbsoluteFile().getParentFile().getParentFile().getCanonicalPath()
      def openEspEnv = System.getenv().get("OPENESP_HOME")
      openesp = (openEspEnv != null) ? openEspEnv : defOpenespDir
    }
  }
  
  /* Set home directory for OpenESP */
  public static setHome(home){
    openesp = home
  }
  
  /**
   * Function to manipulate a text file in place
   */
  def processFileInplace(file, Closure processText) {
    def text = file.text
    file.write(processText(text))
  }

  public String serializeXml(GPathResult xml){
    XmlUtil.serialize(new StreamingMarkupBuilder().bind {
      mkp.yield xml
    } )
  }
}

/**
 * Class to enable or disable a webapp from Tomcat 
 */
public class AppControl extends CtlBase {
  public static void main(String[] args) {
    if (args[0] == "-h" || args.size() < 2) {
      println "Usage: openespctl <enable|disable> <app>"
      println "  where <app> is one of ${apps}"
      return
    }
    def enable = (args[0] == "enable")
    def app = args[1]
    
    AppControl ac = new AppControl()
    ac.change(app, enable)
  }
  
  public change(app, enable) {
    if (apps.contains(app)) {
      def f = [openesp, 'tomcat', 'conf', 'Catalina', 'localhost', app].join(sep)
      def success
      if (!enable) {success = new File(f+".xml").renameTo(f+".xml_disabled")}
      if (enable) {success = new File(f+".xml_disabled").renameTo(f+".xml")}
      if (success) {
        println "${enable ? "Enabled" : "Disabled"} app ${app}. Please restart to take effect."
      } else {
        if (!new File(f+".xml").exists() && !new File(f+".xml_disabled").exists()) {
          println "ERROR: Could not find descriptor file, is ${openesp} the correct OPENESP_HOME?"
        } else {
          println "ERROR: App ${app} is already ${enable ? "enabled" : "disabled"}"
        }
      }
    } else {
      println "Unknown app ${app}, please use one of ${apps}"
    }
  }
}

/**
 * Port replacement in Tomcat's server.xml
 */
public class PortReplace extends CtlBase {
  public static void main(String[] args) {
    def cli = new CliBuilder(usage: 'openespctl port <new port number> [<new ssl port number>]')
    def opt = cli.parse(args)
    cli.with {
         h longOpt:'help', 'About the port command'
    }
    def xargs = opt.arguments()
    if (xargs.size() == 1 && xargs[0] == "" || xargs.size() > 2 || opt.h) {
      cli.usage()
      return
    }
    def port = xargs[0]
    def sslport = xargs.size() > 1 ? xargs[1] : null
    def serverFile = new File([openesp, 'tomcat', 'conf', 'server.xml'].join(sep))
    
    PortReplace pr = new PortReplace()
    try {
      def res = pr.change(serverFile, port, sslport)
      println "Changed normal port from ${res[0]} to ${port}"
      if (sslport != null)
        println "Changed ssl port from ${res[1]} to ${sslport}"
    } catch (Exception e) {
      println "Port change failed. Is ${openesp} the correct OPENESP_HOME?"
    }
  }

  /**
   * Replaces port numbers in Tomcat server.xml
   * Returns the old ports in a String array
   */
  public String[] change(file, newPort, newSslPort) {
    String[] res = new String[2]
    processFileInplace(file) { text ->
        def server = new XmlSlurper().parseText(text)
        def normalconn = server.Service.Connector.find{ it.@SSLEnabled == 'false' }
        def sslconn = server.Service.Connector.find{ it.@SSLEnabled == 'true' }
        normalconn.each() { c ->
          res[0] = c.@port
          c.@port = newPort
        }
        if (newSslPort != null) {
          sslconn.each() { c ->
            res[1] = c.@port
            c.@port = newSslPort
          }
        }
        serializeXml(server)
    }      
    return res
  }
}

public class Upgrader extends CtlBase {
  public static void main(String[] args) {
    def cli = new CliBuilder(usage: 'openespctl upgrade [-d <destination>] <openesp-zip-file>')
    cli.d(longOpt:'dest', 'Openesp-home destination folder', args:1)
    cli.h(longOpt:'help', "Help")
    def opt = cli.parse(args)
    def xargs = opt.arguments()
    if (xargs.size() < 2) {
      cli.usage()
      return
    }
    def zip = xargs[0]
    def dest = opt.d ? opt.d : ".."

    Upgrader u = new Upgrader()
    u.upgrade(dest, zip)
  }
  
  public boolean upgrade(existingFolder, newZip) {
    println "DEBUG: dest=${existingFolder}, zipFile=${newZip}"
  }
}