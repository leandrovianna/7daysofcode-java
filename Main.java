import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {

    private static final String IMDB_API_URL = "https://imdb-api.com/API/";
    public static void main(String[] args) {
        try {
            String api_key = System.getenv("IMDB_API_KEY");
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(new URI(IMDB_API_URL + "Top250Movies/"+api_key))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.body());

        } catch (URISyntaxException | InterruptedException | IOException e) {
            System.out.println("ERROR: "+e.getMessage());
            e.printStackTrace();
        }
    }
}
