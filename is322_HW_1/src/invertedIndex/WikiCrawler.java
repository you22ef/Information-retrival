package invertedIndex;

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

    public void crawling() {
        // Seed URLs
        String[] seeds = {
                "https://en.wikipedia.org/wiki/List_of_pharaohs",
                "https://en.wikipedia.org/wiki/Pharaoh"
        };

        // Queue for pages to crawl
        Queue<String> urlQueue = new LinkedList<>();
        // Set for visited pages
        HashSet<String> visited = new HashSet<>();
        // Add seed URLs to the queue
        for (String seed : seeds) {
            urlQueue.add(seed);
        }
        // Start crawling
        while (!urlQueue.isEmpty() && visited.size() < MAX_PAGES) {
            String currentUrl = urlQueue.poll(); // Get the next URL from the queue
            // Check if the URL has already been visited
            if (visited.contains(currentUrl)) {
                continue;
            }

            try {
                Document doc = Jsoup.connect(currentUrl).get(); // Fetch the page
                System.out.println("Crawled: " + currentUrl);
                visited.add(currentUrl);

                // Select the main content area
                Element contentDiv = doc.selectFirst("#mw-content-text .mw-parser-output");
                StringBuilder plainTextBuilder = new StringBuilder();

                if (contentDiv != null) {
                    // Select only paragraph elements within the main content
                    Elements paragraphs = contentDiv.select("p");
                    for (Element p : paragraphs) {
                        plainTextBuilder.append(p.text()).append("\n"); // Append text of each paragraph
                    }
                } else {
                        // Fallback to doc.text() if the specific content div is not found
                        plainTextBuilder.append(doc.text());
                }
                String plainText = plainTextBuilder.toString();

                // save the plain text to a file named after the last part of the URL
                java.nio.file.Files.write(
                        java.nio.file.Paths.get("is322_HW_1/src/invertedIndex/Path/"
                                + currentUrl.substring(currentUrl.lastIndexOf('/') + 1) + ".txt"),
                        plainText.getBytes());
                // extract links from the page
                Elements links = doc.select("a[href]");
                for (Element link : links) {
                    String href = link.attr("href");
                    // Check if the link is a Wikipedia link
                    Matcher matcher = WIKI_LINK_PATTERN.matcher(href);
                    if (matcher.matches()) {
                        String absoluteUrl = WIKIPEDIA_BASE + href;
                        // Check if the URL is not already visited or in the queue
                        if (!visited.contains(absoluteUrl) && !urlQueue.contains(absoluteUrl)) {
                            urlQueue.add(absoluteUrl);
                        }
                    }
                    // Limit the number of pages to crawl to 10
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
