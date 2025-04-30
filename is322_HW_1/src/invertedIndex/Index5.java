/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package invertedIndex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Math.log10;
import static java.lang.Math.sqrt;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.io.PrintWriter;

/**
 *
 * @author ehab
 */
public class Index5 {

    //--------------------------------------------
    int N = 0;
    public Map<Integer, SourceRecord> sources;  // store the doc_id and the file name.

    public HashMap<String, DictEntry> index; // THe inverted index
    //--------------------------------------------

    public Index5() {
        sources = new HashMap<Integer, SourceRecord>();
        index = new HashMap<String, DictEntry>();
    }

    public void setN(int n) {
        // Sets the value of N (probably the number of documents or terms, depending on context)
        N = n;
    }


    //---------------------------------------------
   public void printPostingList(Posting p) { // Prints the posting list of a given term
        while (p != null) {
            if (p.next == null) {
                System.out.print("{" + p.docId + "," + p.dtf + "}");
            } else {
                System.out.print("{" + p.docId + "," + p.dtf + "}" + ", ");
            }
            p = p.next;
        }
        System.out.println();

    }

    //---------------------------------------------
    public void printDictionary() {// Prints the entire inverted index dictionary with term frequencies and posting lists
        Iterator it = index.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            DictEntry dd = (DictEntry) pair.getValue();
            System.out.print("** [" + pair.getKey() + "," + dd.doc_freq + "]       =--> ");
            
            printPostingList(dd.pList);
        }
        System.out.println("------------------------------------------------------");
        System.out.println("*** Number of terms = " + index.size());

    }
 
    //-----------------------------------------------
    public void buildIndex(String[] files) {  // from disk not from the internet
        // Builds the inverted index from a given list of files
        int fid = 0;
        for (String fileName : files) {
            try (BufferedReader file = new BufferedReader(new FileReader(fileName))) {
                if (!sources.containsKey(fileName)) {
                    sources.put(fid, new SourceRecord(fid, fileName, fileName, "notext"));
                }
                String ln;
                int flen = 0;
                while ((ln = file.readLine()) != null) {
                    // Indexes each line from the file
                    /// -2- **** complete here ****
                    ///**** hint   flen +=  ________________(ln, fid);
                    flen += indexOneLine(ln, fid);
                }
                sources.get(fid).length = flen;

            } catch (IOException e) {
                System.out.println("File " + fileName + " not found. Skip it");
            }
            fid++;
        }
        //   printDictionary();
    }

    //----------------------------------------------------------------------------  
    public int indexOneLine(String ln, int fid) { // Processes a single line of text, tokenizes words, and adds them to the inverted index
        int flen = 0;

        String[] words = ln.split("\\W+");
      //   String[] words = ln.replaceAll("(?:[^a-zA-Z0-9 -]|(?<=\\w)-(?!\\S))", " ").toLowerCase().split("\\s+");
        flen += words.length;
        for (String word : words) {
            word = word.toLowerCase();
            if (stopWord(word)) {
                continue;
            }
            word = stemWord(word);
            // check to see if the word is not in the dictionary
            // if not add it
            if (!index.containsKey(word)) {
                index.put(word, new DictEntry());
            }
            // add document id to the posting list
            if (!index.get(word).postingListContains(fid)) {
                index.get(word).doc_freq += 1; //set doc freq to the number of doc that contain the term 
                if (index.get(word).pList == null) {
                    index.get(word).pList = new Posting(fid);
                    index.get(word).last = index.get(word).pList;
                } else {
                    index.get(word).last.next = new Posting(fid);
                    index.get(word).last = index.get(word).last.next;
                }
            } else {
                index.get(word).last.dtf += 1;
            }
            //set the term_fteq in the collection
            index.get(word).term_freq += 1;
            if (word.equalsIgnoreCase("lattice")) {

                System.out.println("  <<" + index.get(word).getPosting(1) + ">> " + ln);
            }

        }
        return flen;
    }

//----------------------------------------------------------------------------  
    boolean stopWord(String word) {// Checks if the word is a common stopword (e.g., "the", "and", "to")
        if (word.equals("the") || word.equals("to") || word.equals("be") || word.equals("for") || word.equals("from") || word.equals("in")
                || word.equals("a") || word.equals("into") || word.equals("by") || word.equals("or") || word.equals("and") || word.equals("that")) {
            return true;
        }
        if (word.length() < 2) {
            return true;
        }
        return false;

    }
