package missaomarte;
import missaomarte.application.Comando;
import missaomarte.gdx.TecladoParaComando;
import com.badlogic.gdx.Input;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class TecladoParaComandoTest {
    @Test void movimentos() {
        assertEquals(Comando.CIMA,TecladoParaComando.converter(Input.Keys.W));
        assertEquals(Comando.CIMA,TecladoParaComando.converter(Input.Keys.UP));
        assertEquals(Comando.BAIXO,TecladoParaComando.converter(Input.Keys.DOWN));
        assertEquals(Comando.ESQUERDA,TecladoParaComando.converter(Input.Keys.A));
        assertEquals(Comando.DIREITA,TecladoParaComando.converter(Input.Keys.RIGHT));
    }
    @Test void embarqueESaida() {
        assertEquals(Comando.EMBARCAR,TecladoParaComando.converter(Input.Keys.E));
        assertEquals(Comando.SAIR,TecladoParaComando.converter(Input.Keys.ESCAPE));
        assertEquals(Comando.SAIR,TecladoParaComando.converter(Input.Keys.Q));
    }
    @Test void invalida() {assertEquals(Comando.INVALIDO,TecladoParaComando.converter(Input.Keys.F1));}
}
