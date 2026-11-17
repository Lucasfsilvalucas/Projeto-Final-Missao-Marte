package missaomarte.gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import missaomarte.application.Partida;
import missaomarte.domain.Dificuldade;
final class NovaPartidaScreen extends TelaBase {
    private static final int LIMITE_MAPA = 9;
    private final StringBuilder nome=new StringBuilder();
    private Dificuldade dificuldade=Dificuldade.MEDIO;
    NovaPartidaScreen(MissaoMarteGame game) { super(game); }
    @Override public void show() { super.show(); instalar(new InputAdapter() {
        @Override public boolean keyDown(int k) {
            if (k==Input.Keys.ESCAPE) { game.menu();return true; }
            if (k==Input.Keys.UP || k==Input.Keys.LEFT) { dificuldade=Dificuldade.values()[(dificuldade.ordinal()+2)%3];return true; }
            if (k==Input.Keys.DOWN || k==Input.Keys.RIGHT) { dificuldade=Dificuldade.values()[(dificuldade.ordinal()+1)%3];return true; }
            if (game.sessao == null && k==Input.Keys.BACKSPACE && nome.length()>0) { nome.deleteCharAt(nome.length()-1);return true; }
            if (k==Input.Keys.ENTER) {
                String piloto = game.sessao != null ? game.sessao.nome()
                        : (nome.toString().isBlank() ? "Piloto Anonimo" : nome.toString().trim());
                Partida partida=new Partida(game.criador.criar(dificuldade,LIMITE_MAPA), dificuldade,LIMITE_MAPA,piloto,game.random);
                game.setScreen(new GameScreen(game,partida));return true;
            } return false;
        }
        @Override public boolean keyTyped(char c) {
            if (game.sessao == null && c>=32 && c<=126 && nome.length()<22) { nome.append(c);return true; }
            return false;
        }
    }); }
    @Override protected void desenhar(float delta) {
        centralizar("NOVA MISSÃO",455);
        centralizar("Piloto: " + (game.sessao == null ? nome + "_" : game.sessao.nome()),370);
        centralizar("Dificuldade: " + dificuldade.name(),320);
        centralizar("Setas: mudar dificuldade",250);
        centralizar("Enter: iniciar | Esc: menu",210);
    }
}
