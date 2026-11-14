package missaomarte.gdx;
import com.badlogic.gdx.Input;
import missaomarte.application.Comando;
/** Adaptador: os codigos de teclado nao atravessam a fronteira do dominio. */
public final class TecladoParaComando {
    private TecladoParaComando() {}
    public static Comando converter(int tecla) {
        return switch (tecla) {
            case Input.Keys.W, Input.Keys.UP -> Comando.CIMA;
            case Input.Keys.S, Input.Keys.DOWN -> Comando.BAIXO;
            case Input.Keys.A, Input.Keys.LEFT -> Comando.ESQUERDA;
            case Input.Keys.D, Input.Keys.RIGHT -> Comando.DIREITA;
            case Input.Keys.E -> Comando.EMBARCAR;
            case Input.Keys.Q, Input.Keys.ESCAPE -> Comando.SAIR;
            default -> Comando.INVALIDO;
        };
    }
}