//----------------------------------------------------------------------------  

    String stemWord(String word) { //skip for now -- // Stemming function (currently does nothing but can be extended to use a stemmer)
        // return word;
        Stemmer s = new Stemmer();
        s.addString(word);
        s.stem();
        return s.toString();
    }

        Posting intersect(Posting pL1, Posting pL2) {// Computes the intersection of two posting lists (for phrase or multi-word search)
    ///****  -1-   complete after each comment ****
    //   INTERSECT ( p1 , p2 )
    //          1  answer ←      {}
            Posting answer = null;
            Posting last = null;
    //      2 while p1  != NIL and p2  != NIL
            while (pL1 != null && pL2 != null) {
    //          3 do if docID ( p 1 ) = docID ( p2 )
                if (pL1.docId == pL2.docId) {
    //          4   then ADD ( answer, docID ( p1 ))
                    // answer.add(pL1.docId);
                    if (answer == null) {
                        answer = new Posting(pL1.docId);
                        last = answer;
                    } else {
                        last.next = new Posting(pL1.docId);
                        last = last.next;
                    }
    //          5       p1 ← next ( p1 )
                    pL1 = pL1.next;
    //          6       p2 ← next ( p2 )
                    pL2 = pL2.next;
    //          7   else if docID ( p1 ) < docID ( p2 )
                } else if (pL1.docId < pL2.docId) {
    //          8        then p1 ← next ( p1 )
                    pL1 = pL1.next;
    //          9        else p2 ← next ( p2 )
                } else {
                    pL2 = pL2.next;
                }
            }
    //      10 return answer
            return answer;
        }

        public String find_24_01(String phrase) { // any mumber of terms non-optimized search 
            String result = "";
            String[] words = phrase.split("\\W+");
            int len = words.length;
            
            if (len == 0) {
                return "Query is empty.";
            }
            
            Posting posting = null;
            int check = 0;
            for (int i = 0; i < len; i++) {
                if (i == check) {
                    // Handle the first word separately
                    String firstWord = words[i].toLowerCase();
                    if (stopWord(firstWord)) {
                        check++;
                        continue;
                    }
                    if (!index.containsKey(firstWord)) {
                        return "Term '" + firstWord + "' not found in index.";
                    }
                    posting = index.get(firstWord).pList;
                    continue; // Skip to the next iteration
                }
                String currentWord = words[i].toLowerCase();
                if (stopWord(currentWord)) {
                    continue;
                }
                if (!index.containsKey(currentWord)) {
                    return "Term '" + currentWord + "' not found in index.";
                }
                Posting currentPosting = index.get(currentWord).pList;
                if (i == 0) {
                    posting = currentPosting;
                } else {
                    posting = intersect(posting, currentPosting);
                }
            }
            // Format the result
            if (posting == null) {
                return "No documents found matching the phrase.";
            }

            while (posting != null) {
                //System.out.println("\t" + sources.get(num));
                SourceRecord docInfo = sources.get(posting.docId);
                if (docInfo != null) { // Check if docInfo exists
                    result += "\t" + posting.docId + " - " + docInfo.title + " - " + docInfo.length + "\n";
                } else {
                    result += "\t" + posting.docId + " - [Document Info Not Found] \n"; // Handle missing doc info
                }
                posting = posting.next;
            }
            return result.isEmpty() ? "No documents found matching the phrase." : result; // Return message if result is still empty
        }
    
    
    //---------------------------------
    String[] sort(String[] words) {  //bubble sort -- Sorts an array of strings in lexicographical (dictionary) order using the bubble sort algorithm.
        boolean sorted = false;
        String sTmp;
        //-------------------------------------------------------
        while (!sorted) {
            sorted = true;
            for (int i = 0; i < words.length - 1; i++) {
                int compare = words[i].compareTo(words[i + 1]);
                if (compare > 0) {
                    sTmp = words[i];
                    words[i] = words[i + 1];
                    words[i + 1] = sTmp;
                    sorted = false;
                }
            }
        }
        return words;
    }

     //---------------------------------

    public void store(String storageName) { // Stores the inverted index to a file for future use
        try {
            String pathToStorage = "is322_HW_1/src/invertedIndex/Path/data/"+storageName;
            Writer wr = new FileWriter(pathToStorage);
            for (Map.Entry<Integer, SourceRecord> entry : sources.entrySet()) {
                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue().URL + ", Value = " + entry.getValue().title + ", Value = " + entry.getValue().text);
                wr.write(entry.getKey().toString() + ",");
                wr.write(entry.getValue().URL.toString() + ",");
                wr.write(entry.getValue().title.replace(',', '~') + ",");
                wr.write(entry.getValue().length + ","); //String formattedDouble = String.format("%.2f", fee );
                wr.write(String.format("%4.4f", entry.getValue().norm) + ",");
                wr.write(entry.getValue().text.toString().replace(',', '~') + "\n");
            }
            wr.write("section2" + "\n");

            Iterator it = index.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                DictEntry dd = (DictEntry) pair.getValue();
                //  System.out.print("** [" + pair.getKey() + "," + dd.doc_freq + "] <" + dd.term_freq + "> =--> ");
                wr.write(pair.getKey().toString() + "," + dd.doc_freq + "," + dd.term_freq + ";");
                Posting p = dd.pList;
                while (p != null) {
                    //    System.out.print( p.docId + "," + p.dtf + ":");
                    wr.write(p.docId + "," + p.dtf + ":");
                    p = p.next;
                }
                wr.write("\n");
            }
            wr.write("end" + "\n");
            wr.close();
            System.out.println("=============EBD STORE=============");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//=========================================    
    public boolean storageFileExists(String storageName){//// Checks if a storage file already exists
        java.io.File f = new java.io.File("is322_HW_1/src/invertedIndex/Path/data/"+storageName);
        if (f.exists() && !f.isDirectory())
            return true;
        return false;
            
    }
