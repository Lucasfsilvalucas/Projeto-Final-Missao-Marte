package missaomarte.gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
final class ConfirmarLimpezaScreen extends TelaBase {
    private boolean limpou;
    ConfirmarLimpezaScreen(MissaoMarteGame game) { super(game); }
    @Override public void show() { super.show(); instalar(new InputAdapter() {
        @Override public boolean keyDown(int k) {
            if (k==Input.Keys.S || k==Input.Keys.Y) { game.ranking.limpar();limpou=true;return true; }
            if (k==Input.Keys.ESCAPE || k==Input.Keys.N || k==Input.Keys.ENTER) { game.menu();return true; }
            return false;
        }
    }); }
    @Override protected void desenhar(float delta) {
        centralizar("LIMPAR RANKING?",405);
        centralizar(limpou ? "Ranking limpo!" : "S / Y = confirmar | N / Esc = cancelar",330);
        if (limpou) centralizar("Enter ou Esc para voltar",275);
    }
}
