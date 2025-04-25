import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.Arrays;

public class WikipediaQuery {
    private static final Pattern NON_WORD_CHAR = Pattern.compile("\\W+", Pattern.UNICODE_CHARACTER_CLASS);
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.print("Enter your search query (or 'quit' to exit): ");
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("quit")) {
                break;
            }
            
            String[] queryTerms = processQuery(input);
            
            System.out.println("Processed query terms:");
            for (String term : queryTerms) {
                System.out.println("- " + term);
            }
        }
        
        scanner.close();
        System.out.println("Search session ended.");
    }
    
    public static String[] processQuery(String query) {
        String[] terms = NON_WORD_CHAR.split(query.toLowerCase());
        return Arrays.stream(terms)
                   .filter(term -> !term.isEmpty())
                   .toArray(String[]::new);
    }
}