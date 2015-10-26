/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework3;
import java.io.*;
import java.util.*;
/**
 *
 * @author Chirag
 */

 class obserVation
 {
    String observation;
    int count;
    double prob;
 }

class state
{
 String stateName;
 ArrayList<obserVation> al;

    state() {
        this.al = new  ArrayList<>();
    }
}

class prevStates
{
    String stateName;
    double prob;
}
class stateTransition
{
 state firstState;
 state secondState;
 int count;
 double prob;
}

public class HomeWork3 {

    /**
     * @param args the command line arguments
     */
            
    @SuppressWarnings("empty-statement")
    public static void main(String[] args) {
        // TODO code application logic here
       // stateTransition[] st=new stateTransition[];
         String line;
            //state prevState=new state();
         ArrayList<state> allStates=new ArrayList<>();
         int stateTransitions=-1;
         String fname="entrain.txt";
         String fname1="entest.txt";
         ArrayList<stateTransition> stateTrans=new ArrayList<>();
        try
        {
            state prevState=null;
            FileReader f;
            f = new FileReader(fname);
             try (BufferedReader br = new BufferedReader(f)) {
                while((line = br.readLine()) != null)
                {
                    if(line.matches(""))
                        break;
                    int isBoundary=0;
                    System.out.println(line+stateTransitions);
                    stateTransitions=stateTransitions+1;
                    state newState=new state();
                    state newState1=null;
                    if(line.matches("###/###"))
                    {
                        if(stateTransitions==0)
                        { 
                            newState.stateName="Start";
                            allStates.add(newState);
                        }
                        else
                        {
                            newState.stateName="End";
                            int isState=0;
                            newState1=new state();
                            newState1.stateName="Start";
                            for (state temp : allStates) {
                                if(temp.stateName.matches(newState.stateName))
                                {
                                    isState=1;
                                    break;
                                }
                            }
                            if(isState==0)
                            {
                                allStates.add(newState);
                                 Iterator al=allStates.iterator();
                            }
                            isBoundary=1;
                        }
                      }
                    else if(!line.matches("###/###") && stateTransitions>0)
                    {
                      String[] stateObs=line.split("/");
                      newState.stateName=stateObs[1];
                      int isState=0;
                        for (state temp : allStates) {
                            if(temp.stateName.matches(newState.stateName)||temp.stateName.contains("$"))
                            {
                                isState=1;
                                newState=temp;
                                int isOb=0;
                                for (obserVation tempOb : temp.al) {
                                    if(stateObs[0].matches(tempOb.observation))
                                    {
                                        isOb=1;
                                        tempOb.count=tempOb.count+1;
                                    }
                                }
                                if(isOb==0)
                                {
                                    obserVation tempOb=new obserVation();
                                    tempOb.count=1;
                                    if(stateObs[0].contains("?"))
                                    {
                                        stateObs[0]=stateObs[0].replace("?", "/?");
                                    }
                                    tempOb.observation=stateObs[0];
                                    temp.al.add(tempOb);
                                }
                            }
                        }
                          if(isState==0)
                          {
                           state temp=new state();
                           temp.stateName=stateObs[1];
                           obserVation ob=new obserVation();
                           ob.count=1;
                           ob.observation=stateObs[0];
                           temp.al.add(ob);
                           newState=temp;
                           allStates.add(temp);
                        }
                           
                    }
                   
                    if(stateTransitions==0)
                    {    
                        prevState=newState;
                    }
                    else
                    {
                        int isStateTrans=0;
                        for (stateTransition st : stateTrans) {
                            if(newState.stateName.contains(st.secondState.stateName) && prevState.stateName.contains(st.firstState.stateName))
                            {
                                st.count=st.count+1;
                                isStateTrans=1;
                                break;
                            }
                        }
                        if(isStateTrans==0)
                        {
                          stateTransition st = new stateTransition();
                          st.firstState=prevState;
                          st.secondState=newState;
                          st.count=1;
                          stateTrans.add(st);
                        }
                        if(isBoundary==0)
                        prevState=newState;
                        else
                        prevState=newState1;    
                    }
                            
                    }  
                    f.close();
                            for (stateTransition temp : stateTrans) {
                            temp.prob=temp.count/(double)stateTransitions;
                            System.out.println(temp.firstState.stateName+"->"+temp.secondState.stateName+" "+temp.count+" "+temp.prob);
                            }
                            Iterator al=allStates.iterator();
                            while(al.hasNext())
                            { 
                                int total=0;
                                state temp1=(state)al.next();
                                System.out.println("StateName:"+temp1.stateName);
                                for (obserVation ob1 : temp1.al) {
                                total=total+ob1.count;
                                }
                                for (obserVation ob1 : temp1.al) {
                                ob1.prob=ob1.count/(double)total;
                                 System.out.println("Observation:"+ob1.observation+" "+ob1.count+" "+ob1.prob);
                                 //   total=total+ob1.count;   
                                }
                                
                            } 
            }
               
                FileReader f1;
                ArrayList<String> obarr=new ArrayList<>();
                ArrayList<String> correctTags=new ArrayList<>();
                ArrayList<String> calcTags=new ArrayList<>();
                f1 = new FileReader(fname1);
                int transitions=-1;
                int newLine=0;
                int totalWords=0;
                double errorWords=0; 
                double totalError=0;
                try (BufferedReader br1 = new BufferedReader(f1)) {
                while((line = br1.readLine()) != null)
                {
                    System.out.println(line);
                    
                    if(line.matches(""))
                        break;
                    transitions=transitions+1;
                    if(line.contains("###/###"))
                    {
                        if(transitions==0)
                        {
                            obarr.add("Start");
                        }
                        else
                        {
                            if(newLine==0)
                            {
                                obarr.add("End");
                                calcTags=viterbiCalc(obarr,stateTrans,allStates);
                               for(String Tags:calcTags)
                                {
                                    System.out.println("New Tags:"+Tags);
                                }
                                errorWords=errortagsfunction(calcTags,correctTags);
                               totalError=totalError+errorWords;
                               newLine=1;
                            }
                            if(newLine==1)
                            {
                                obarr.clear();
                                correctTags.clear();
                                obarr.add("Start");
                                newLine=0;
                            }    
                           
                        }    
                    }
                    else
                    {
                        String[] tempOb=line.split("/");
                        if(tempOb[0].contains("?"))
                        tempOb[0]="/?";
                        obarr.add(tempOb[0]);
                        correctTags.add(tempOb[1]);
                        totalWords=totalWords+1;
                    }
                    
                }
                System.out.println("Total Number of words:"+totalWords);
                System.out.println("Error Rate:"+(totalError/totalWords));
                }
             
        }
           
        
        catch(Exception ex)
        {
            System.out.println(ex.getMessage()+"error Message");
        }
    }

