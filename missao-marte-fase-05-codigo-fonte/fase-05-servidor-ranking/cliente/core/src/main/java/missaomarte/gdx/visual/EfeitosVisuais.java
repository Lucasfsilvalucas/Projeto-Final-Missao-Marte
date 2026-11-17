package missaomarte.gdx.visual;
import missaomarte.gdx.recursos.Imagem;
import java.util.ArrayList;
import java.util.List;
public final class EfeitosVisuais {
    public static final class Efeito {
        public final Imagem imagem;public final float x,y,duracao;
        private float tempo;
        public Efeito(Imagem i,float x,float y,float duracao){imagem=i;this.x=x;this.y=y;this.duracao=duracao;}
        public int quadro(){return Math.min(imagem.quadros-1,(int)(tempo/duracao*imagem.quadros));}
        public boolean terminou(){return tempo>=duracao;}
        private void atualizar(float delta){tempo+=Math.max(0,delta);}
    }
    private final List<Efeito> efeitos=new ArrayList<>();
    public void adicionar(Imagem i,float x,float y,float d){efeitos.add(new Efeito(i,x,y,d));}
    public void atualizar(float delta){for(Efeito e:efeitos)e.atualizar(delta);efeitos.removeIf(Efeito::terminou);}
    public List<Efeito> listar(){return List.copyOf(efeitos);}
}
