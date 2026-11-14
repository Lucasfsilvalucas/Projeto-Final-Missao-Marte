package missaomarte.gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import missaomarte.application.*;
import missaomarte.domain.*;
import java.util.List;

final class GameScreen extends TelaBase {
    private final Partida partida;
    private String mensagem="Explore Marte e resgate os passageiros.";
    private boolean rankingGravado;
    private boolean entrou;
    private final MapaGeometria geometria = new MapaGeometria(42,90,35,5);
    GameScreen(MissaoMarteGame game,Partida partida) { super(game);this.partida=partida; }
    @Override public void show() { super.show(); instalar(new InputAdapter() {
        @Override public boolean keyDown(int keycode) {
            if (partida.getEstado()!=EstadoPartida.EM_ANDAMENTO) {
                if (keycode==Input.Keys.ENTER) {
                    if(partida.getEstado()==EstadoPartida.VITORIA) game.setScreen(new RankingScreen(game,partida,entrou));
                    else game.menu();
                    return true;
                }
                if (keycode==Input.Keys.ESCAPE) {game.menu();return true;}
                return false;
            }
            Comando comando=TecladoParaComando.converter(keycode);
            if(comando==Comando.INVALIDO) return false;
            if(comando==Comando.SAIR) {partida.executar(comando);game.menu();return true;}
            ResultadoTurno resultado=partida.executar(comando);
            mensagem=resultado.mensagem();
            if(resultado.estado()==EstadoPartida.VITORIA && !rankingGravado) {
                rankingGravado=true; entrou=game.ranking.registrarVitoria(partida);
            }
            return true;
        }
    }); }
    private Color cor(EntidadeMapa e) {
        if(e instanceof Nave) return new Color(.22f,.85f,.7f,1);
        if(e instanceof Passageiro) return new Color(.98f,.82f,.35f,1);
        if(e instanceof Asteroide) return new Color(.58f,.58f,.65f,1);
        if(e instanceof Inimigo) return new Color(.95f,.25f,.28f,1);
        return new Color(.28f,.52f,.98f,1);
    }
    @Override protected void desenhar(float delta) {
        Missao m=partida.getMissao();
        float c=geometria.celula();
        game.shapes.begin(ShapeRenderer.ShapeType.Filled);
        for(int y=-5;y<=5;y++) for(int x=-5;x<=5;x++) {
            game.shapes.setColor((x+y)%2==0?new Color(.09f,.15f,.22f,1):new Color(.12f,.19f,.27f,1));
            game.shapes.rect(geometria.xCanto(x),geometria.yCanto(y),c-1,c-1);
        }
        // A ordem mantem a nave sobre a base e as demais entidades.
        for(EntidadeMapa e:m.entidadesVisiveis()) {
            Posicao p=e.getPosicao();
            game.shapes.setColor(cor(e));
            game.shapes.rect(geometria.xCanto(p.x())+3,geometria.yCanto(p.y())+3,c-7,c-7);
        }
        game.shapes.end();
        game.batch.begin();
        game.font.setColor(Color.WHITE);
        for(EntidadeMapa e:m.entidadesVisiveis()) {
            Posicao p=e.getPosicao();
            game.font.draw(game.batch,String.valueOf(e.getSimbolo()),geometria.xCanto(p.x())+11,geometria.yCanto(p.y())+24);
        }
        game.font.draw(game.batch,"MISSAO MARTE",475,545);
        game.font.draw(game.batch,"Piloto: "+partida.getPiloto(),475,500);
        game.font.draw(game.batch,"Dificuldade: "+partida.getDificuldade().name(),475,465);
        game.font.draw(game.batch,"Vidas: "+m.getNave().getVidas(),475,430);
        game.font.draw(game.batch,"Pontos: "+partida.getPontos(),475,395);
        game.font.draw(game.batch,"A bordo: "+m.getNave().getPassageiros().size()+"/"+m.getNave().getCapacidade(),475,360);
        game.font.draw(game.batch,"Entregues: "+m.getEntregues()+"/"+m.getTotalPassageiros(),475,325);
        game.font.draw(game.batch,"No mapa: "+m.getPassageiros().size(),475,290);
        game.font.draw(game.batch,"Movimentos: "+partida.getMovimentos(),475,255);
        game.font.draw(game.batch,"WASD/Setas: mover",475,210);
        game.font.draw(game.batch,"E: embarcar | Q/Esc: sair",475,185);
        game.font.draw(game.batch,mensagem,45,65,710,com.badlogic.gdx.utils.Align.left,true);
        if (partida.getEstado()!=EstadoPartida.EM_ANDAMENTO) {
            String fim=partida.getEstado()==EstadoPartida.VITORIA?"MISSAO CUMPRIDA!":"GAME OVER";
            game.font.draw(game.batch,fim+"  Enter: continuar",130,570);
        }
        game.batch.end();
    }
}
