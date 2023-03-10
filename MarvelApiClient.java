public class MarvelApiClient implements APIClient {
    public MarvelApiClient(String marvelApiKey) {
    }

    @Override
    public String getBody() {
        return "";
    }

    @Override
    public Class<? extends Content> type() {
        return ComicBook.class;
    }
}
