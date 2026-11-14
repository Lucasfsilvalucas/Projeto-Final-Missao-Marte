package missaomarte.gdx;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
final class MenuScreen extends TelaBase {
    MenuScreen(MissaoMarteGame game) { super(game); }
    @Override public void show() {
        super.show(); instalar(new InputAdapter() {
            @Override public boolean keyDown(int k) {
                switch(k) {
                    case Input.Keys.NUM_1,Input.Keys.NUMPAD_1 -> game.novaPartida();
                    case Input.Keys.NUM_2,Input.Keys.NUMPAD_2 -> game.ranking();
                    case Input.Keys.NUM_3,Input.Keys.NUMPAD_3 -> game.setScreen(new ConfirmarLimpezaScreen(game));
                    case Input.Keys.NUM_4,Input.Keys.NUMPAD_4,Input.Keys.ESCAPE -> Gdx.app.exit();
                    default -> { return false; }
                } return true;
            }
        });
    }
    @Override protected void desenhar(float delta) {
        retangulo(130,155,540,315,new Color(.09f,.17f,.27f,1));
        centralizar("MISSAO MARTE UNIFOR",430);
        centralizar("1 - Nova missao",350);
        centralizar("2 - Ranking Top 5",310);
        centralizar("3 - Limpar ranking",270);
        centralizar("4 - Sair",230);
        centralizar("WASD / Setas para mover durante a partida",108);
    }
}
