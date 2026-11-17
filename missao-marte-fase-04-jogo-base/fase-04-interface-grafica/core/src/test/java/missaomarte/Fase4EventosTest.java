package missaomarte;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import missaomarte.application.*;
import missaomarte.domain.*;
import java.util.*;
class Fase4EventosTest {
    private Partida partida(){
        return new Partida(new Missao(new Nave("teste",5),List.of(new Professor("Ana",new Posicao(3,3))),List.of(),List.of()),Dificuldade.FACIL,5,"Teste",new Random(4));
    }
    @Test void construtorAntigoPermaneceCompativel(){
        assertTrue(new ResultadoTurno("texto",EstadoPartida.EM_ANDAMENTO).eventos().isEmpty());
    }
    @Test void movimentoRegistraEvento(){
        assertTrue(partida().executar(Comando.DIREITA).eventos().contains(EventoTurno.MOVIMENTO));
    }
    @Test void comandoInvalidoNaoGeraEvento(){
        assertTrue(partida().executar(Comando.INVALIDO).eventos().isEmpty());
    }
    @Test void abortarNaoGeraEvento(){
        assertTrue(partida().executar(Comando.SAIR).eventos().isEmpty());
    }
}
