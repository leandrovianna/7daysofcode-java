public record ComicBook(String title, String urlImage, double rating, int year) implements Content, Comparable<Content> {
    @Override
    public String type() {
        return "Comic Book";
    }

    @Override
    public int compareTo(Content other) {
        return this.rating() < other.rating() ? -1 : 1;
    }
}
