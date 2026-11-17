package missaomarte.gdx.visual;
public final class RepetidorDeTecla {
    public static final float INTERVALO = .14f;
    private float restante; private boolean ativo;
    public void iniciar() {ativo=true;restante=INTERVALO;}
    public void parar() {ativo=false;restante=0;}
    public boolean atualizar(float delta) {
        if(!ativo)return false;
        restante-=Math.max(0,delta);
        if(restante>0)return false;
        restante=INTERVALO + restante % INTERVALO;
        return true;
    }
}
