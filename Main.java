import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
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

    private static List<Movie> parseMovies(List<String> moviesJson) {
        List<Movie> movies = new ArrayList<>();
        for (String movieJson : moviesJson) {
            String title = extractJsonAttribute(movieJson, "title");
            String urlImage = extractJsonAttribute(movieJson, "image");
            double rating = Double.parseDouble(extractJsonAttribute(movieJson, "imDbRating"));
            int year = Integer.parseInt(extractJsonAttribute(movieJson, "year"));

            Movie movie = new Movie(title, urlImage, rating, year);
            movies.add(movie);
        }
        return movies;
    }

    public static void main(String[] args) {
        String json = fetchData();
        List<String> moviesJson = extractMovies(json);

        List<Movie> movies = parseMovies(moviesJson);

        try {
            Writer writer = new PrintWriter(System.out);
            HTMLGenerator htmlGenerator = new HTMLGenerator(writer);
            htmlGenerator.generate(movies);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
