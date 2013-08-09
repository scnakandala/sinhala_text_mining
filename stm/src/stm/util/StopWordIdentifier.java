package stm.util;

public class StopWordIdentifier {

    private SinhalaTokenizer tokenizer = new SinhalaTokenizer();
     
    public String[] removeStopWords(String str){
        // has to implement removing stop words
        return tokenizer.getWords(str);
    }
}
