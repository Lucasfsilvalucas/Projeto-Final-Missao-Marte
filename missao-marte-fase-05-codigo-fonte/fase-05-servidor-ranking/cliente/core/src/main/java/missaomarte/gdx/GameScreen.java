package missaomarte.gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import missaomarte.application.*;
import missaomarte.domain.Posicao;
import missaomarte.gdx.recursos.*;
import missaomarte.gdx.visual.*;
import missaomarte.gdx.hud.HudRenderer;

import missaomarte.application.servidor.PartidaConcluida;
import missaomarte.application.servidor.Sessao;

final class GameScreen extends TelaBase {
    private final Sessao sessao;
    private PartidaConcluida resultadoOnline;
    private EstadoEnvio envio = EstadoEnvio.INATIVO;
    private String avisoEnvio = "";
    private enum EstadoEnvio { INATIVO, ENVIANDO, CONFIRMADO, FALHOU }
    private final Partida partida;
    private final MundoRenderer mundo;
    private final HudRenderer hud;
    private final EfeitosVisuais efeitos=new EfeitosVisuais();
    private final MensagensFlutuantes textos=new MensagensFlutuantes();
    private final Tremor tremor=new Tremor();
    private final RepetidorDeTecla repetidor=new RepetidorDeTecla();
    private String mensagem="Explore Marte e resgate os passageiros.";
    private boolean resultadoRegistrado,entrou;
    private int teclaSegurada=-1, orientacao;
    private Comando comandoSegurado;
    private float piscar, flash;
    private int quadroChama = 1;
    private final Rectangle areaMapa = new Rectangle(MundoRenderer.X, MundoRenderer.Y,
            MundoRenderer.TAMANHO, MundoRenderer.TAMANHO);
    private final Rectangle recorteMapa = new Rectangle();
    private final com.badlogic.gdx.math.Matrix4 tremorMatriz = new com.badlogic.gdx.math.Matrix4();
    GameScreen(MissaoMarteGame game,Partida partida) { super(game);this.partida=partida; this.sessao=game.sessao;
        mundo=new MundoRenderer(game.recursos, partida.getLimite()); hud=new HudRenderer(game.recursos);
    }
    private void executar(Comando comando) {
        if(comando==Comando.INVALIDO || partida.getEstado()!=EstadoPartida.EM_ANDAMENTO)return;
        if(comando==Comando.SAIR){partida.executar(comando);game.menu();return;}
        int antes=partida.getPontos();
        ResultadoTurno resultado = partida.executar(comando);
        if (resultado.eventos().contains(EventoTurno.MOVIMENTO)) {
            orientacao = switch (comando) {
                case CIMA -> 0;
                case DIREITA -> -90;
                case BAIXO -> 180;
                case ESQUERDA -> 90;
                default -> orientacao;
            };
            quadroChama = 1 - quadroChama;
        }
        processarResultado(resultado, antes);
    }
    private void processarResultado(ResultadoTurno resultado, int pontosAntes) {
        mensagem=resultado.mensagem();
        Posicao p=partida.getMissao().getNave().getPosicao();
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
        int diferenca=partida.getPontos()-pontosAntes;
        if(Math.abs(diferenca)>1)textos.adicionar((diferenca>0?"+":"")+diferenca,p.x(),p.y());
        boolean concluida = resultado.estado()==EstadoPartida.VITORIA || resultado.estado()==EstadoPartida.DERROTA;
        if (concluida && !resultadoRegistrado) {
            resultadoRegistrado = true;
            if (sessao != null) {
                resultadoOnline = PartidaConcluida.de(partida);
                enviarResultado();
            } else if (resultado.estado()==EstadoPartida.VITORIA) {
                entrou=game.ranking.registrarVitoria(partida);
            }
        }
        if(resultado.estado()!=EstadoPartida.EM_ANDAMENTO)pararRepeticao();
    }
    private void enviarResultado() {
        envio = EstadoEnvio.ENVIANDO;
        avisoEnvio = "Enviando resultado...";
        game.servidor.enviar(sessao, resultadoOnline, resposta -> {
            if (game.getScreen() != this) return;
            if (resposta.naoAutorizada()) {
                game.expirarSessao();
            } else if (resposta.sucesso()) {
                envio = EstadoEnvio.CONFIRMADO;
                avisoEnvio = "Resultado enviado. Enter: continuar";
            } else {
                envio = EstadoEnvio.FALHOU;
                avisoEnvio = resposta.erro();
            }
        });
    }
    private void pararRepeticao(){comandoSegurado=null;teclaSegurada=-1;repetidor.parar();}
    @Override public void show(){super.show();instalar(new InputAdapter(){
        @Override public boolean keyDown(int tecla){
            if(partida.getEstado()!=EstadoPartida.EM_ANDAMENTO){
                if (envio == EstadoEnvio.ENVIANDO) return true;
                if (tecla == Input.Keys.R && envio == EstadoEnvio.FALHOU) {
                    enviarResultado();
                    return true;
                }
                if (tecla == Input.Keys.ENTER) {
                    if (sessao == null && partida.getEstado()==EstadoPartida.VITORIA) {
                        game.setScreen(new RankingScreen(game,partida,entrou));
                    } else if (envio == EstadoEnvio.CONFIRMADO && partida.getEstado()==EstadoPartida.VITORIA) {
                        game.setScreen(new ResultadosOnlineScreen(game, false, true));
                    } else game.menu();
                    return true;
                }
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
        int pontosAntes=partida.getPontos();
        partida.atualizarInimigos(delta).ifPresent(resultado -> processarResultado(resultado, pontosAntes));
        efeitos.atualizar(delta);textos.atualizar(delta);tremor.atualizar(delta);
        piscar=Math.max(0,piscar-delta);flash=Math.max(0,flash-delta);
        game.viewport.calculateScissors(game.batch.getTransformMatrix(), areaMapa, recorteMapa);
        if (ScissorStack.pushScissors(recorteMapa)) {
            desenharMapa(delta);
            ScissorStack.popScissors();
        }
        if(flash>0){
            com.badlogic.gdx.Gdx.gl.glEnable(GL20.GL_BLEND);
            com.badlogic.gdx.Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
            game.shapes.begin(ShapeRenderer.ShapeType.Filled);
            game.shapes.setColor(1f,.1f,.1f,flash*.9f);
            game.shapes.rect(MundoRenderer.X,MundoRenderer.Y,MundoRenderer.TAMANHO,MundoRenderer.TAMANHO);
            game.shapes.end();com.badlogic.gdx.Gdx.gl.glDisable(GL20.GL_BLEND);
        }
        hud.desenhar(game.shapes,game.batch,game.font,partida,mensagem,game.sons.mudo());
        if(partida.getEstado()!=EstadoPartida.EM_ANDAMENTO){
            String titulo = partida.getEstado()==EstadoPartida.VITORIA ? "MISSÃO CUMPRIDA!" : "GAME OVER";
            centralizar(envio == EstadoEnvio.INATIVO ? titulo + " Enter: continuar" : titulo,538);
            if (envio != EstadoEnvio.INATIVO) centralizar(avisoEnvio, 516);
            if (envio == EstadoEnvio.FALHOU) centralizar("R: reenviar | Enter: continuar", 494);
        }
    }
    private void desenharMapa(float delta) {
        float amp=tremor.amplitude();
        game.batch.setTransformMatrix(tremorMatriz.setToTranslation((float)Math.sin(com.badlogic.gdx.utils.TimeUtils.millis()*.08)*amp,(float)Math.cos(com.badlogic.gdx.utils.TimeUtils.millis()*.11)*amp,0));
        game.batch.begin();
        mundo.desenhar(game.batch, partida.getMissao(), delta, orientacao,
                quadroChama, piscar, efeitos);
        for(var t:textos.listar()){
            var cor = MissaoMarteGame.COR_TEXTO;
            game.font.setColor(cor.r,cor.g,cor.b,t.opacidade());
            game.font.draw(game.batch,t.conteudo,mundo.geometria().xCanto(t.x),mundo.geometria().yCanto(t.yVisual()) + mundo.geometria().celula());
        }
        game.font.setColor(MissaoMarteGame.COR_TEXTO);game.batch.end();
        game.batch.setTransformMatrix(tremorMatriz.idt());
    }
}
