import groovy.xml.XmlUtil
import groovy.xml.StreamingMarkupBuilder
import groovy.util.slurpersupport.GPathResult

// Find script home and set that on base class
def scriptDir = new File(getClass().protectionDomain.codeSource.location.path).parent
def openespDir = new File(scriptDir).getAbsoluteFile().getParentFile().getCanonicalPath().replaceAll("%20", " ");
def openEspEnv = System.getenv().get("OPENESP_HOME")
CtlBase.setHome(openEspEnv == null ? openespDir : openEspEnv)

// List available commands
commands = ['help', 'disable', 'enable', 'port', 'installrecords']
commands_win = ['service']
commands_nonwin = ['daemon']
is_windows = System.properties['os.name'].toLowerCase().contains('windows')
commands = commands.plus(is_windows ? commands_win : commands_nonwin)
 

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
  args = Arrays.asList(["-h", cmd])
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

  case "daemon":
    Linuxdaemon.main(trimargs(args))
    break

  case "service":
    println "Not yet implemented"
    break

  case "installrecords":
    Installrecords.main(trimargs(args))
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
        def scriptDir = new File(CtlBase.class.protectionDomain.codeSource.location.path).parent
        def openespDir = new File(scriptDir).getAbsoluteFile().getParentFile().getCanonicalPath().replaceAll("%20", " ");
        def openEspEnv = System.getenv().get("OPENESP_HOME")
        openesp = (openEspEnv != null) ? openEspEnv : openespDir
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

public class Linuxdaemon extends CtlBase {
  public static void main(String[] args) {
    def cli = new CliBuilder(usage: 'openespctl daemon [--openesphome=<home>,--name=<scriptname>,--memory=<NNm>,--javahome=<java_home>] <install | uninstall | start | stop | status>')
    cli.o(longOpt:'openesphome', 'OpenESP-home folder', args:1)
    cli.j(longOpt:'javahome', 'java_home location', args:1)
    cli.m(longOpt:'memory', 'JVM memory', args:1)
    cli.n(longOpt:'name', 'script name', args:1)
    cli.h(longOpt:'help', "Help")
    def opt = cli.parse(args)
    if(!opt) {
      return
    }
    def xargs = opt.arguments()
    if (xargs.size() < 1) {
      cli.usage()
      return
    }
    def cmd = xargs[0]

    Linuxdaemon ld = new Linuxdaemon()

    if(opt.h) {
     cli.usage()
     return
    }

    if(!opt.n) {
      println "Missing required option -n"
      cli.usage()
      return
    }
     

    if(cmd=="install") {
       def scriptFile = new File("/etc/init.d/"+opt.n)
       if(scriptFile.exists()) {
              println "Linux daemon with name " + opt.n + " already exists"
              return
       }

       if(!opt.j) {
            println "-j|--javahome parameter is required for install"
            return
       }
       if( opt.o) {
           def directory = new File(opt.o)
	   if(!directory.exists()) {
		println  opt.o + " directory does not exist"
		return
	   }
	   else
            ld.install( opt.o, opt.n, opt.j, opt.m)
       }
       else  {
             ld.install( openesp, opt.n, opt.j, opt.m)
       }
    }
    else if(cmd=="uninstall")
       ld.uninstall( opt.n)
    else if(cmd=="start")
       ld.start(opt.n)
    else if(cmd=="stop")
       ld.stop(opt.n)
    else if(cmd=="status")
       ld.status(opt.n)
    else {
        println "Invalid command"
        cli.usage()
        return
    }

  }

 public boolean install(openesphome, scriptname, javahome, memory) {
        println "Installing " + scriptname + " -- home = " + openesphome
        
        def file = new File(openesphome + "/bin/openesp.sh")
        def fileText = file.text;
	def scriptFile = new File("/etc/init.d/" + scriptname);
	//scriptFile.write(fileText);

	//Read java_opts from java_opts.properties
	def javaOpts = "export JAVA_OPTS=\""

        def joptsFile = new File(openesphome + "/bin/java_opts.properties")
            joptsFile.eachLine { line ->
                          if(memory && line.startsWith("-Xms"))
                                    javaOpts = javaOpts + "-Xms"+memory+"M -Xmx"+memory+"M \\\n"
                          else if(line.startsWith("-Dzkrun")) {
                               line = line.replaceAll("\\+",",")
                               javaOpts = javaOpts + line + " \\\n"
                          }
                          else
                              javaOpts = javaOpts + line + " \\\n"
        }
        javaOpts = javaOpts + "\"\n"

        file.eachLine { line ->
                      if(line.startsWith("#OPENESP_HOME=")) {
                          scriptFile << ("OPENESP_HOME="+openesphome+"\n")
                      }
                      else if(line.startsWith("#export JAVA_OPTS=")) {
                           scriptFile << (javaOpts)
                      }
                      else if(line.startsWith("export JRE_HOME")) {
                           scriptFile << ("export JRE_HOME="+javahome + "\n")
                      }
                      else if(line.startsWith("export JAVA_HOME")) {
                           scriptFile << ("export JAVA_HOME="+javahome + "\n")
                      }
                      else {
                           scriptFile << (line +"\n")
                      }
        }
        
        // set permissions for script to /etc/init.d
        def cmdPerm = "chmod 755 /etc/init.d/" + scriptname

        println cmdPerm.execute().text

 }
 
