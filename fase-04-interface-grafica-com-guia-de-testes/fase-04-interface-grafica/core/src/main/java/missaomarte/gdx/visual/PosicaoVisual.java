package missaomarte.gdx.visual;
public final class PosicaoVisual {
    private float x,y,alvoX,alvoY;
    public PosicaoVisual(float x,float y) {this.x=x;this.y=y;this.alvoX=x;this.alvoY=y;}
    public float x(){return x;} public float y(){return y;}
    public void alvo(float x,float y){alvoX=x;alvoY=y;}
    public void atualizar(float delta,float velocidade) {
        if(delta<=0||velocidade<=0)return;
        float dx=alvoX-x,dy=alvoY-y,dist=(float)Math.hypot(dx,dy),passo=delta*velocidade;
        if(dist<=passo){x=alvoX;y=alvoY;}else{x+=dx*passo/dist;y+=dy*passo/dist;}
    }
}
