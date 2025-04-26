package invertedIndex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TextPreprocessor {
    // method to check if a word is a stop word
    public static boolean isStopWord(String word) {
        if (word == null)
            return false;

        Set<String> stopWords = new HashSet<>(
                Arrays.asList("a", "an", "the", "and", "or", "but", "if", "while", "is", "are",
                        "was", "were", "be", "been", "being", "have", "has", "had", "do",
                        "does", "did", "in", "on", "at", "of", "for", "to", "from", "by", "into",
                        "with", "about", "as", "this", "that", "which", "who", "whom",
                        "with", "about", "as", "into", "through", "after", "over"));

        if (stopWords.contains(word.toLowerCase())) {
            return true;
        }
        if (word.length() < 2) {
            return true;
        }
        return false;
    }

    // method to stem a word using the Stemmer algorithm
    String stemWord(String word) {
        Stemmer s = new Stemmer();
        s.addString(word);
        s.stem();
        return s.toString();
    }

    // method to preprocess the text by tokenizing, stemming, and removing stop
    // words
    // and return a list of tokens
    public List<String> preprocessText(String text) {
        List<String> tokens = new ArrayList<>();
        String[] words = text.split("\\W+");
        for (String word : words) {
            word = word.toLowerCase();
            if (isStopWord(word)) {
                continue;
            }
            word = stemWord(word);
            if (word.length() < 2) {
                continue;
            }
            tokens.add(word);
        }
        return tokens;
    }
}
