import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String apiKey = System.getenv("IMDB_API_KEY");
        APIClient apiClient = new ImdbApiClient(apiKey);
        String json = apiClient.getBody();

        List<Movie> movies = new ImdbMovieJsonParser(json).parse();

        Collections.sort(movies, Comparator.reverseOrder());
        try {
            Writer writer = new PrintWriter("content.html");
            HTMLGenerator htmlGenerator = new HTMLGenerator(writer);
            htmlGenerator.generate(movies);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
