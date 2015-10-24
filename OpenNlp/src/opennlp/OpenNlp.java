/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opennlp;
import opennlp.tools.parser.*;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.ParserFactory;

//import com.asprise.util.pdf.PDFReader;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import java.io.*;

/**
 *
 * @author Chirag
 */

public class OpenNlp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
            //System.out.println("HI");

  // always start with a model, a model is learned from training dataru
               try
               {    
                String[] Rule={"VP->VBG,NP,NN","VP->VBD,VP","VP->VBC","VP->VB,NP","VP->VBD,NP","VP->VBD","VP->VBN","NP->NNS","VP->VB,PP","VP->VB,NP","VB->VBD","VP->VBZ","VP->VBP,NP"};  
                InputStream is = new FileInputStream("en-sent.zip");
                SentenceModel model = new SentenceModel(is);
                SentenceDetectorME sdetector = new SentenceDetectorME(model);
                FileReader fname=new FileReader("9_Screenplay.txt");
                String paragraph;
                BufferedReader reader=new BufferedReader(fname);
                while((paragraph=reader.readLine())!= null)    
                {
                String sentences[] = sdetector.sentDetect(paragraph);
                for(int j=0;j<sentences.length;j++)
                {
                   System.out.println(sentences[j]);
                    InputStream is2 = new FileInputStream("en-parser-chunking.zip");
 
                        ParserModel model1 = new ParserModel(is2);
 
                                Parser parser = ParserFactory.create(model1);
 
                                 Parse topParses[] = ParserTool.parseLine(sentences[j], parser, 1);
                                 Parse[] temp=null;
                            for (Parse p : topParses)
                            {
                                p.show();
                               traverse(p);
                            }
	                    is2.close();

	//perfMon.stopAndPrintFinalResult();

                }
                }
                is.close();
               }
               
               catch(Exception Ex)
               {
                   System.out.println(Ex.getMessage());
               }  
    }          

    private static void traverse(Parse head) {
       
        if(head==null)
            return;
                 
        Parse[] temp1=head.getChildren();
                 
                for(int j2=0;j2<temp1.length;j2++)
                {
                 //String[] rules= {"VP->VBG,VP,NN","VP->VBC","VP->VB,NP","VP->VBD,NP","VP->VBD","NP->NNS","VP->VB,PP","VP->VB,NP","VB->VBD,","VP->VBZ"};  
                    String[] rules= {"VP->VBG,NP,NN","VP->VB,PP","VP->VBD,NP","VP->VBD","VP->VBP","VP->VBZ,PP","VP->VBZ,NP","VP->VBG","VP->VB,NP","VP->VBG,PP","VP->VBD,NP","VP->VBZ,NP","VP->VB,NP","PP->IN","VP->VBG,NP","VP->VBG","VP->VBD","VP->VB"};
                    for(int rulelen=0;rulelen<rules.length;rulelen++)
                    {
                        String[] temp= rules[rulelen].split("->");
                        if(head.getType().matches(temp[0]))
                        { 
                            String[] rule=null;
                            int i=0;
                            int j=0;
                            String activity=null;
                            if(temp[1].contains(",")) 
                            {
                                rule=temp[1].split(",");
                            }
                            else
                            {
                            rule=new String[1];
                            rule[0]=temp[1];
                            }
                     //  System.out.println(temp[1]);
                            int firstMatch=0;
                            while(i<temp1.length)
                            {   if(j==rule.length)
                                {
                                    j=0;
                                    firstMatch=0;
                                    activity=null;
                                }
                                if(temp1[i].getType().matches(rule[j]))
                                {
                                    if(activity==null && firstMatch==0)
                                    {
                                        
                                        activity=temp1[i].toString();
                                        firstMatch=1;
                                    }
                                    else
                                    {
                                        
                                        activity=activity+" "+temp1[i].toString();
                                    }
                                    j++;
                                }
                                else
                                {
                                    j=0;
                                    activity=null;
                                    firstMatch=0;
                                }
                                i++;
                            }
                            if(activity != null)
                            {
                                System.out.println("Activity:"+activity);
                                return;
                            }
                        }
                    
                       
                   }
                   
                   //System.out.println("Children Parse Tree Nodes:"+temp1[j2].getType()+" "+temp1[j2].toString());
                    traverse(temp1[j2]);   
                    
                }
                 
                
                
                    
                   // traverse(temp1[1]);
                } 
            
        
    }

   
    

    
    /**
     *
     * @throws InvalidFormat
     */

    

