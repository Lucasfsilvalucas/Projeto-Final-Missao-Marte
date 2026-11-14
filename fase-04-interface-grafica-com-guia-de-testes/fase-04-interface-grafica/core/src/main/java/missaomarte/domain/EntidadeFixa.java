package missaomarte.domain;
public abstract class EntidadeFixa implements EntidadeMapa {
    private final Posicao posicao;
    protected EntidadeFixa(Posicao posicao) { this.posicao = posicao; }
    @Override public Posicao getPosicao() { return posicao; }
}
