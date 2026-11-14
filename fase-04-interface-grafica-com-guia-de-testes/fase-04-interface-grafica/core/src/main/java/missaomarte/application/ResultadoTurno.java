package missaomarte.application;
import java.util.List;
public record ResultadoTurno(String mensagem, EstadoPartida estado, List<EventoTurno> eventos) {
    public ResultadoTurno(String mensagem, EstadoPartida estado) { this(mensagem, estado, List.of()); }
    public ResultadoTurno { eventos = List.copyOf(eventos); }
}
