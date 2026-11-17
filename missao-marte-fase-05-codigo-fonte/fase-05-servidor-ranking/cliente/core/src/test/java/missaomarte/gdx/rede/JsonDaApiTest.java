package missaomarte.gdx.rede;

import com.badlogic.gdx.utils.JsonReader;
import java.util.UUID;
import missaomarte.application.EstadoPartida;
import missaomarte.application.servidor.PartidaConcluida;
import missaomarte.domain.Dificuldade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JsonDaApiTest {
    @Test void preservaAspasEBarraNaSenha() {
        String senha = "aspas\"barra\\fim";
        var json = new JsonReader().parse(JsonDaApi.credenciais("NomeTeste", senha));
        assertEquals("NomeTeste", json.getString("nome"));
        assertEquals(senha, json.getString("senha"));
    }

    @Test void enviaNumerosNegativosSemAspasEIncluiIdDoEnvio() {
        UUID id = UUID.randomUUID();
        var partida = new PartidaConcluida(Dificuldade.MEDIO, EstadoPartida.DERROTA, -15, 20, 3, id);
        var json = new JsonReader().parse(JsonDaApi.partida(partida));
        assertTrue(json.get("pontuacao").isNumber());
        assertEquals(-15, json.getInt("pontuacao"));
        assertEquals(id.toString(), json.getString("idEnvio"));
        assertNull(json.get("usuario"));
    }

    @Test void leSessaoSemExporTokenEmTexto() {
        var sessao = JsonDaApi.sessao("""
                {"nome":"Piloto","token":"token-de-teste"}
                """);
        assertEquals("Piloto", sessao.nome());
        assertEquals("token-de-teste", sessao.token());
        assertFalse(sessao.toString().contains(sessao.token()));
        assertThrows(IllegalArgumentException.class, () -> JsonDaApi.sessao("{\"nome\":\"Piloto\"}"));
    }

    @Test void leRankingeHistoricoInclusiveVazios() {
        var ranking = JsonDaApi.ranking("""
                [{"nome":"Piloto","pontuacao":130,"duracaoSegundos":9,"dificuldade":"MEDIO"}]
                """);
        assertEquals(130, ranking.get(0).pontuacao());
        var historico = JsonDaApi.historico("""
                [{"id":2,"resultado":"VITORIA","pontuacao":130,"duracaoSegundos":9,
                "dificuldade":"MEDIO","movimentos":8,"dataHora":"2026-11-17T19:30:00Z"}]
                """);
        assertEquals("VITORIA", historico.get(0).resultado());
        assertEquals("2026-11-17T19:30:00Z", historico.get(0).dataHora().toString());
        assertTrue(JsonDaApi.ranking("[]").isEmpty());
        assertTrue(JsonDaApi.historico("[]").isEmpty());
    }

    @Test void trataErrosSemExibirCorpoBruto() {
        assertEquals("Informe a senha.", JsonDaApi.erro(400, """
                {"detail":"Revise.","campos":{"senha":"Informe a senha."}}
                """));
        assertEquals("Nome duplicado.", JsonDaApi.erro(409, "{\"detail\":\"Nome duplicado.\"}"));
        assertEquals("Usuário ou senha inválidos.", JsonDaApi.erro(401, ""));
        assertEquals("Erro do servidor. Tente novamente.", JsonDaApi.erro(503, "<html>mensagem</html>"));
        assertEquals("A operação foi recusada (HTTP 400).", JsonDaApi.erro(400, "corpo bruto"));
    }
}