//----------------------------------------------------    
    public void createStore(String storageName) {// Creates an empty storage file
        try {
            String pathToStorage = "is322_HW_1/src/invertedIndex/Path/data/"+storageName;
            Writer wr = new FileWriter(pathToStorage);
            wr.write("end" + "\n");
            wr.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//----------------------------------------------------      
     //load index from hard disk into memory
    public HashMap<String, DictEntry> load(String storageName) {// Loads an existing index from a file into memory
        try {
            String pathToStorage = "is322_HW_1/src/invertedIndex/Path/data/"+storageName;         
            sources = new HashMap<Integer, SourceRecord>();
            index = new HashMap<String, DictEntry>();
            BufferedReader file = new BufferedReader(new FileReader(pathToStorage));
            String ln = "";
            int flen = 0;
            while ((ln = file.readLine()) != null) {
                if (ln.equalsIgnoreCase("section2")) {
                    break;
                }
                String[] ss = ln.split(",");
                int fid = Integer.parseInt(ss[0]);
                try {
                    System.out.println("**>>" + fid + " " + ss[1] + " " + ss[2].replace('~', ',') + " " + ss[3] + " [" + ss[4] + "]   " + ss[5].replace('~', ','));

                    SourceRecord sr = new SourceRecord(fid, ss[1], ss[2].replace('~', ','), Integer.parseInt(ss[3]), Double.parseDouble(ss[4]), ss[5].replace('~', ','));
                    //   System.out.println("**>>"+fid+" "+ ss[1]+" "+ ss[2]+" "+ ss[3]+" ["+ Double.parseDouble(ss[4])+ "]  \n"+ ss[5]);
                    sources.put(fid, sr);
                } catch (Exception e) {

                    System.out.println(fid + "  ERROR  " + e.getMessage());
                    e.printStackTrace();
                }
            }
            while ((ln = file.readLine()) != null) {
                //     System.out.println(ln);
                if (ln.equalsIgnoreCase("end")) {
                    break;
                }
                String[] ss1 = ln.split(";");
                String[] ss1a = ss1[0].split(",");
                String[] ss1b = ss1[1].split(":");
                index.put(ss1a[0], new DictEntry(Integer.parseInt(ss1a[1]), Integer.parseInt(ss1a[2])));
                String[] ss1bx;   //posting
                for (int i = 0; i < ss1b.length; i++) {
                    ss1bx = ss1b[i].split(",");
                    if (index.get(ss1a[0]).pList == null) {
                        index.get(ss1a[0]).pList = new Posting(Integer.parseInt(ss1bx[0]), Integer.parseInt(ss1bx[1]));
                        index.get(ss1a[0]).last = index.get(ss1a[0]).pList;
                    } else {
                        index.get(ss1a[0]).last.next = new Posting(Integer.parseInt(ss1bx[0]), Integer.parseInt(ss1bx[1]));
                        index.get(ss1a[0]).last = index.get(ss1a[0]).last.next;
                    }
                }
            }
            System.out.println("============= END LOAD =============");
            //    printDictionary();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return index;
    }
}

//=====================================================================
