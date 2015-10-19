/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opennlp;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.util.Span;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
//import com.asprise.util.pdf.PDFReader;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import java.io.*;
import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.util.PlainTextByLineStream;
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
                String[] Rule={"VB->DT->NN","VBN->IN->DT","VBN->NNP->IN","VB->NN,JJ->NNS","VB->IN->DT->NN","NNP->VBD->RP->TO->DT->NN","VB->PRP->DT->NN"};  
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
                     InputStream is1 = new FileInputStream("en-ner-person.zip");
                    TokenNameFinderModel model1 = new TokenNameFinderModel(is1);
                    is1.close();
                    NameFinderME nameFinder = new NameFinderME(model1);
                    String sample[]=sentences[j].split(" ");
                    Span nameSpans[] = nameFinder.find(sample);
                    for(Span s: nameSpans)
			System.out.println("Name:"+s.toString());	
                    POSModel model2 = new POSModelLoader()
                    .load(new File("en-pos-maxent.zip"));
                    PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
                    POSTaggerME tagger = new POSTaggerME(model2);
 
                       
                    ObjectStream<String> lineStream = new PlainTextByLineStream(
			new StringReader(sentences[j]));
 
                   //perfMon.start();
                    String line;
                    while ((line = lineStream.read()) != null) {
                        
                        String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE
				.tokenize(line);
                        String[] tags = tagger.tag(whitespaceTokenizerLine);
 
                        POSSample sample2 = new POSSample(whitespaceTokenizerLine, tags);
                        System.out.println(sample2.toString());
                           String activity=null;
        String[] sample1=sample2.toString().split(" ");
        int i;
        for(i=0;i<Rule.length;i++)
        {
        String[] indRule=null;
        if(Rule[i].contains("->"))
        {
            indRule=Rule[i].split("->");
        }
        else
        {
            indRule=new String[1];
            indRule[0]=Rule[i];
        }
        int term=0;
        int ruleLen=0;
        int firstMatch=0;
        while(term<sample1.length)
        {
            String[] posWord=sample1[term].split("_");
             if(ruleLen==indRule.length)
             {
                 System.out.println("Activity:......"+activity);
                 activity=null;
                 ruleLen=0;
                 firstMatch=0;
             }
               
            if(posWord[1].matches(indRule[ruleLen]))
            {
                if(firstMatch==0)
                {
                    firstMatch=1;
                    activity=posWord[0];
                   // System.out.println(posWord[0]);
                    ruleLen++;
                }
                else
                {
                     activity=activity+" "+posWord[0];
                     ruleLen++;
                     // System.out.println(posWord[0]);
                } 
                //System.out.println(activity);
                 term++;
            }
            else
            {
                //if(activity!=null)
                  //  System.out.println(activity);
                if(firstMatch==1)
                {    
                    firstMatch=0;
                    activity=null;
                ruleLen=0;

                }
                else
                {
                    activity=null;
                    ruleLen=0;
                    term++;
                }
                
            }
                    
        }
        
    } 
                         
		//perfMon.incrementCounter();
	}
	//perfMon.stopAndPrintFinalResult();

                }
                }
               }
               
               catch(Exception Ex)
               {
                   System.out.println(Ex.getMessage());
               }  
    }          
    }

    
    /**
     *
     * @throws InvalidFormat
     */

    

