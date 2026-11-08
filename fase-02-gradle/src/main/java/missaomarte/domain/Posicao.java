package missaomarte.domain;
public record Posicao(int x, int y) {
    public Posicao deslocar(int dx, int dy) { return new Posicao(x + dx, y + dy); }
}
