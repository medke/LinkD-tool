package shared;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CleanFile {
   public static void main(String[] args) {
     new CleanFile().replace();
   }

   public void replace() {
      String oldFileName = "E:/_PhD/Implementation2/films-i.nt";
      String tmpFileName = "E:/_PhD/Implementation2/films-o2.nt";

      BufferedReader br = null;
      BufferedWriter bw = null;
      try {
         br = new BufferedReader(new FileReader(oldFileName));
         bw = new BufferedWriter(new FileWriter(tmpFileName));
         String line;
         while ((line = br.readLine()) != null) {
        	 
             /*
         	if (!line.contains("<http://data.linkedmdb.org/resource/film"))
         	{
         		
         	}*/
        	 /*
            if (line.contains("<http://data.linkedmdb.org/resource/"))
               line = line.replaceAll("<http://data.linkedmdb.org/resource/(.*?)/[0-9]+>", "");
           
        	 
            if (line.contains("<http://www.w3.org/2002/07/owl#sameAs>"))
                line = line.replaceAll("<http://www.w3.org/2002/07/owl#sameAs>", "");
                
            */
        	 /*
        	 if (line.contains("<http://data.linkedmdb.org/resource/"))
                 line = line.replaceAll("<http://dbpedia.org/resource/(.*?)+>", "");
                 */
        	 
              //   line = line.substring(0,line.length() - 1);
        	 line = line.replaceAll(">", "");
        	 line = line.replaceAll("<", "");
             
            bw.write(line+"\n");
 
            
           
            System.out.println("working");
            
         }
      } catch (Exception e) {
         return;
      } finally {
         try {
            if(br != null)
               br.close();
         } catch (IOException e) {
            //
         }
         try {
            if(bw != null)
               bw.close();
         } catch (IOException e) {
            //
         }
      }
      // Once everything is complete, delete old file..
      File oldFile = new File(oldFileName);
      oldFile.delete();

      // And rename tmp file's name to old file name
      File newFile = new File(tmpFileName);
      newFile.renameTo(oldFile);

   }
}