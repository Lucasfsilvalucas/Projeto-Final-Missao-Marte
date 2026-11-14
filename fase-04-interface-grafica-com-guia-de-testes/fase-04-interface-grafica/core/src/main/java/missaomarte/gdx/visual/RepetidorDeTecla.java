package missaomarte.gdx.visual;
public final class RepetidorDeTecla {
    private float restante; private boolean ativo;
    public void iniciar() {ativo=true;restante=.28f;}
    public void parar() {ativo=false;restante=0;}
    public boolean atualizar(float delta) {
        if(!ativo)return false;
        restante-=Math.max(0,delta);
        if(restante>0)return false;
        restante=Math.max(0,restante)+.14f;
        return true;
    }
}
