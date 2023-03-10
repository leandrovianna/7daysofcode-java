/*
 Escolha por um objeto imutável. Dados são passados ao construtor e só podem ser consultados com getters.
 Os filmes vão ser obtidos da API já com os dados completos e não é necessário fazer mudanças posteriores nos objetos.
 Usando record, pois facilita a escrita dessa classe.
 */

public record Movie (String title, String urlImage, double rating, int year) implements Content, Comparable<Content> {
    @Override
    public int compareTo(Content other) {
        return this.rating() < other.rating() ? -1 : 1;
    }

    @Override
    public String type() {
        return "Movie";
    }
}