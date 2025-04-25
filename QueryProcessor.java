import java.util.*;
import java.util.regex.Pattern;

public class QueryProcessor {
    private static final Pattern NON_WORD_CHAR = Pattern.compile("\\W+", Pattern.UNICODE_CHARACTER_CLASS
    public static String[] processQuery(String query) {
        return Arrays.stream(NON_WORD_CHAR.split(query.toLowerCase()))
                   .filter(term -> !term.isEmpty())
                   .toArray(String[]::new);
    }
    public static Map<String, Integer> getTermFrequencies(String[] terms) {
        Map<String, Integer> termFreq = new HashMap<>();
        for (String term : terms) {
            termFreq.put(term, termFreq.getOrDefault(term, 0) + 1);
        }
        return termFreq;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter query (or 'quit'): ");
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("quit")) break;
            
            String[] terms = processQuery(input);
            System.out.println("Processed: " + Arrays.toString(terms));
        }
        scanner.close();
    }
}