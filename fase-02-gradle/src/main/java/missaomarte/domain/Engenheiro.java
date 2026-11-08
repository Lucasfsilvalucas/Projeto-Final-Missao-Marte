package missaomarte.domain;
public final class Engenheiro extends Passageiro {
    public Engenheiro(String nome, Posicao posicao) { super(nome, posicao); }
    @Override public int getPontuacao() { return 15; }
    @Override public char getSimbolo() { return 'E'; }
    @Override public String getDescricao() { return "Engenheiro"; }
}
