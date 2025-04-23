import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WikiCrawler {


    private static final int MAX_PAGES = 10;
    private static final String WIKIPEDIA_BASE = "https://en.wikipedia.org";
    private static final Pattern WIKI_LINK_PATTERN = Pattern.compile("^/wiki/[^:#]*$");

    public static void main(String[] args) {
        // Seed URLs
        String[] seeds = {
            "https://en.wikipedia.org/wiki/List_of_pharaohs",
            "https://en.wikipedia.org/wiki/Pharaoh"
        };

        // Queue for pages to crawl
        Queue<String> urlQueue = new LinkedList<>();
        // Set for visited pages
        HashSet<String> visited = new HashSet<>();

        for (String seed : seeds) {
            urlQueue.add(seed);
        }

        while (!urlQueue.isEmpty() && visited.size() < MAX_PAGES) {
            String currentUrl = urlQueue.poll();

            if (visited.contains(currentUrl)) {
                continue;
            }

            try {
                Document doc = Jsoup.connect(currentUrl).get();
                System.out.println("Crawled: " + currentUrl);
                visited.add(currentUrl);
                String plainText = doc.text();
                // create new folder called "wiki" if it doesn't exist
                java.nio.file.Files.createDirectories(java.nio.file.Paths.get("wiki"));
                // save the plain text to a file named after the last part of the URL
                java.nio.file.Files.write(java.nio.file.Paths.get("wiki/" + currentUrl.substring(currentUrl.lastIndexOf('/') + 1) + ".txt"), plainText.getBytes());
                // extract links from the page
                Elements links = doc.select("a[href]");
                for (Element link : links) {
                    String href = link.attr("href");

                    Matcher matcher = WIKI_LINK_PATTERN.matcher(href);
                    if (matcher.matches()) {
                        String absoluteUrl = WIKIPEDIA_BASE + href;

                        if (!visited.contains(absoluteUrl) && !urlQueue.contains(absoluteUrl)) {
                            urlQueue.add(absoluteUrl);
                        }
                    }

                    if (visited.size() + urlQueue.size() >= MAX_PAGES) {
                        break;
                    }
                }

            } catch (IOException e) {
                System.err.println("Failed to retrieve: " + currentUrl);
            }
        }

        System.out.println("\nFinished crawling " + visited.size() + " pages.");
    }
}
