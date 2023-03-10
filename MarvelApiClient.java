import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

public class MarvelApiClient implements APIClient {
    private static final String MARVEL_API_URL = "https://gateway.marvel.com/v1/public/comics";
    private final String privateKey;
    private final String publicKey;

    public MarvelApiClient(String privateKey, String publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    @Override
    public String getBody() {
        try {
            String timestamp = String.valueOf(Calendar.getInstance().getTimeInMillis());
            String hash = getMD5Hash(timestamp+privateKey+publicKey);
            String url = MARVEL_API_URL + "?ts=" + timestamp
                    + "&apikey=" + this.publicKey
                    + "&hash=" + hash;
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(new URI(url))
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

    private String getMD5Hash(String text) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] result = messageDigest.digest(text.getBytes(StandardCharsets.UTF_8));
            return String.format("%032x", new BigInteger(1, result));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class<? extends Content> type() {
        return ComicBook.class;
    }
}
