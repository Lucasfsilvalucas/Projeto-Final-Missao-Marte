package missaomarte.gdx;
/** Converte a grade do jogo (Y positivo para baixo) para coordenadas gráficas (Y positivo para cima). */
public final class MapaGeometria {
    private final float origemX, origemY, celula;
    private final int limite;
    public MapaGeometria(float origemX, float origemY, float celula, int limite) {
        if (celula <= 0 || limite < 1) throw new IllegalArgumentException("Geometria invalida");
        this.origemX=origemX; this.origemY=origemY; this.celula=celula; this.limite=limite;
    }
    public float xCanto(float x) { return origemX + (x+limite)*celula; }
    public float yCanto(float y) { return origemY + (limite-y)*celula; }
    public float xCentro(float x) { return xCanto(x)+celula/2f; }
    public float yCentro(float y) { return yCanto(y)+celula/2f; }
    public float celula() { return celula; }
}
