/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package invertedIndex;
//import index5 class
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;


/**
 *
 * @author ehab
 */
public class Test {

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
        index.buildIndex(fileList);
        index.store("data.txt");
        index.printDictionary();
        TFIDF tfidf = new TFIDF();
        tfidf.BuildTFIDF(Arrays.asList(fileList));

        String query = "";

        do {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your query or 'exit' to quit: ");
            query = scanner.nextLine();
            if (query.equalsIgnoreCase("exit")) {
                break;
            }
            QueryPreprocessor queryPreprocessor = new QueryPreprocessor();
            // Step 1: Preprocess the query
            List<String> queryTokens = queryPreprocessor.processQuery(query);
            // Step 2: Compute TF-IDF for the query
            Map<String, Double> queryTFIDF = tfidf.computeQueryTFIDF(queryTokens);
            // Step 3: Calculate cosine similarity
            Map<String, Double> results = tfidf.calculateCosineSimilarityForAllDocuments(queryTFIDF);
            //sort the results by the cosine similarity score

            //   tfidf.printResults(results);
            List<Map.Entry<String, Double>> sortedResults = new ArrayList<>(results.entrySet());
            sortedResults.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

            // Step 5: View and save Top 10
            List<Map.Entry<String, Double>> top10 = sortedResults.stream().limit(10).collect(Collectors.toList());

            // Print Top 10 in the console
            System.out.println("\nTop 10 Documents:");
            top10.forEach(entry -> System.out.printf("%s : %.6f%n", entry.getKey(), entry.getValue()));

            // Save to file
            try (FileWriter writer = new FileWriter("Top10Results.txt")) {
                for (Map.Entry<String, Double> entry : top10) {
                    writer.write(String.format("%s : %.6f%n", entry.getKey(), entry.getValue()));
                }
                System.out.println("\nResults saved to 'Top10Results.txt'");
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }
        }   while (!query.isEmpty());

    }
}
