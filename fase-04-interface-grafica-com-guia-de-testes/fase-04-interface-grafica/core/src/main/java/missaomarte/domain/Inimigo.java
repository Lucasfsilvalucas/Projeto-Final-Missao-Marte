package missaomarte.domain;
import java.util.Random;
public final class Inimigo implements EntidadeMapa {
    private Posicao posicao;
    public Inimigo(Posicao posicao) { this.posicao = posicao; }
    @Override public Posicao getPosicao() { return posicao; }
    public void mover(Random random, int limite) {
        int d = random.nextInt(4);
        Posicao destino = switch (d) {
            case 0 -> posicao.deslocar(1, 0);
            case 1 -> posicao.deslocar(-1, 0);
            case 2 -> posicao.deslocar(0, 1);
            default -> posicao.deslocar(0, -1);
        };
        if (Math.abs(destino.x()) <= limite && Math.abs(destino.y()) <= limite) posicao = destino;
    }
    @Override public char getSimbolo() { return 'X'; }
    @Override public String getDescricao() { return "Inimigo"; }
}
