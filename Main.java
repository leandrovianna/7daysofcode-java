import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String apiKey = System.getenv("IMDB_API_KEY");
        String json = new ImdbApiClient(apiKey).getBody();

        List<Movie> movies = new ImdbMovieJsonParser(json).parse();

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
