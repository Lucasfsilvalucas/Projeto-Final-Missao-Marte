package missaomarte.gdx;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;
import missaomarte.application.servidor.Resposta;
import missaomarte.application.servidor.ServidorDoJogo;
import missaomarte.application.servidor.Sessao;

final class ResultadosOnlineScreen extends TelaBase {
    private final Sessao sessao;
    private final boolean historico;
    private final boolean aposVitoria;
    private Resposta<List<String>> dados;
    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    ResultadosOnlineScreen(MissaoMarteGame game, boolean historico, boolean aposVitoria) {
        super(game);
        this.sessao = game.sessao;
        this.historico = historico;
        this.aposVitoria = aposVitoria;
    }

    @Override public void show() {
        super.show();
        instalar(new InputAdapter() {
            @Override public boolean keyDown(int tecla) {
                if (tecla == Input.Keys.ESCAPE || tecla == Input.Keys.ENTER) {
                    game.menu();
                    return true;
                }
                if (tecla == Input.Keys.R && dados != null) {
                    carregar();
                    return true;
                }
                return false;
            }
        });
        carregar();
    }

    private void carregar() {
        dados = null;
        if (historico) game.servidor.historico(sessao, this::mostrarHistorico);
        else game.servidor.ranking(sessao, this::mostrarRanking);
    }

    private void mostrarHistorico(Resposta<List<ServidorDoJogo.Registro>> retorno) {
        mostrar(retorno, registros -> {
            var linhas = registros.stream().map(item ->
                    formatarData(item.dataHora(), ZoneId.systemDefault()) + "  |  " + item.resultado()
                    + "  |  " + item.dificuldade() + "  |  " + item.pontuacao() + " pts  |  "
                    + item.duracaoSegundos() + " s  |  " + item.movimentos() + " mov.").toList();
            dados = Resposta.sucesso(linhas);
        });
    }

    private void mostrarRanking(Resposta<List<ServidorDoJogo.Posicao>> retorno) {
        mostrar(retorno, posicoes -> {
            var linhas = new java.util.ArrayList<String>();
            boolean noTop = false;
            for (var item : posicoes) {
                linhas.add((linhas.size() + 1) + ". " + item.nome() + "  |  " + item.pontuacao()
                        + " pts  |  " + item.duracaoSegundos() + " s  |  " + item.dificuldade());
                noTop |= item.nome().equalsIgnoreCase(sessao.nome());
            }
            if (aposVitoria) linhas.add(noTop ? "Você está no Top 10." : "Você está fora do Top 10.");
            dados = Resposta.sucesso(List.copyOf(linhas));
        });
    }

    private <T> void mostrar(Resposta<T> retorno, Consumer<T> preencher) {
        if (game.getScreen() != this) return;
        if (retorno.naoAutorizada()) {
            game.expirarSessao();
        } else if (retorno.sucesso()) {
            preencher.accept(retorno.valor());
        } else {
            dados = Resposta.erro(retorno.erro(), false);
        }
    }

    static String formatarData(Instant instante, ZoneId fuso) {
        return DATA.withZone(fuso).format(instante);
    }

    @Override protected void desenhar(float delta) {
        centralizar(historico ? "MEU HISTÓRICO" : "RANKING GLOBAL - TOP 10", 520);
        if (dados == null) centralizar("Carregando...", 400);
        else if (!dados.sucesso()) {
            centralizar(dados.erro(), 400);
            centralizar("R: tentar novamente", 355);
        } else if (dados.valor().isEmpty()) {
            centralizar(historico ? "Nenhuma partida registrada." : "Nenhuma vitória no ranking.", 400);
        } else {
            float y = 465;
            for (String linha : dados.valor()) {
                centralizar(linha, y);
                y -= 29;
            }
        }
        centralizar("Enter/Esc: menu", 90);
    }
}
