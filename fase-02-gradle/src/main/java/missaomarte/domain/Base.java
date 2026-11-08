package missaomarte.domain;
public final class Base extends EntidadeFixa {
    public Base() { super(new Posicao(0, 0)); }
    @Override public char getSimbolo() { return 'L'; }
    @Override public String getDescricao() { return "Base de pouso"; }
}
