package missaomarte.gdx.recursos;
public enum Som {
    MOVER("mover",false), SELECIONAR("selecionar",false), EMBARCAR("embarcar",false),
    ENTREGAR("entregar",false), COLISAO("colisao",false), VITORIA("vitoria",false),
    DERROTA("derrota",false), MUSICA("musica",true);
    public final String caminho; public final boolean musica;
    Som(String nome,boolean musica) { this.caminho="sons/"+nome+".wav";this.musica=musica; }
}
