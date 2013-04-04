import java.io.File;
import java.util.Properties;

public class TestSolrConfig {

 static {
   Properties p=System.getProperties();

   File f = new File(p.getProperty("user.dir")+"/solr-config.zip");
   customSolrConfig= ( f.exists() );
 }
 public static boolean customSolrConfig;

 public static void main(String[] args){

  System.out.println("!! IN TEST CLASS !!");
  if(TestSolrConfig.customSolrConfig)
          System.out.println("solr-config.zip exists");
  else

      System.out.println("solr-config.zip DOES NOT exist");

}

}