package missaomarte.servidor.partida;

import java.util.UUID;
import jakarta.validation.constraints.*;

public record ResultadoRecebido(
        @NotNull Dificuldade dificuldade,
        @NotNull Resultado resultado,
        @NotNull @Min(-100000) @Max(100000) Integer pontuacao,
        @NotNull @Min(0) @Max(86400) Integer duracaoSegundos,
        @NotNull @Min(0) @Max(100000) Integer movimentos,
        UUID idEnvio) {}
