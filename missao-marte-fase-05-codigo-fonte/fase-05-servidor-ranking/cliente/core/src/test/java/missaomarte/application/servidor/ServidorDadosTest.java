package missaomarte.application.servidor;

import java.util.List;
import java.util.Random;
import missaomarte.application.Comando;
import missaomarte.application.EstadoPartida;
import missaomarte.application.CriadorMissao;
import missaomarte.application.Partida;
import missaomarte.domain.Dificuldade;
import missaomarte.domain.Missao;
import missaomarte.domain.Nave;
import missaomarte.domain.Posicao;
import missaomarte.domain.Professor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ServidorDadosTest {
    @Test void formularioValidaCadastroSemExporSenha() {
        var dados = new FormularioDeConta();
        dados.trocarModo();
        "Pi!loto_".chars().forEach(c -> dados.digitar((char) c));
        assertEquals("Piloto_", dados.nome());
        dados.trocarCampo();
        dados.digitar('M');
        dados.digitar('\n');
        assertEquals("*", dados.senhaMascarada());
        assertNotNull(dados.erro());
        dados.trocarModo();
        assertNull(dados.erro());
        dados.apagar();
        assertNotNull(dados.erro());
        dados.trocarModo();
        "é".repeat(37).chars().forEach(c -> dados.digitar((char) c));
        assertEquals(36, dados.senhaMascarada().length());
        assertNull(dados.erro());
        dados.limparSenha();
        assertEquals("", dados.senha());
    }

    @Test void distingueSucessoFalhaESessaoExpirada() {
        assertTrue(Resposta.sucesso(1).sucesso());
        assertFalse(Resposta.erro("Erro", false).sucesso());
        assertTrue(Resposta.erro("Sessão expirada", true).naoAutorizada());
    }

    @Test void rejeitaPartidaEmAndamento() {
        var random = new Random(10);
        var dificuldade = Dificuldade.MEDIO;
        var partida = new Partida(new CriadorMissao(random).criar(dificuldade, 9), dificuldade, 9, "Piloto", random);
        assertThrows(IllegalArgumentException.class, () -> PartidaConcluida.de(partida));
    }

    @Test void converteVitoriaRealComPontuacaoEMovimentos() {
        var missao = new Missao(new Nave("Nave", 5), List.of(new Professor("Professor", new Posicao(1, 0))),
                List.of(), List.of());
        var partida = new Partida(missao, Dificuldade.FACIL, 5, "Piloto", new Random(0));
        partida.executar(Comando.DIREITA);
        partida.executar(Comando.EMBARCAR);
        partida.executar(Comando.ESQUERDA);

        var resultado = PartidaConcluida.de(partida);
        assertEquals(EstadoPartida.VITORIA, resultado.resultado());
        assertEquals(Dificuldade.FACIL, resultado.dificuldade());
        assertEquals(partida.getPontos(), resultado.pontuacao());
        assertEquals(2, resultado.movimentos());
        assertNotNull(resultado.idEnvio());
    }
}
