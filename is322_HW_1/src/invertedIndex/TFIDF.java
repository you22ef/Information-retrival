package invertedIndex;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TFIDF {
    private List<List<String>> documents;
    private Set<String> vocabulary;
    private List<Map<String, Double>> termFrequencies;
    private Map<String, Double> inverseDocumentFrequencies;
    private List<Map<String, Double>> tfidfScores;
    private List<String> rawDocuments;
    private List<String> documentsNames;

    public TFIDF() {

        this.vocabulary = new HashSet<>();
        this.termFrequencies = new ArrayList<>();
        this.inverseDocumentFrequencies = new HashMap<>();
        this.tfidfScores = new ArrayList<>();
        this.rawDocuments = new ArrayList<>();
        this.documentsNames = new ArrayList<>();
    }
 
    // method to load files from a list of file paths
    // and return the raw text of the documents
    public List<String> loadFiles(List<String> files) throws IOException {
        this.documentsNames = new ArrayList<>();
        this.rawDocuments = new ArrayList<>();

        for (String filePath : files) {
            File f = new File(filePath);
            if (f.isFile() && f.canRead()) {
                BufferedReader br = new BufferedReader(new FileReader(f));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append(" ");
                }
                br.close();
                rawDocuments.add(sb.toString());
                documentsNames.add(filePath);
            }
        }
        return rawDocuments;
    }
    // method to preprocess a list of raw documents and return a list of tokenized documents
    // and update the vocabulary with the unique tokens
    public List<List<String>> preprocessDocuments(List<String> rawDocuments) {
        TextPreprocessor tp = new TextPreprocessor();
        this.documents = new ArrayList<>();
        for (String doc : rawDocuments) {
            List<String> tokens = new ArrayList<>();
            tokens = tp.preprocessText(doc);
            for (String token : tokens) {
                vocabulary.add(token);
            }
            documents.add(tokens);
        }
        return documents;
    }
    // method to calculate term frequency for each document
    // and store it in a list of maps where each map represents a document
    // and the keys are the terms and the values are their frequencies
    public void calculateTF() {        
        for (List<String> document : documents) {
            Map<String, Double> tf = new HashMap<>();
            for (String term : document) {
                if (tf.containsKey(term)) {
                    tf.put(term, tf.get(term) + 1);
                } else {
                    tf.put(term, 1.0);
                }
            }
            for (String term : tf.keySet()) {
                double count = tf.get(term);
                double tfValue = 1 + Math.log10(count);
                tf.put(term, tfValue);
            }
            termFrequencies.add(tf);
        }
    }
   // method to calculate inverse document frequency for each term in the vocabulary
    // and store it in a map where the keys are the terms and the values are their IDF scores
    public void calculateIDF() {
        for (String term : vocabulary) {
            int docCnt = 0;
            for (Map<String, Double> tf : termFrequencies) {
                if (tf.containsKey(term)) {
                    docCnt++;
                }
            }
            int documentCount = documents.size();
            double idf = Math.log10((double)documentCount  / docCnt );
            // todo: should i optimize that if documentCount == docCnt make it +1
            inverseDocumentFrequencies.put(term, idf);

        }
    }
    // method to calculate TF-IDF scores for each document
    // and store it in a list of maps where each map represents a document
    // and the keys are the terms and the values are their TF-IDF scores
    public void calculateTFIDF() {
        for (int i = 0; i < documents.size(); i++) {
            Map<String, Double> tfidf = new HashMap<>();
            Map<String, Double> termFrequencyMap = this.termFrequencies.get(i);
            for (String term : vocabulary) {
                double tfValue = termFrequencyMap.getOrDefault(term, 0.0);
                double idfValue = inverseDocumentFrequencies.getOrDefault(term, 0.0);
                tfidf.put(term, tfValue * idfValue);
            }
            tfidfScores.add(tfidf);
        }
    }
  // method to print the TF-IDF scores for each document
    public void printTFIDF() {
        for (Map<String, Double> tfidf : tfidfScores) {
            System.out.println("document");
            for (String term : vocabulary) {
                System.out.println("Term: " + term + ", TF-IDF: " + tfidf.get(term));
            }
        }
    }
   // method to build the TF-IDF model from a list of files
    public void BuildTFIDF(List<String> files) throws IOException {
        this.rawDocuments = loadFiles(files);
        this.documents = preprocessDocuments(rawDocuments);
        calculateTF();
        calculateIDF();
        calculateTFIDF();
    }
    // method to compute TF-IDF scores for the query tokens 
    // and return a map where the keys are the terms and the values are their TF-IDF scores
    public Map<String, Double> computeQueryTFIDF(List<String> queryTokens) {
        Map<String, Double> queryTF = new HashMap<>();
        for (String token : queryTokens) {
            queryTF.put(token, queryTF.getOrDefault(token, 0.0) + 1);
        }
        for (String token : queryTF.keySet()) {
            double tf = queryTF.get(token) / queryTokens.size();
            double idf = inverseDocumentFrequencies.getOrDefault(token, 0.0);
            queryTF.put(token, tf * idf); // TF-IDF
        }
        return queryTF;
    }
    // method to calculate cosine similarity between the query and a document
    // and return the cosine similarity score
    double calculateCosineSimilarity(Map<String, Double> query, Map<String, Double> document) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (String term : vocabulary) {
            double tfidfValue = document.getOrDefault(term, 0.0);
            double queryValue = query.getOrDefault(term, 0.0);
            dotProduct += tfidfValue * queryValue;
            normA += Math.pow(tfidfValue, 2);
            normB += Math.pow(queryValue, 2);
        }
        if (normA == 0 || normB == 0) {
            return 0.0;
        } else {
            return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
        }
    }
    // method to calculate cosine similarity for all documents in the collection
    // and return a map where the keys are the document names and the values are their cosine similarity scores
    public Map<String, Double> calculateCosineSimilarityForAllDocuments(Map<String, Double> query) {
        Map<String, Double> cosineSimilarities = new HashMap<>();
        for (int i = 0; i < tfidfScores.size(); i++) {
            Map<String, Double> tfidf = tfidfScores.get(i);
            double cosineSimilarity = calculateCosineSimilarity(query, tfidf);
            cosineSimilarities.put("Document " + documentsNames.get(i), cosineSimilarity);
        }
        return cosineSimilarities;
    }
    // method to print the results of the cosine similarity calculations
    public void printResults(Map<String, Double> results) {
        for (Map.Entry<String, Double> entry : results.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}