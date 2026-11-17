package missaomarte.servidor.ranking;

import java.util.List;
import missaomarte.servidor.partida.PartidaRepository;
import missaomarte.servidor.partida.Resultado;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/ranking")
public class RankingController {
    private final PartidaRepository partidas;

    public RankingController(PartidaRepository partidas) { this.partidas = partidas; }

    @GetMapping
    public List<PosicaoRanking> ranking() {
        return partidas.melhoresVitorias(Resultado.VITORIA, PageRequest.of(0, 10));
    }
}
