package missaomarte.servidor.partida;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/partidas")
public class PartidaController {
    private final PartidaService partidas;

    public PartidaController(PartidaService partidas) { this.partidas = partidas; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Partida.Registro registrar(@AuthenticationPrincipal Jwt token, @Valid @RequestBody ResultadoRecebido dados) {
        return partidas.registrar(Long.parseLong(token.getSubject()), dados);
    }

    @GetMapping("/minhas")
    public List<Partida.Registro> historico(@AuthenticationPrincipal Jwt token,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho) {
        return partidas.historico(Long.parseLong(token.getSubject()), pagina, tamanho);
    }
}
