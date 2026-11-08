package missaomarte.domain;
public abstract class Passageiro extends EntidadeFixa {
    private final String nome;
    protected Passageiro(String nome, Posicao posicao) {
        super(posicao); this.nome = nome;
    }
    public String getNome() { return nome; }
    public abstract int getPontuacao();
}
