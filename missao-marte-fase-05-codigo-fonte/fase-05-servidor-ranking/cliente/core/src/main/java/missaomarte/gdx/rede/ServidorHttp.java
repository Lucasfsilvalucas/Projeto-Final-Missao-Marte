package missaomarte.gdx.rede;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import java.net.URI;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import missaomarte.application.servidor.*;

public final class ServidorHttp implements ServidorDoJogo {
    private final String endereco;

    public ServidorHttp(String endereco) {
        URI uri = URI.create(endereco);
        if (uri.getHost() == null || uri.getUserInfo() != null || uri.getQuery() != null
                || uri.getFragment() != null || !("http".equals(uri.getScheme()) || "https".equals(uri.getScheme()))) {
            throw new IllegalArgumentException("Informe uma URL HTTP ou HTTPS para missaomarte.servidor.");
        }
        this.endereco = endereco.replaceAll("/+$", "");
    }

    @Override public void cadastrar(String nome, String senha, Consumer<Resposta<Void>> retorno) {
        requisitar("/api/usuarios", JsonDaApi.credenciais(nome, senha), null, texto -> null, retorno);
    }

    @Override public void entrar(String nome, String senha, Consumer<Resposta<Sessao>> retorno) {
        requisitar("/api/auth/login", JsonDaApi.credenciais(nome, senha), null, JsonDaApi::sessao, retorno);
    }

    @Override public void enviar(Sessao sessao, PartidaConcluida partida, Consumer<Resposta<Registro>> retorno) {
        requisitar("/api/partidas", JsonDaApi.partida(partida), sessao, JsonDaApi::registro, retorno);
    }

    @Override public void ranking(Sessao sessao, Consumer<Resposta<List<Posicao>>> retorno) {
        requisitar("/api/ranking", null, sessao, JsonDaApi::ranking, retorno);
    }

    @Override public void historico(Sessao sessao, Consumer<Resposta<List<Registro>>> retorno) {
        requisitar("/api/partidas/minhas?pagina=0&tamanho=10", null, sessao, JsonDaApi::historico, retorno);
    }

    private <T> void requisitar(String rota, String json, Sessao sessao, Function<String, T> converter,
                                Consumer<Resposta<T>> retorno) {
        var pedido = new Net.HttpRequest(json == null ? Net.HttpMethods.GET : Net.HttpMethods.POST);
        pedido.setUrl(endereco + rota);
        pedido.setTimeOut(5000);
        pedido.setFollowRedirects(false);
        pedido.setHeader("Accept", "application/json, application/problem+json");
        if (json != null) {
            pedido.setHeader("Content-Type", "application/json; charset=UTF-8");
            pedido.setContent(json);
        }
        if (sessao != null) pedido.setHeader("Authorization", "Bearer " + sessao.token());
        Gdx.net.sendHttpRequest(pedido, new Net.HttpResponseListener() {
            @Override public void handleHttpResponse(Net.HttpResponse http) {
                int codigo = http.getStatus().getStatusCode();
                Resposta<T> resposta;
                try {
                    String texto = http.getResultAsString();
                    resposta = codigo >= 200 && codigo < 300
                            ? Resposta.sucesso(converter.apply(texto))
                            : Resposta.erro(JsonDaApi.erro(codigo, texto), codigo == 401 && sessao != null);
                } catch (RuntimeException ex) {
                    resposta = Resposta.erro("Resposta inválida do servidor.", codigo == 401 && sessao != null);
                }
                concluir(resposta);
            }

            @Override public void failed(Throwable causa) {
                concluir(Resposta.erro("Não foi possível conectar ao servidor.", false));
            }

            @Override public void cancelled() {
                concluir(Resposta.erro("Conexão cancelada.", false));
            }

            private void concluir(Resposta<T> resposta) {
                Gdx.app.postRunnable(() -> retorno.accept(resposta));
            }
        });
    }
}