 public boolean uninstall(scriptname) {
        stop(scriptname)
        println "Uninstalling " + scriptname
        def cmdDel = "rm -f /etc/init.d/" + scriptname
        println cmdDel.execute().text
 }

  public boolean start(scriptname) {
        println "Starting " + scriptname
        def cmdStart = "/etc/init.d/" + scriptname + " start"
        println cmdStart.execute().text
 }
 
   public boolean stop(scriptname) {
        println "Stopping " + scriptname
        def cmdStop = "/etc/init.d/" + scriptname + " stop"
        println cmdStop.execute().text
 }
 
   public boolean status(scriptname) {
        println "Status for  " + scriptname
        def cmdStatus = "/etc/init.d/" + scriptname + " status"
        println cmdStatus.execute().text
 }

}
public class Installrecords extends CtlBase {
  public static void main(String[] args) {
    def cli = new CliBuilder(usage: 'openespctl installrecords [options] <add | remove>')
    cli.o(longOpt:'openesphome', 'Openesp-home folder', args:1)
    cli.n(longOpt:'name', 'script or service name', args:1)
    cli.v(longOpt:'version', 'version', args:1)
    cli.h(longOpt:'help', "Help")
    def opt = cli.parse(args)
    if(!opt) {
      return
    }
    def xargs = opt.arguments()
    if (xargs.size() < 1) {
      println "Invalid command"
      cli.usage()
      return
    }
    def cmd = xargs[0]

    Installrecords ir = new Installrecords()

    if(opt.h) {
     cli.usage()
     return
    }

  if(cmd) {
    if(!opt.o || !opt.n || !opt.v) {
           println "Missing required option: -o, -n and -v are all required"
           cli.usage()
           return
    }
    switch (cmd) {
           case "add":
           ir.add(opt.o, opt.n, opt.v)
           break
  
           case "remove":
           ir.remove(opt.o, opt.n, opt.v)
           break

           default:
           println "Unknown command."
           cli.usage()
           return
    }
  }
  else {
       println "Invalid command"
           cli.usage()
           return
  }
 }
 
 public boolean isWindows() {
   if (System.properties['os.name'].toLowerCase().contains('windows')) {
    return true
    } else {
    return false
    }
 }

 public boolean add(openesphome, name, version) {
   println "Adding install record for location: " + openesphome + ", service name: " + name  + " and version: " + version

   def file
   def fileText
   def newrec
   def header = "date,version,location,service.name,init.script\n"

   def format = new java.text.SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")
   def datetime = format.format( new Date()  )

   if (isWindows()) {
      def dir = new File("C:\\ProgramData\\OpenESP").mkdir()
      file = new File("C:\\ProgramData\\OpenESP\\openesp-installs.csv")
      newrec = datetime + "," + version + "," + openesphome + "," + name + ",\n"
    } else {
      file = new File("/etc/openesp-installs.csv")
      newrec = datetime + "," + version + "," + openesphome + ",,/etc/init.d/" + name + ",\n"
    }

     if(file.exists()) {
       fileText = file.text
       fileText = fileText +  newrec
     }
     else  {
       fileText = header + newrec
     }

    file.write(fileText)
 }

 public boolean remove(openesphome, name, version) {
   println "Removing install record for location: " + openesphome + ", service name: " + name  + " and version: " + version
    
    def file

   if (isWindows()) {
      def dir = new File("C:\\ProgramData\\OpenESP").mkdir()
      file = new File("C:\\ProgramData\\OpenESP\\openesp-installs.csv")
    } else {
      file = new File("/etc/openesp-installs.csv")
      name = "/etc/init.d/" + name
    }

     if(!file.exists()) {
        println "Install records file DOES NOT exist !"
        return
     }

    def newText=""
    def found = false
    openesphome = "," + openesphome + ","
    name =  "," + name + ","
    version = "," + version + ","

    file.eachLine { line ->
              if(!line.contains(openesphome) || !line.contains(name) || !line.contains(version))
                 newText = newText + line +"\n"
              else
                 found=true
    }
    
    if(found)   {
             file.write(newText)
             println "install record has been removed"
    }
    else
        println "This record was not found, can not be removed !"

 }
}
