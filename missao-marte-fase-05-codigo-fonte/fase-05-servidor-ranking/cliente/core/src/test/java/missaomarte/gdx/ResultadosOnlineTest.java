package missaomarte.gdx;

import java.time.Instant;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ResultadosOnlineTest {
    @Test void apresentaHorarioNoFusoDoJogador() {
        assertEquals("17/11/2026 17:26", ResultadosOnlineScreen.formatarData(
                Instant.parse("2026-11-17T20:26:00Z"), ZoneId.of("America/Fortaleza")));
    }
}
