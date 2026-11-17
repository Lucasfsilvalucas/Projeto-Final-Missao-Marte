package missaomarte.domain;
public final class Astronauta extends Passageiro {
    public Astronauta(String nome, Posicao posicao) { super(nome, posicao); }
    @Override public int getPontuacao() { return 20; }
    @Override public char getSimbolo() { return 'T'; }
    @Override public String getDescricao() { return "Astronauta"; }
}
