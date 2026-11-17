package missaomarte.gdx.hud;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import missaomarte.application.Partida;
import missaomarte.domain.Missao;
import missaomarte.gdx.recursos.*;
public final class HudRenderer {
    private final Recursos recursos;
    public HudRenderer(Recursos recursos){this.recursos=recursos;}
    public void desenhar(ShapeRenderer formas, SpriteBatch batch, BitmapFont fonte, Partida partida,String mensagem,boolean mudo){
        Missao m=partida.getMissao();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
        formas.begin(ShapeRenderer.ShapeType.Filled);
        formas.setColor(new Color(.025f,.06f,.13f,.82f));
        formas.rect(18,550,764,42);formas.rect(445,82,342,455);formas.rect(18,38,764,39);
        formas.setColor(new Color(.09f,.48f,.60f,.95f));
        formas.rect(480,252,265,14);
        formas.setColor(Color.GREEN);
        if(m.getTotalPassageiros()>0)formas.rect(480,252,265f*m.getEntregues()/m.getTotalPassageiros(),14);
        formas.end();Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
        for(int i=0;i<3;i++)batch.draw(recursos.quadro(i<m.getNave().getVidas()?Imagem.VIDA_CHEIA:Imagem.VIDA_VAZIA),36+i*31,561,23,23);
        fonte.draw(batch,"Pontos: "+partida.getPontos(),150,577);
        fonte.draw(batch,"Tempo: "+partida.getTempoSegundos()+"s",308,577);
        fonte.draw(batch,mudo?"Som: desligado (M)":"Som: ligado (M)",490,577);
        fonte.draw(batch,"Piloto: "+partida.getPiloto(),475,512);
        fonte.draw(batch,"Dificuldade: "+partida.getDificuldade(),475,481);
        fonte.draw(batch,"Vagas da nave",475,447);
        for(int i=0;i<m.getNave().getCapacidade();i++)batch.draw(recursos.quadro(i<m.getNave().getPassageiros().size()?Imagem.VAGA_OCUPADA:Imagem.VAGA_LIVRE),475+i*35,405,28,28);
        fonte.draw(batch,"Entregues: "+m.getEntregues()+"/"+m.getTotalPassageiros(),475,287);
        fonte.draw(batch,"No mapa: "+m.getPassageiros().size(),475,227);
        fonte.draw(batch,"Movimentos: "+partida.getMovimentos(),475,197);
        fonte.draw(batch,mensagem,475,161,286,com.badlogic.gdx.utils.Align.left,true);
        fonte.draw(batch,"WASD/Setas: mover  |  E: embarcar  |  M: som  |  Esc: sair",30,62);
        batch.end();
    }
}
