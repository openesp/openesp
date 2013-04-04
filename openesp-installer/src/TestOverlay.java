import java.io.File;
import java.util.Properties;

public class TestOverlay {

 static {
   Properties p=System.getProperties();

   File f = new File(p.getProperty("user.dir")+"/openesp-overlay.zip");
   customOverlay = ( f.exists() );
 }
 public static boolean customOverlay;

 public static void main(String[] args){

  System.out.println("!! IN TEST CLASS !!");
  if(TestOverlay.customOverlay)
          System.out.println("openesp-overlay.zip exists");
  else

      System.out.println("openesp-overlay.zip DOES NOT exist");

}

}