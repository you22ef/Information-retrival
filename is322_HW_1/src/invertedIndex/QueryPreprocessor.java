package invertedIndex;

import java.util.ArrayList;
import java.util.List;

public class QueryPreprocessor {
    // method to process a query string by tokenizing, stemming, and removing stop
    // words
    // and return a list of tokens
    public List<String> processQuery(String query) {
        TextPreprocessor tp = new TextPreprocessor();
        List<String> tokens = new ArrayList<>();
        tokens = tp.preprocessText(query);
        return tokens;
    }
}
