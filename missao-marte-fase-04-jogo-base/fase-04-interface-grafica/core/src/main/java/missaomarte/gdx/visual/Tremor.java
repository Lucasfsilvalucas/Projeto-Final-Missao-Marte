package missaomarte.gdx.visual;
public final class Tremor {
    private float restante,total,intensidade;
    public void iniciar(float duracao,float intensidade) {this.restante=Math.max(0,duracao);this.total=this.restante;this.intensidade=intensidade;}
    public void atualizar(float delta){restante=Math.max(0,restante-Math.max(0,delta));}
    public float amplitude(){return total==0?0:intensidade*restante/total;}
}
