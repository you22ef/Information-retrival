/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package invertedIndex;
//import index5 class
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
            // Step 4: Sort the results by the cosine similarity score
            results = results.entrySet()
                    .stream()
                    .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (e1, e2) -> e1, LinkedHashMap::new));

            // Step 5: Print the results
            tfidf.printResults(results);
            
            
            System.out.println("Phrase search result = \n" + index.find_24_01(query));
        } while (!query.isEmpty());

    }
}