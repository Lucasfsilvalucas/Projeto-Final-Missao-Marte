package missaomarte.gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import missaomarte.application.*;
import missaomarte.domain.Posicao;
import missaomarte.gdx.recursos.*;
import missaomarte.gdx.visual.*;
import missaomarte.gdx.hud.HudRenderer;

final class GameScreen extends TelaBase {
    private final Partida partida;
    private final MundoRenderer mundo;
    private final HudRenderer hud;
    private final EfeitosVisuais efeitos=new EfeitosVisuais();
    private final MensagensFlutuantes textos=new MensagensFlutuantes();
    private final Tremor tremor=new Tremor();
    private final RepetidorDeTecla repetidor=new RepetidorDeTecla();
    private String mensagem="Explore Marte e resgate os passageiros.";
    private boolean rankingGravado,entrou;
    private int teclaSegurada=-1, orientacao;
    private Comando comandoSegurado;
    private float piscar,flash;
    private final com.badlogic.gdx.math.Matrix4 tremorMatriz = new com.badlogic.gdx.math.Matrix4();
    GameScreen(MissaoMarteGame game,Partida partida) { super(game);this.partida=partida;
        mundo=new MundoRenderer(game.recursos); hud=new HudRenderer(game.recursos);
    }
    private void executar(Comando comando) {
        if(comando==Comando.INVALIDO || partida.getEstado()!=EstadoPartida.EM_ANDAMENTO)return;
        if(comando==Comando.SAIR){partida.executar(comando);game.menu();return;}
        int antes=partida.getPontos();
        ResultadoTurno resultado=partida.executar(comando);
        mensagem=resultado.mensagem();
        Posicao p=partida.getMissao().getNave().getPosicao();
        if(comando==Comando.CIMA)orientacao=0;
        if(comando==Comando.DIREITA)orientacao=-90;
        if(comando==Comando.BAIXO)orientacao=180;
        if(comando==Comando.ESQUERDA)orientacao=90;
        for(EventoTurno e:resultado.eventos()){
            switch(e){
                case MOVIMENTO -> game.sons.tocar(Som.MOVER);
                case EMBARQUE -> {game.sons.tocar(Som.EMBARCAR);efeitos.adicionar(Imagem.BRILHO,p.x(),p.y(),.5f);}
                case ENTREGA -> {game.sons.tocar(Som.ENTREGAR);efeitos.adicionar(Imagem.BRILHO,0,0,.5f);textos.adicionar("Entregue!",0,0);}
                case COLISAO -> {game.sons.tocar(Som.COLISAO);efeitos.adicionar(Imagem.EXPLOSAO,p.x(),p.y(),.55f);tremor.iniciar(.35f,7f);piscar=1f;flash=.28f;}
                case VITORIA -> {game.sons.pausaFinal();game.sons.tocar(Som.VITORIA);}
                case DERROTA -> {game.sons.pausaFinal();game.sons.tocar(Som.DERROTA);}
            }
        }
        int diferenca=partida.getPontos()-antes;
        if(Math.abs(diferenca)>1)textos.adicionar((diferenca>0?"+":"")+diferenca,p.x(),p.y());
        if(resultado.estado()==EstadoPartida.VITORIA && !rankingGravado){rankingGravado=true;entrou=game.ranking.registrarVitoria(partida);}
        if(resultado.estado()!=EstadoPartida.EM_ANDAMENTO)pararRepeticao();
    }
    private void pararRepeticao(){comandoSegurado=null;teclaSegurada=-1;repetidor.parar();}
    @Override public void show(){super.show();instalar(new InputAdapter(){
        @Override public boolean keyDown(int tecla){
            if(partida.getEstado()!=EstadoPartida.EM_ANDAMENTO){
                if(tecla==Input.Keys.ENTER){if(partida.getEstado()==EstadoPartida.VITORIA)game.setScreen(new RankingScreen(game,partida,entrou));else game.menu();return true;}
                if(tecla==Input.Keys.ESCAPE){game.menu();return true;}
                return false;
            }
            Comando c=TecladoParaComando.converter(tecla);
            if(c==Comando.INVALIDO)return false;
            if(c==Comando.CIMA||c==Comando.BAIXO||c==Comando.ESQUERDA||c==Comando.DIREITA){
                if(tecla==teclaSegurada)return true;
                teclaSegurada=tecla;comandoSegurado=c;repetidor.iniciar();
            }
            executar(c);return true;
        }
        @Override public boolean keyUp(int tecla){if(teclaSegurada==tecla)pararRepeticao();return false;}
    });}
    @Override public void hide(){pararRepeticao();super.hide();}
    @Override protected void desenhar(float delta){
        if(partida.getEstado()==EstadoPartida.EM_ANDAMENTO && comandoSegurado!=null && repetidor.atualizar(delta))executar(comandoSegurado);
        efeitos.atualizar(delta);textos.atualizar(delta);tremor.atualizar(delta);
        piscar=Math.max(0,piscar-delta);flash=Math.max(0,flash-delta);
        // A câmera não é movida: somente o mapa recebe um pequeno deslocamento visual.
        float amp=tremor.amplitude();
        game.batch.setTransformMatrix(tremorMatriz.setToTranslation((float)Math.sin(com.badlogic.gdx.utils.TimeUtils.millis()*.08)*amp,(float)Math.cos(com.badlogic.gdx.utils.TimeUtils.millis()*.11)*amp,0));
        game.batch.begin();
        mundo.desenhar(game.batch,partida.getMissao(),delta,orientacao,piscar,efeitos,textos);
        for(var t:textos.listar()){
            game.font.setColor(1f,.94f,.4f,t.opacidade());
            game.font.draw(game.batch,t.conteudo,mundo.geometria().xCanto(t.x),mundo.geometria().yCanto(t.yVisual())+35);
        }
        game.font.setColor(Color.WHITE);game.batch.end();
        game.batch.setTransformMatrix(tremorMatriz.idt());
        if(flash>0){
            com.badlogic.gdx.Gdx.gl.glEnable(GL20.GL_BLEND);
            com.badlogic.gdx.Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
            game.shapes.begin(ShapeRenderer.ShapeType.Filled);
            game.shapes.setColor(1f,.1f,.1f,flash*.9f);game.shapes.rect(40,88,388,388);
            game.shapes.end();com.badlogic.gdx.Gdx.gl.glDisable(GL20.GL_BLEND);
        }
        hud.desenhar(game.shapes,game.batch,game.font,partida,mensagem,game.sons.mudo());
        if(partida.getEstado()!=EstadoPartida.EM_ANDAMENTO){
            centralizar(partida.getEstado()==EstadoPartida.VITORIA?"MISSÃO CUMPRIDA! Enter: continuar":"GAME OVER - Enter: continuar",538);
        }
    }
}
