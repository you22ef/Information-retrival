package invertedIndex;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Test2 {
     public static void main(String args[]) throws IOException {
        Index5 index = new Index5();
        WikiCrawler crawler = new WikiCrawler();
        //|**  change it to your collection directory 
        //|**  in windows "C:\\tmp11\\rl\\collection\\"
        crawler.crawling();       
        String files = "is322_HW_1/src/invertedIndex/Path/";

        File file = new File(files);
        
        
        //|** String[] 	list()
        //|**  Returns an array of strings naming the files and directories in the directory denoted by this abstract pathname.
        String[] fileList = file.list();
        //print the list of files
        for (int i = 0; i < fileList.length; i++) {
            System.out.println(fileList[i]);
        }

        fileList = index.sort(fileList);
        index.N = fileList.length;

        for (int i = 0; i < fileList.length; i++) {
            fileList[i] = files + fileList[i];
        }
        TFIDF tfidf = new TFIDF();
        tfidf.BuildTFIDF(Arrays.asList(fileList));

        String query = "";

        do {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your query: ");
            query = scanner.nextLine();
            QueryPreprocessor queryPreprocessor = new QueryPreprocessor();
            // Step 1: Preprocess the query
            List<String> queryTokens = queryPreprocessor.processQuery(query);
            // Step 2: Compute TF-IDF for the query
            Map<String, Double> queryTFIDF = tfidf.computeQueryTFIDF(queryTokens);
            // Step 3: Calculate cosine similarity
            Map<String, Double> results = tfidf.calculateCosineSimilarityForAllDocuments(queryTFIDF);
            //   tfidf.printResults(results);
            tfidf.printResults(results);
        } while (!query.isEmpty());

    }
}
