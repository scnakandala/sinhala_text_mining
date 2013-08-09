package stm.util;

import java.util.Arrays;

public class SinhalaTokenizer {

    public String[] getWords(String str){
        String[] inputWords = str.split("\\s|\\.|\"|\'|-|\\?|\\!|\\(|\\)");
        String[] outputWords = new String[inputWords.length];
        
        int j = 0;
        for(int i=0;i<inputWords.length;i++){
            if(inputWords[i].length()>0 && isValidSinhalaWord(inputWords[i])){
                outputWords[j++] = inputWords[i];
            }
        }        
        return Arrays.copyOf(outputWords, j+1);
    }
    
    public boolean isValidSinhalaWord(String str){
        char[] characters = str.toCharArray();
        for(int i=0;i<characters.length;i++){
            int temp = (int)characters[i];
            if(temp<3456 || temp > 3583){
                return false;
            }
        }        
        return true;
    }
    
    public String[] getSentences(String str){
        return str.split("\\.|\\?|\\!");
    }
}
