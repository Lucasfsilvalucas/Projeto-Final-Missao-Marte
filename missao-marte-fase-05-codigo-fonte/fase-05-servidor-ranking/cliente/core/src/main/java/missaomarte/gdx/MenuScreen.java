package missaomarte.gdx;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import missaomarte.gdx.recursos.Imagem;
import missaomarte.gdx.recursos.Som;
final class MenuScreen extends TelaBase {
    private final String aviso;
    MenuScreen(MissaoMarteGame game, String aviso) { super(game); this.aviso = aviso; }
    @Override public void show() {
        super.show(); instalar(new InputAdapter() {
            @Override public boolean keyDown(int k) {
                if(k>=Input.Keys.NUM_1 && k<=Input.Keys.NUM_3) game.sons.tocar(Som.SELECIONAR);
                switch(k) {
                    case Input.Keys.NUM_1,Input.Keys.NUMPAD_1 -> game.novaPartida();
                    case Input.Keys.NUM_2,Input.Keys.NUMPAD_2 -> game.ranking();
                    case Input.Keys.NUM_3,Input.Keys.NUMPAD_3 -> {
                        if (game.sessao == null) game.setScreen(new ConfirmarLimpezaScreen(game));
                        else game.setScreen(new ResultadosOnlineScreen(game, true, false));
                    }
                    case Input.Keys.NUM_4,Input.Keys.NUMPAD_4 -> {
                        if (game.sessao == null) game.setScreen(new LoginScreen(game));
                        else {
                            game.sessao = null;
                            game.menu("Sessão encerrada.");
                        }
                    }
                    case Input.Keys.NUM_5,Input.Keys.NUMPAD_5,Input.Keys.ESCAPE -> Gdx.app.exit();
                    default -> { return false; }
                } return true;
            }
        });
    }
    @Override protected void desenhar(float delta) {
        game.batch.begin();
        game.batch.draw(game.recursos.animacao(Imagem.PROPULSOR,.09f).getKeyFrame(com.badlogic.gdx.utils.TimeUtils.millis()/1000f,true),370,475,60,60);
        game.batch.draw(game.recursos.quadro(Imagem.NAVE),370,480,60,60);
        game.batch.end();
        centralizar("MISSÃO MARTE UNIFOR",430);
        centralizar("1 - Nova missão",350);
        boolean convidado = game.sessao == null;
        centralizar(convidado ? "Modo convidado" : "Piloto: " + game.sessao.nome(),390);
        centralizar(convidado ? "2 - Ranking local (Top 5)" : "2 - Ranking global (Top 10)",310);
        centralizar(convidado ? "3 - Limpar ranking local" : "3 - Meu histórico",270);
        centralizar(convidado ? "4 - Entrar ou criar conta" : "4 - Sair da conta",230);
        centralizar("5 - Sair",190);
        centralizar(aviso,145);
        centralizar("WASD / Setas para mover durante a partida",108);
    }
}
