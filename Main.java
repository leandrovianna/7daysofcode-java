import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final String IMDB_API_URL = "https://imdb-api.com/API/";

    private static String fetchData() {
        try {
            String api_key = System.getenv("IMDB_API_KEY");
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(new URI(IMDB_API_URL + "Top250Movies/" + api_key))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();

        } catch (URISyntaxException | InterruptedException | IOException e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }

    private static List<String> extractMovies(String json) {
        Pattern moviesListPattern = Pattern.compile("\\[.+]");
        Matcher matcher = moviesListPattern.matcher(json);
        if (matcher.find()) {
            String movies = json.substring(matcher.start()+1, matcher.end()-1);
            return Arrays.stream(movies.split("\\{"))
                    .map(String::strip)
                    .filter(s -> !s.isEmpty())
                    .toList();
        }
        return new ArrayList<>();
    }

    private static String extractJsonAttribute(String json, String selected) {
        String[] attributes = json.substring(1, json.length()-1).split("\",");
        for (String attribute : attributes) {
            int index = attribute.indexOf(":");
            String name = attribute.substring(0, index).replace("\"", "");
            String value = attribute.substring(index+1).replaceAll("[\"}]", "");

            if (name.equals(selected)) {
                return value;
            }
        }
        return "";
    }

    private static List<String> parseTitles(List<String> movies) {
        List<String> titles = new ArrayList<>();

        for (String movie : movies) {
            titles.add(extractJsonAttribute(movie, "title"));
        }

        return titles;
    }

    private static List<String> parseUrlImages(List<String> movies) {
        List<String> urlImages = new ArrayList<>();
        for (String movie : movies) {
            urlImages.add(extractJsonAttribute(movie, "image"));
        }
        return urlImages;
    }

    private static List<String> parseYears(List<String> movies) {
        List<String> years = new ArrayList<>();
        for (String movie : movies) {
            years.add(extractJsonAttribute(movie, "year"));
        }
        return years;
    }

    private static List<String> parseImdbRatings(List<String> movies) {
        List<String> imdbRatings = new ArrayList<>();
        for (String movie : movies) {
            imdbRatings.add(extractJsonAttribute(movie, "imDbRating"));
        }
        return imdbRatings;
    }

    public static void main(String[] args) {
        String json = fetchData();
        List<String> movies = extractMovies(json);

        List<String> titles = parseTitles(movies);
        titles.forEach(System.out::println);

        List<String> urlImages = parseUrlImages(movies);
        urlImages.forEach(System.out::println);

        List<String> years = parseYears(movies);
        years.forEach(System.out::println);

        List<String> imdbRatings = parseImdbRatings(movies);
        imdbRatings.forEach(System.out::println);
    }
}
