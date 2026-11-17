package missaomarte.domain;
import java.util.ArrayList;
import java.util.List;
public final class Nave implements EntidadeMapa {
    private final String id;
    private final int capacidade;
    private Posicao posicao = new Posicao(0, 0);
    private int direcaoX, direcaoY = -1;
    private int vidas = 3;
    private final List<Passageiro> aBordo = new ArrayList<>();
    public Nave(String id, int capacidade) {
        if (capacidade < 1) throw new IllegalArgumentException("Capacidade invalida");
        this.id = id; this.capacidade = capacidade;
    }
    public String getId() { return id; }
    @Override public Posicao getPosicao() { return posicao; }
    public Posicao getCauda() { return posicao.deslocar(-direcaoX, -direcaoY); }
    public boolean ocupa(Posicao lugar) { return posicao.equals(lugar) || getCauda().equals(lugar); }
    public int getVidas() { return vidas; }
    public int getCapacidade() { return capacidade; }
    public List<Passageiro> getPassageiros() { return List.copyOf(aBordo); }
    public boolean mover(int dx, int dy, int limite) {
        Posicao destino = posicao.deslocar(dx, dy);
        if (Math.abs(destino.x()) > limite || Math.abs(destino.y()) > limite) return false;
        if (dx == 0 && dy == 0) return true;
        Posicao cauda = destino.deslocar(-dx, -dy);
        if (Math.abs(cauda.x()) > limite || Math.abs(cauda.y()) > limite) return false;
        posicao = destino;
        direcaoX = dx;
        direcaoY = dy;
        return true;
    }
    public boolean embarcar(Passageiro passageiro) {
        if (aBordo.size() >= capacidade) return false;
        aBordo.add(passageiro);
        return true;
    }
    public int desembarcar() { int quantidade = aBordo.size(); aBordo.clear(); return quantidade; }
    public void perderVida() { if (vidas > 0) vidas--; }
    @Override public char getSimbolo() { return '@'; }
    @Override public String getDescricao() { return "Nave"; }
}