    private static ArrayList<String> viterbiCalc(ArrayList<String> obarr, ArrayList<stateTransition> stateTrans, ArrayList<state> allStates) {
        state prevState=null;
        int isObfound=0;
        state currState=null;
        obserVation currOb=null;
        int stateCount=0;
        ArrayList<String> correctTag=new ArrayList<>();
        ArrayList<stateTransition> st=new ArrayList<>();
        for(String obs:obarr)
        {
           isObfound=0;
            if(obs.matches("Start"))
            {
                prevState=new state();
                prevState.stateName="Start";
                correctTag.add("Start");
            }
            else if(obs.matches("End"))
            {
                correctTag.add("End");
            }    
            else
            {
                currOb=new obserVation();
                currOb.observation=obs;
                
                System.out.println("Working on ObserVation"+currOb.observation);
                //String prevStates=correctTag.get(correctTag.size()-1);
                prevState.stateName=correctTag.get(correctTag.size()-1);
                //int firstCheck=0;
                for(state states:allStates)
                {
                   int firstCheck=0;
                   for(obserVation ob:states.al)
                    {
                        if(currOb.observation.contains(ob.observation) )
                        {
                            isObfound=1;
                            currState=new state();
                            currOb.prob=ob.prob;
                            currState.stateName=states.stateName;
                            System.out.println("Observation found in state"+currState.stateName+ob.prob);
                            currState.al.add(currOb);
                            for(stateTransition st1:stateTrans)
                           {
                               
                                 if(st1.firstState.stateName.matches(prevState.stateName) && st1.secondState.stateName.matches(currState.stateName))
                                {
                                    System.out.println("Transaction Found"+firstCheck);
                                    stateTransition tempTrans=new stateTransition();
                                    
                                    tempTrans.firstState=st1.firstState;
                                    tempTrans.secondState=st1.secondState;
                                    tempTrans.prob=currOb.prob*st1.prob;
                                       
                                             
                                            if(firstCheck==1)
                                             {
                                              stateTransition tempTrans2=st.get(st.size()-1);
                                              System.out.println("Here"+tempTrans2.prob+" "+tempTrans.prob);
                                             if(tempTrans2.prob<tempTrans.prob)
                                                {
                                                    st.remove(st.size()-1);
                                                    st.add(tempTrans);
                                                    correctTag.remove(correctTag.size()-1);
                                                     System.out.println("Correct Tags1:"+correctTag.get(correctTag.size()-1));
                                                    correctTag.add(tempTrans.secondState.stateName);
                                            }
                                             }
                                            if(firstCheck==0)
                                            {
                                            firstCheck=1;
                                            st.add(tempTrans);
                                            correctTag.add(tempTrans.secondState.stateName);
                                            System.out.println("Correct Tags2:"+correctTag.get(correctTag.size()-1));
                                            
                                             }
                                            
                                            
                                }
                                }
                            
                        }
                        
                    }
                   
                }
                if(isObfound==0)
                {
                    //String currTag=null;
                    int firstMatch=0;
                    for(stateTransition statetrans:stateTrans)
                    {
                        if(statetrans.firstState.stateName.matches(prevState.stateName))
                        {
                            if(firstMatch==0)
                            {
                                st.add(statetrans);
                                correctTag.add(statetrans.secondState.stateName);
                                firstMatch=1;
                            }
                            else
                            {
                                stateTransition tempTransState=st.get(st.size()-1);
                                if(tempTransState.prob<statetrans.prob)
                                {
                                    st.remove(st.size()-1);
                                    st.add(statetrans);
                                    correctTag.remove(correctTag.size()-1);
                                    System.out.println("Correct Tags:"+statetrans.secondState.stateName);
                                    correctTag.add(statetrans.secondState.stateName);
                                }
                            }  
                        }
                    
                } 
                }
            }
      
        }    
        return correctTag;
    }

    private static double errortagsfunction(ArrayList<String> calcTags, ArrayList<String> correctTags) {
        double errors=0;
        //System.out.println("Clear from here");
        for(int i=0;i<calcTags.size()-1;i++)
        {
            if(!calcTags.get(i).matches(correctTags.get(i)))
            {
                System.out.println("Clear from here");
                errors=errors+1;
            }
        }
        
        return errors;
    }
    
}
