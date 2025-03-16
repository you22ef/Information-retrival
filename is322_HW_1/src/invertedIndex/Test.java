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

/**
 *
 * @author ehab
 */
public class Test {

    public static void main(String args[]) throws IOException {
        Index5 index = new Index5();
        //|**  change it to your collection directory 
        //|**  in windows "C:\\tmp11\\rl\\collection\\"       
        String files = "D:\\Information retrival\\is322_HW_1\\is322_HW_1\\src\\invertedIndex\\Path\\";

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

        String test3 = "Youssef"; // data  should plain greatest comif
        System.out.println("Boo0lean Model result = \n" + index.find_24_01(test3));

        String phrase = "";

        do {
            System.out.println("Print search phrase: ");
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            phrase = in.readLine();
/// -3- **** complete here ****
            System.out.println("Phrase search result = \n" + index.find_24_01(phrase));
        } while (!phrase.isEmpty());

    }
}
