package missaomarte.domain;
public enum Dificuldade {
    FACIL(30, 2, 2), MEDIO(20, 4, 4), DIFICIL(45, 8, 8);
    private final int pontosIniciais, asteroides, inimigos;
    Dificuldade(int pontos, int asteroides, int inimigos) {
        this.pontosIniciais = pontos;
        this.asteroides = asteroides; this.inimigos = inimigos;
    }
    public int pontosIniciais() { return pontosIniciais; }
    public int quantidadePassageiros() { return 10; }
    public int quantidadeAsteroides() { return asteroides; }
    public int quantidadeInimigos() { return inimigos; }
    public static Dificuldade deString(String texto) {
        if (texto == null) return MEDIO;
        return switch (texto.trim().toLowerCase()) {
            case "facil", "fácil" -> FACIL;
            case "dificil", "difícil" -> DIFICIL;
            default -> MEDIO;
        };
    }
    @Override public String toString() { return switch(this) {
        case FACIL -> "Fácil"; case MEDIO -> "Médio"; case DIFICIL -> "Difícil";
    }; }
}
