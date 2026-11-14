package missaomarte.gdx.visual;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import missaomarte.domain.*;
import missaomarte.gdx.MapaGeometria;
import missaomarte.gdx.recursos.*;
import java.util.List;
public final class MundoRenderer {
    private final Recursos recursos;
    private final PosicoesVisuais posicoes=new PosicoesVisuais();
    private final MapaGeometria geo=new MapaGeometria(42,90,35,5);
    private float tempo;
    public MundoRenderer(Recursos recursos) { this.recursos=recursos; }
    public void desenhar(SpriteBatch batch,Missao missao,float delta, int orientacao,float piscar, EfeitosVisuais efeitos,MensagensFlutuantes textos) {
        tempo+=delta;
        List<EntidadeMapa> objetos=missao.entidadesVisiveis();
        posicoes.sincronizar(objetos,delta);
        for(int y=-5;y<=5;y++)for(int x=-5;x<=5;x++)batch.draw(recursos.quadro(Imagem.TERRENO),geo.xCanto(x),geo.yCanto(y),geo.celula()-1,geo.celula()-1);
        for(EntidadeMapa e:objetos){
            if(e instanceof Nave)continue;
            PosicaoVisual v=posicoes.obter(e);
            Imagem i=SimboloParaImagem.de(e.getSimbolo());
            TextureRegion quadro= i==Imagem.BASE?recursos.animacao(i,.25f).getKeyFrame(tempo,true):
                i==Imagem.INIMIGO?recursos.animacao(i,.3f).getKeyFrame(tempo,true):recursos.quadro(i);
            batch.draw(quadro,geo.xCanto(v.x()),geo.yCanto(v.y()),geo.celula(),geo.celula());
        }
        Nave nave=missao.getNave(); PosicaoVisual v=posicoes.obter(nave);
        if(v!=null && !(piscar>0 && ((int)(piscar*12))%2==0)){
            float x=geo.xCanto(v.x()),y=geo.yCanto(v.y()),c=geo.celula();
            TextureRegion chama=recursos.animacao(Imagem.PROPULSOR,.09f).getKeyFrame(tempo,true);
            batch.draw(chama,x,y-8,c,c);
            batch.draw(recursos.quadro(Imagem.NAVE),x,y,c/2,c/2,c,c,1,1,orientacao);
        }
        for(EfeitosVisuais.Efeito e:efeitos.listar()){
            TextureRegion q=recursos.quadro(e.imagem,e.quadro());
            batch.draw(q,geo.xCanto(e.x),geo.yCanto(e.y),geo.celula(),geo.celula());
        }
    }
    public MapaGeometria geometria(){return geo;}
}
