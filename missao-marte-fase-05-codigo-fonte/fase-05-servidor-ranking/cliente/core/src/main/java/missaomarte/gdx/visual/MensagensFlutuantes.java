package missaomarte.gdx.visual;
import java.util.ArrayList;
import java.util.List;
public final class MensagensFlutuantes {
    public static final class Texto {
        public final String conteudo;public final float x,y;private float tempo;
        Texto(String s,float x,float y){conteudo=s;this.x=x;this.y=y;}
        public float yVisual(){return y-tempo*.75f;}
        public float opacidade(){return Math.max(0,1-tempo/1.2f);}
        void atualizar(float delta){tempo+=Math.max(0,delta);}
    }
    private final List<Texto> textos=new ArrayList<>();
    public void adicionar(String s,float x,float y){textos.add(new Texto(s,x,y));}
    public void atualizar(float delta){for(Texto t:textos)t.atualizar(delta);textos.removeIf(t->t.opacidade()<=0);}
    public List<Texto> listar(){return List.copyOf(textos);}
}
