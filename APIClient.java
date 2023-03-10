public interface APIClient {
    String getBody();
    Class<? extends Content> type();
}
