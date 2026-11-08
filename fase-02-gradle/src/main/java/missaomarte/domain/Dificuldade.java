package missaomarte.domain;
public enum Dificuldade {
    FACIL(30, 4, 1, 1), MEDIO(20, 5, 2, 2), DIFICIL(45, 5, 3, 3);
    private final int pontosIniciais, passageiros, asteroides, inimigos;
    Dificuldade(int pontos, int passageiros, int asteroides, int inimigos) {
        this.pontosIniciais = pontos; this.passageiros = passageiros;
        this.asteroides = asteroides; this.inimigos = inimigos;
    }
    public int pontosIniciais() { return pontosIniciais; }
    public int quantidadePassageiros() { return passageiros; }
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
