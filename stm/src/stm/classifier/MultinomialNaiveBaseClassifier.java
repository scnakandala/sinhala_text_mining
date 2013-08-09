package stm.classifier;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class MultinomialNaiveBaseClassifier extends SupervisedClassfier implements TextClassfier {
    // Uses multinomial feature set

    private Hashtable<String, Integer>[] featuresets;

    public MultinomialNaiveBaseClassifier() {
        super();
        this.featuresets = new Hashtable[classifiers.length];
        createClassifier();
    }

    private void createClassifier() {
        for (int i = 0; i < classifiers.length; i++) {
            creatFeatureSets(i);
        }
    }

    private void creatFeatureSets(int i) {
        featuresets[i] = new Hashtable<String, Integer>();
        for (int j = 0; j < featureWords[i].length; j++) {
            String word = featureWords[i][j];
            if(word==null) continue;
            if (featuresets[i].containsKey(word)) {
                Integer val = featuresets[i].get(word);
                featuresets[i].remove(word);
                featuresets[i].put(word, val + 1);
            } else {
                featuresets[i].put(word, 1);
            }
        }
    }

    @Override
    public Map<String, Double> findCategory(String str) {
        String[] inputFeatures = stopWordIdentifier.removeStopWords(str);
        double[] probabilities = new double[classifiers.length];
        double probNormalizer = 0.0;
        for (int i = 0; i < classifiers.length; i++) {
            double p = (double)numberOfArticles[i] / totalArticles;
            double priorProb = Math.log10(p);
            double likelihood = 0.0;
            inputFeatures = removeDublicates(inputFeatures);
            for (int j = 0; j < inputFeatures.length; j++) {
                if(inputFeatures[j]==null) continue;
                int count = 0;
                if (featuresets[i].contains(inputFeatures[j])) {
                    count = featuresets[i].get(inputFeatures[j]);
                }
                likelihood += Math.log10((double)(count + 1) / (featureWords[i].length + featuresets[i].size() + 1));
            }
            probabilities[i] = priorProb + likelihood;
            probNormalizer += probabilities[i];
        }

        HashMap<String, Double> output = new HashMap<String, Double>();
        for (int i = 0; i < classifiers.length; i++) {
            output.put(classifiers[i], probabilities[i] / probNormalizer);
        }
        return output;
    }

    private String[] removeDublicates(String[] input) {
        // has to remove dublicate words
        return input;
    }
}
