import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImdbMovieJsonParser implements JsonParser {
    private final String json;

    public ImdbMovieJsonParser(String json) {
        this.json = json;
    }

    private List<String> extractMovies(String json) {
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

    private String extractJsonAttribute(String json, String selected) {
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

    public List<Movie> parse() {
        List<Movie> movies = new ArrayList<>();
        List<String> moviesJson = extractMovies(this.json);

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
}
