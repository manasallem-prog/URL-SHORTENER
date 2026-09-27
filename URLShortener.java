import java.util.*;

public class URLShortener {

    // shortCode -> original URL
    private final HashMap<String, String> urlMap = new HashMap<>();

    // original URL -> shortCode
    private final HashMap<String, String> reverseMap = new HashMap<>();

    // Stores recently created short URLs
    private final Queue<String> recentUrls = new LinkedList<>();

    // Used for undo/delete
    private final Stack<String> history = new Stack<>();

    // Generates unique numeric IDs
    private long id = 1000;

    private static final String BASE62 =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // Convert number to Base62
    private String base62Encode(long number) {

        if (number == 0) {
            return "0";
        }

        StringBuilder result = new StringBuilder();

        while (number > 0) {
            int remainder = (int) (number % 62);
            result.append(BASE62.charAt(remainder));
            number = number / 62;
        }

        return result.reverse().toString();
    }

    // Shorten URL
    public String shortenURL(String originalURL) {

        // If URL already exists
        if (reverseMap.containsKey(originalURL)) {
            return reverseMap.get(originalURL);
        }

        // Generate unique short code
        String shortCode = base62Encode(id++);

        // Store mappings
        urlMap.put(shortCode, originalURL);
        reverseMap.put(originalURL, shortCode);

        // Maintain recent URLs
        recentUrls.offer(shortCode);

        // Maintain history
        history.push(shortCode);

        return shortCode;
    }

    // Retrieve original URL
    public String getOriginalURL(String shortCode) {
        return urlMap.get(shortCode);
    }

    // Display all URLs
    public void displayAll() {

        if (urlMap.isEmpty()) {
            System.out.println("No URLs available.");
            return;
        }

        System.out.println("\n----- URL Mappings -----");

        for (Map.Entry<String, String> entry : urlMap.entrySet()) {

            System.out.println(
                    "Short URL : http://short.ly/" + entry.getKey()
            );

            System.out.println(
                    "Original  : " + entry.getValue()
            );

            System.out.println("-------------------------");
        }
    }

    // Show recently created URLs
    public void showRecentURLs() {

        if (recentUrls.isEmpty()) {
            System.out.println("No recent URLs.");
            return;
        }

        System.out.println("\nRecent URLs:");

        for (String code : recentUrls) {
            System.out.println(
                    "http://short.ly/" + code
            );
        }
    }

    // Undo last URL creation
    public void undoLast() {

        if (history.isEmpty()) {
            System.out.println("Nothing to undo.");
            return;
        }

        String shortCode = history.pop();

        String originalURL = urlMap.remove(shortCode);

        if (originalURL != null) {
            reverseMap.remove(originalURL);
            recentUrls.remove(shortCode);

            System.out.println(
                    "Deleted: http://short.ly/" + shortCode
            );
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        URLShortener shortener = new URLShortener();

        while (true) {

            System.out.println("\n==============================");
            System.out.println("       URL SHORTENER");
            System.out.println("==============================");

            System.out.println("1. Shorten URL");
            System.out.println("2. Open Short URL");
            System.out.println("3. Display All URLs");
            System.out.println("4. Show Recent URLs");
            System.out.println("5. Undo Last URL");
            System.out.println("6. Exit");

            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:

                    System.out.print("Enter original URL: ");

                    String originalURL = scanner.nextLine();

                    String shortCode =
                            shortener.shortenURL(originalURL);

                    System.out.println(
                            "\nShort URL: http://short.ly/" + shortCode
                    );

                    break;

                case 2:

                    System.out.print("Enter short code: ");

                    String code = scanner.nextLine();

                    String original =
                            shortener.getOriginalURL(code);

                    if (original != null) {
                        System.out.println(
                                "Original URL: " + original
                        );
                    } else {
                        System.out.println(
                                "Short URL not found."
                        );
                    }

                    break;

                case 3:

                    shortener.displayAll();

                    break;

                case 4:

                    shortener.showRecentURLs();

                    break;

                case 5:

                    shortener.undoLast();

                    break;

                case 6:

                    System.out.println("Thank you!");

                    scanner.close();

                    return;

                default:

                    System.out.println(
                            "Invalid choice!"
                    );
            }
        }
    }
}

