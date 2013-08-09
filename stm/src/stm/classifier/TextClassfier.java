package stm.classifier;

import java.util.Map;

public interface TextClassfier {

    public Map<String, Double> findCategory(String str);
    
}
