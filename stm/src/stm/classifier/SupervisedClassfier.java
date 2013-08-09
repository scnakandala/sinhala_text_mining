package stm.classifier;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import stm.util.StopWordIdentifier;

public class SupervisedClassfier {
    private String[] classifiers = {"foreign","local","sport"};
    private String[][] featureWords = new String[classifiers.length][];
    private StopWordIdentifier stopWordIdentifier = new StopWordIdentifier();
    
    public SupervisedClassfier(){
        for(int i=0;i<classifiers.length;i++){
            try {
                BufferedReader reader = new BufferedReader(
                        new FileReader("./resources/news_supervized_learning/"+classifiers[i]+".txt"));
                String text = "";
                String temp = "";
                while(temp != null){
                    text += temp;
                    temp = reader.readLine();
                }
                
                text = text.replaceAll("</post>", "");
                String[] sentences = text.split("<post>");
                ArrayList<String> wordsList = new ArrayList<String>();
                for(int j=0;j<sentences.length;j++){
                    String[] words = stopWordIdentifier.removeStopWords(sentences[j]);
                    wordsList.addAll(Arrays.asList(words));
                }                
                featureWords[i] = wordsList.toArray(new String[wordsList.size()]);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(SupervisedClassfier.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SupervisedClassfier.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
