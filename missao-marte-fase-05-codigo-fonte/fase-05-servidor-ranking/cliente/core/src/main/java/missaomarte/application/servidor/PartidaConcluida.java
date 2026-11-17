package missaomarte.application.servidor;

import java.util.UUID;
import missaomarte.application.EstadoPartida;
import missaomarte.application.Partida;
import missaomarte.domain.Dificuldade;

public record PartidaConcluida(Dificuldade dificuldade, EstadoPartida resultado, int pontuacao,
                               long duracaoSegundos, int movimentos, UUID idEnvio) {
    public static PartidaConcluida de(Partida partida) {
        var resultado = partida.getEstado();
        if (resultado != EstadoPartida.VITORIA && resultado != EstadoPartida.DERROTA) {
            throw new IllegalArgumentException("A partida precisa terminar em vitória ou derrota.");
        }
        return new PartidaConcluida(partida.getDificuldade(), resultado, partida.getPontos(),
                partida.getTempoSegundos(), partida.getMovimentos(), UUID.randomUUID());
    }
}
