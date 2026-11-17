package missaomarte.application.servidor;

import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;

public interface ServidorDoJogo {
    void cadastrar(String nome, String senha, Consumer<Resposta<Void>> retorno);
    void entrar(String nome, String senha, Consumer<Resposta<Sessao>> retorno);
    void enviar(Sessao sessao, PartidaConcluida partida, Consumer<Resposta<Registro>> retorno);
    void ranking(Sessao sessao, Consumer<Resposta<List<Posicao>>> retorno);
    void historico(Sessao sessao, Consumer<Resposta<List<Registro>>> retorno);

    record Registro(long id, String dificuldade, String resultado, int pontuacao,
                    long duracaoSegundos, int movimentos, Instant dataHora) {}
    record Posicao(String nome, int pontuacao, long duracaoSegundos, String dificuldade) {}
}
