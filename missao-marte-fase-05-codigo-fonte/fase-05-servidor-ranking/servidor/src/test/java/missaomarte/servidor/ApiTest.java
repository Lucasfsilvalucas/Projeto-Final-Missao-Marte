package missaomarte.servidor;

import com.jayway.jsonpath.JsonPath;
import java.time.Instant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApiTest {
    private static final String SENHA = "uma-senha-de-teste";
    @Autowired MockMvc mvc;
    @Autowired JdbcTemplate banco;
    @Autowired JwtEncoder encoder;

    @AfterEach void limpar() {
        banco.update("delete from partida");
        banco.update("delete from usuario");
    }

    @Test void cadastroValidaDadosEArmazenaHashComSal() throws Exception {
        cadastro("PilotoA").andExpect(status().isCreated()).andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.senha").doesNotExist()).andExpect(jsonPath("$.senhaHash").doesNotExist());
        cadastro("PilotoB").andExpect(status().isCreated());
        var hashes = banco.queryForList("select senha_hash from usuario", String.class);
        assertEquals(2, hashes.size());
        assertNotEquals(hashes.get(0), hashes.get(1));
        assertTrue(hashes.stream().allMatch(hash -> new BCryptPasswordEncoder().matches(SENHA, hash)));
        cadastro("pILOTOa").andExpect(status().isConflict());
        cadastro("a").andExpect(status().isBadRequest()).andExpect(jsonPath("$.campos.nome").exists());
        mvc.perform(post("/api/usuarios").contentType("application/json").content(credenciais("Valido", "curta")))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.campos.senha").exists());
        mvc.perform(post("/api/usuarios").contentType("application/json").content(credenciais("Valido", "é".repeat(37))))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.campos.senha").exists());
    }

    @Test void loginUsaMensagemIgualParaNomeInexistenteESenhaErrada() throws Exception {
        cadastro("PilotoA").andExpect(status().isCreated());
        String incorreta = login("PilotoA", "senha-errada").andExpect(status().isUnauthorized())
                .andReturn().getResponse().getContentAsString();
        String inexistente = login("NaoExiste", "senha-errada").andExpect(status().isUnauthorized())
                .andReturn().getResponse().getContentAsString();
        assertEquals(JsonPath.<String>read(incorreta, "$.detail"),
                JsonPath.<String>read(inexistente, "$.detail"));
        login("pilotoa", SENHA).andExpect(status().isOk()).andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.expiraEm").value(7200)).andExpect(jsonPath("$.senha").doesNotExist());
    }

    @Test void protegeRankingMesmoComApiDocumentada() throws Exception {
        mvc.perform(get("/v3/api-docs")).andExpect(status().isOk());
        mvc.perform(get("/api/ranking")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/ranking").header("Authorization", "Bearer invalido"))
                .andExpect(status().isUnauthorized());
        Instant agora = Instant.now();
        var claims = JwtClaimsSet.builder().subject("1").issuedAt(agora.minusSeconds(7201))
                .expiresAt(agora.minusSeconds(1)).build();
        String expirado = encoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims))
                .getTokenValue();
        mvc.perform(get("/api/ranking").header("Authorization", "Bearer " + expirado))
                .andExpect(status().isUnauthorized());
        String token = criarConta("Piloto");
        mvc.perform(get("/api/ranking").header("Authorization", token))
                .andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test void historicoIsoladoEPaginadoEReenvioSemDuplicacao() throws Exception {
        String alice = criarConta("Alice");
        String bob = criarConta("Bob");
        String envio = java.util.UUID.randomUUID().toString();
        String repetir = partida("VITORIA", 120, 21).replace("}", ",\"usuario\":\"Bob\",\"idEnvio\":\"" + envio + "\"}");
        String primeiro = enviar(alice, repetir).andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        String repetido = enviar(alice, repetir).andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        assertEquals(JsonPath.<Integer>read(primeiro, "$.id"), JsonPath.<Integer>read(repetido, "$.id"));
        enviar(alice, partida("DERROTA", -10, 25)).andExpect(status().isCreated());
        enviar(alice, partida("ABORTADA", 120, 20)).andExpect(status().isBadRequest());
        enviar(alice, partida("VITORIA", 100001, 20)).andExpect(status().isBadRequest());
        enviar(alice, "{}").andExpect(status().isBadRequest());
        mvc.perform(get("/api/partidas/minhas?pagina=0&tamanho=1").header("Authorization", alice))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].resultado").value("DERROTA"));
        mvc.perform(get("/api/partidas/minhas?pagina=1&tamanho=1").header("Authorization", alice))
                .andExpect(jsonPath("$[0].resultado").value("VITORIA"));
        mvc.perform(get("/api/partidas/minhas?tamanho=51").header("Authorization", alice))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/api/partidas/minhas").header("Authorization", bob))
                .andExpect(content().json("[]"));
    }

    @Test void rankingUsaMelhorVitoriaTempoAntiguidadeELimite() throws Exception {
        String cedo = criarConta("VitoriaCedo");
        String tarde = criarConta("VitoriaTarde");
        String rapido = criarConta("Rapido");
        enviar(cedo, partida("VITORIA", 100, 20)).andExpect(status().isCreated());
        enviar(tarde, partida("VITORIA", 100, 20)).andExpect(status().isCreated());
        enviar(rapido, partida("VITORIA", 100, 19)).andExpect(status().isCreated());
        enviar(rapido, partida("VITORIA", 99, 1)).andExpect(status().isCreated());
        enviar(rapido, partida("DERROTA", 500, 1)).andExpect(status().isCreated());
        String eliminado = criarConta("Eliminado");
        enviar(eliminado, partida("VITORIA", 0, 1)).andExpect(status().isCreated());
        enviar(eliminado, partida("DERROTA", 900, 1)).andExpect(status().isCreated());
        mvc.perform(get("/api/ranking").header("Authorization", cedo))
                .andExpect(jsonPath("$.length()").value(3));
        for (int i = 0; i < 8; i++) {
            String outro = criarConta("Jogador" + i);
            enviar(outro, partida("VITORIA", 10 + i, 25)).andExpect(status().isCreated());
        }
        mvc.perform(get("/api/ranking").header("Authorization", cedo))
                .andExpect(jsonPath("$.length()").value(10))
                .andExpect(jsonPath("$[0].nome").value("Rapido"))
                .andExpect(jsonPath("$[0].pontuacao").value(100))
                .andExpect(jsonPath("$[1].nome").value("VitoriaCedo"))
                .andExpect(jsonPath("$[2].nome").value("VitoriaTarde"))
                .andExpect(jsonPath("$[0].senhaHash").doesNotExist());
    }

    private String criarConta(String nome) throws Exception {
        cadastro(nome).andExpect(status().isCreated());
        var resposta = login(nome, SENHA).andExpect(status().isOk()).andReturn().getResponse();
        return "Bearer " + JsonPath.<String>read(resposta.getContentAsString(), "$.token");
    }

    private ResultActions cadastro(String nome) throws Exception {
        return mvc.perform(post("/api/usuarios").contentType("application/json").content(credenciais(nome, SENHA)));
    }

    private ResultActions login(String nome, String senha) throws Exception {
        return mvc.perform(post("/api/auth/login").contentType("application/json").content(credenciais(nome, senha)));
    }

    private ResultActions enviar(String token, String dados) throws Exception {
        return mvc.perform(post("/api/partidas").header("Authorization", token)
                .contentType("application/json").content(dados));
    }

    private String credenciais(String nome, String senha) {
        return """
                {"nome":"%s","senha":"%s"}
                """.formatted(nome, senha);
    }

    private String partida(String resultado, int pontos, int duracao) {
        return """
                {"dificuldade":"MEDIO","resultado":"%s","pontuacao":%d,"duracaoSegundos":%d,"movimentos":5}
                """.formatted(resultado, pontos, duracao);
    }
}
