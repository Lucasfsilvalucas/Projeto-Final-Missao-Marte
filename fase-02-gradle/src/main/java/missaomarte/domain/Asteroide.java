package missaomarte.domain;
public final class Asteroide extends EntidadeFixa {
    public Asteroide(Posicao posicao) { super(posicao); }
    @Override public char getSimbolo() { return '#'; }
    @Override public String getDescricao() { return "Asteroide"; }
}
