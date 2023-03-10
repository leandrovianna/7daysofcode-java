import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ImdbApiClient implements APIClient {
    private static final String IMDB_API_URL = "https://imdb-api.com/API/";

    private final String apiKey;

    public ImdbApiClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getBody() {
        try {
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(new URI(IMDB_API_URL + "Top250Movies/" + this.apiKey))
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

    @Override
    public Class<? extends Content> type() {
        return Movie.class;
    }
}
