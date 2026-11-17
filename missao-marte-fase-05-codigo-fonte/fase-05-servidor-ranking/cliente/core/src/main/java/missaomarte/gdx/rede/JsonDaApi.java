package missaomarte.gdx.rede;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import missaomarte.application.servidor.PartidaConcluida;
import missaomarte.application.servidor.ServidorDoJogo.Posicao;
import missaomarte.application.servidor.ServidorDoJogo.Registro;
import missaomarte.application.servidor.Sessao;

final class JsonDaApi {
    private JsonDaApi() {}

    static String credenciais(String nome, String senha) {
        return objeto(json -> json.set("nome", nome).set("senha", senha));
    }

    static String partida(PartidaConcluida partida) {
        return objeto(json -> json.set("dificuldade", partida.dificuldade().name())
                .set("resultado", partida.resultado().name())
                .set("pontuacao", partida.pontuacao())
                .set("duracaoSegundos", partida.duracaoSegundos())
                .set("movimentos", partida.movimentos())
                .set("idEnvio", partida.idEnvio().toString()));
    }

    static Sessao sessao(String texto) {
        var json = ler(texto);
        return new Sessao(obrigatorio(json, "nome"), obrigatorio(json, "token"));
    }

    static Registro registro(String texto) { return registro(ler(texto)); }

    static List<Registro> historico(String texto) { return lista(texto, JsonDaApi::registro); }

    static List<Posicao> ranking(String texto) {
        return lista(texto, item -> new Posicao(obrigatorio(item, "nome"), item.getInt("pontuacao"),
                item.getLong("duracaoSegundos"), obrigatorio(item, "dificuldade")));
    }

    static String erro(int status, String texto) {
        try {
            var json = ler(texto);
            var campos = json.get("campos");
            if (campos != null && campos.isObject() && campos.child != null && campos.child.isString()) {
                String detalhe = mensagemCurta(campos.child.asString());
                if (detalhe != null) return detalhe;
            }
            var detail = json.get("detail");
            if (detail != null && detail.isString()) {
                String detalhe = mensagemCurta(detail.asString());
                if (detalhe != null) return detalhe;
            }
        } catch (RuntimeException ex) {
            // Usa a mensagem do status se o corpo não segue o contrato.
        }
        if (status == 401) return "Usuário ou senha inválidos.";
        if (status >= 500) return "Erro do servidor. Tente novamente.";
        return "A operação foi recusada (HTTP " + status + ").";
    }

    private static String mensagemCurta(String texto) {
        String mensagem = texto.replaceAll("[\\p{Cntrl}]", " ").trim();
        return mensagem.isEmpty() ? null : mensagem.substring(0, Math.min(mensagem.length(), 100));
    }

    private static Registro registro(JsonValue item) {
        return new Registro(item.getLong("id"), obrigatorio(item, "dificuldade"),
                obrigatorio(item, "resultado"), item.getInt("pontuacao"),
                item.getLong("duracaoSegundos"), item.getInt("movimentos"),
                Instant.parse(obrigatorio(item, "dataHora")));
    }

    private static <T> List<T> lista(String texto, Function<JsonValue, T> converter) {
        var json = ler(texto);
        if (!json.isArray()) throw new IllegalArgumentException("Esperava uma lista.");
        List<T> itens = new ArrayList<>();
        for (var item : json) itens.add(converter.apply(item));
        return List.copyOf(itens);
    }

    private static String obrigatorio(JsonValue json, String campo) {
        var valor = json.get(campo);
        if (valor == null || !valor.isString() || valor.asString().isBlank()) {
            throw new IllegalArgumentException("Campo ausente ou inválido.");
        }
        return valor.asString();
    }

    private static JsonValue ler(String texto) { return new JsonReader().parse(texto); }

    private static String objeto(EscreverCampos dados) {
        var texto = new StringWriter();
        try (var json = new JsonWriter(texto)) {
            json.setOutputType(JsonWriter.OutputType.json);
            json.object();
            dados.escrever(json);
            json.pop();
        } catch (IOException ex) {
            throw new IllegalStateException("Falha ao escrever JSON.", ex);
        }
        return texto.toString();
    }

    @FunctionalInterface
    private interface EscreverCampos { void escrever(JsonWriter json) throws IOException; }
}
