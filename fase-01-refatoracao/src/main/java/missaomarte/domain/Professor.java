package missaomarte.domain;
public final class Professor extends Passageiro {
    public Professor(String nome, Posicao posicao) { super(nome, posicao); }
    @Override public int getPontuacao() { return 10; }
    @Override public char getSimbolo() { return 'P'; }
    @Override public String getDescricao() { return "Professor"; }
}
