import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        APIClient marvelApiClient = new MarvelApiClient(
                System.getenv("MARVEL_API_PRIVATE_KEY"),
                System.getenv("MARVEL_API_PUBLIC_KEY"));
        String marvelJson = marvelApiClient.getBody();
        List<ComicBook> comicBooks = new MarvelComicBookJsonParser(marvelJson).parse();

        APIClient imdbApiClient = new ImdbApiClient(System.getenv("IMDB_API_KEY"));
        String imdbJson = imdbApiClient.getBody();
        List<Movie> movies = new ImdbMovieJsonParser(imdbJson).parse();

        List<? extends Content> mixed = Stream.of(comicBooks, movies)
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(Content::year))
                .collect(Collectors.toList());

        try {
            Writer writer = new PrintWriter("content.html");
            HTMLGenerator htmlGenerator = new HTMLGenerator(writer);
            htmlGenerator.generate(mixed);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
