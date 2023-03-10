import java.util.ArrayList;
import java.util.List;

public class MarvelComicBookJsonParser implements JsonParser {
    private final String json;

    public MarvelComicBookJsonParser(String json) {
        this.json = json;
    }

    @Override
    public List<ComicBook> parse() {
        List<ComicBook> comicBooks = new ArrayList<>();
        // TODO: Parse json
        return comicBooks;
    }
}
