package missaomarte.gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import missaomarte.application.Partida;
final class RankingScreen extends TelaBase {
    private final Partida ultima;
    private final boolean entrou;
    RankingScreen(MissaoMarteGame game,Partida ultima,boolean entrou) { super(game);this.ultima=ultima;this.entrou=entrou; }
    @Override public void show() { super.show(); instalar(new InputAdapter() {
        @Override public boolean keyDown(int k) {
            if(k==Input.Keys.ESCAPE || k==Input.Keys.ENTER || k==Input.Keys.M) {game.menu();return true;}
            return false;
        }
    }); }
    @Override protected void desenhar(float delta) {
        centralizar("RANKING - TOP 5",545);
        if (ultima!=null) centralizar("Ultima missao: " + ultima.getPiloto() + " - " + ultima.getPontos()+" pts"+(entrou?" (Top 5)":""),500);
        var lista=game.ranking.listar();
        if (lista.isEmpty()) centralizar("Ainda nao ha pontuacoes registradas",425);
        for (int i=0;i<lista.size();i++) {
            var r=lista.get(i);
            escrever((i+1)+". "+r.nome()+" - "+r.pontos()+" pts - "+r.dificuldade().name()+" - "+r.passageirosEntregues()+" entregues",140,440-i*48);
        }
        centralizar("Enter / Esc: menu",100);
    }
}
