package invertedIndex;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class RankTopDocuments {
    public static void main(String[] args) {
        try {
            // Specify the folder path containing the documents
            String folderPath = "Information-retrival-main/Information-retrival-main/is322_HW_1/data";

            // Get list of document file paths
            List<String> documentPaths = FileHandler.getDocumentPaths(folderPath);

            // Create TFIDF object and build the model
            TFIDF tfidf = new TFIDF();
            tfidf.BuildTFIDF(documentPaths);

            // Your search query
            String queryText = "your search terms here";

            // Preprocess query
            TextPreprocessor tp = new TextPreprocessor();
            List<String> queryTokens = tp.preprocessText(queryText);

            // Compute TF-IDF for query
            Map<String, Double> queryTFIDF = tfidf.computeQueryTFIDF(queryTokens);

            // Compute Cosine Similarity for all documents
            Map<String, Double> cosineSimilarities = tfidf.calculateCosineSimilarityForAllDocuments(queryTFIDF);

            // Sort the documents by similarity (descending)
            List<Map.Entry<String, Double>> sortedDocs = new ArrayList<>(cosineSimilarities.entrySet());
            sortedDocs.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));

            // Output top 10 results
            System.out.println("Top 10 Documents:");
            FileWriter writer = new FileWriter("top10_results.txt");

            int count = 0;
            for (Map.Entry<String, Double> entry : sortedDocs) {
                if (count >= 10) break;
                String outputLine = String.format("%s : %.6f", entry.getKey(), entry.getValue());
                System.out.println(outputLine);
                writer.write(outputLine + "\n");
                count++;
            }
            writer.close();
            System.out.println("Top 10 results saved to 'top10_results.txt'.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
