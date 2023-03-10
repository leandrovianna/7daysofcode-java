import java.util.ArrayList;
import java.util.List;

public class MarvelComicBookParser implements JsonParser {
    public MarvelComicBookParser(String json) {
    }

    @Override
    public List<ComicBook> parse() {
        return new ArrayList<>();
    }
}
