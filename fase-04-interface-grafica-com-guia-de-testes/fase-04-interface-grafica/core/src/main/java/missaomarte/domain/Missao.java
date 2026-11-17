package missaomarte.domain;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public final class Missao {
    private final Base base = new Base();
    private final Nave nave;
    private final List<Passageiro> passageiros = new ArrayList<>();
    private final List<Asteroide> asteroides = new ArrayList<>();
    private final List<Inimigo> inimigos = new ArrayList<>();
    private final int totalPassageiros;
    private int entregues = 0;
    public Missao(Nave nave, List<Passageiro> passageiros, List<Asteroide> asteroides, List<Inimigo> inimigos) {
        this.nave = nave; this.passageiros.addAll(passageiros); this.asteroides.addAll(asteroides);
        this.inimigos.addAll(inimigos); this.totalPassageiros = passageiros.size();
    }
    public Nave getNave() { return nave; }
    public Base getBase() { return base; }
    public List<Passageiro> getPassageiros() { return List.copyOf(passageiros); }
    public List<Asteroide> getAsteroides() { return List.copyOf(asteroides); }
    public List<Inimigo> getInimigos() { return List.copyOf(inimigos); }
    public int getEntregues() { return entregues; }
    public int getTotalPassageiros() { return totalPassageiros; }
    public boolean indicadoresConsistentes() {
        return nave.getPassageiros().size() + entregues + passageiros.size() == totalPassageiros;
    }
    public Passageiro passageiroNaPosicao() {
        return passageiros.stream().filter(p -> p.getPosicao().equals(nave.getPosicao())).findFirst().orElse(null);
    }
    public boolean embarcar(Passageiro passageiro) {
        if (passageiro == null || !passageiros.contains(passageiro) || !nave.embarcar(passageiro)) return false;
        passageiros.remove(passageiro);
        return true;
    }
    public int desembarcarNaBase() {
        if (!nave.getPosicao().equals(base.getPosicao())) return 0;
        int numero = nave.desembarcar(); entregues += numero;
        return numero;
    }
    public boolean venceu() { return entregues == totalPassageiros; }
    public boolean moverInimigos(Random random, int limite) {
        boolean colisao = false;
        for (Inimigo inimigo : inimigos) {
            Posicao origem = inimigo.getPosicao();
            inimigo.mover(random, limite);
            if (!nave.ocupa(origem) && nave.ocupa(inimigo.getPosicao())) {
                colisao = true;
            }
        }
        return colisao;
    }
    public boolean verificaColisao() {
        return asteroides.stream().anyMatch(a -> nave.ocupa(a.getPosicao()))
            || inimigos.stream().anyMatch(i -> nave.ocupa(i.getPosicao()));
    }
    public List<EntidadeMapa> entidadesVisiveis() {
        List<EntidadeMapa> todas = new ArrayList<>();
        todas.add(base); todas.addAll(passageiros); todas.addAll(asteroides);
        todas.addAll(inimigos); todas.add(nave);
        return List.copyOf(todas);
    }
}
