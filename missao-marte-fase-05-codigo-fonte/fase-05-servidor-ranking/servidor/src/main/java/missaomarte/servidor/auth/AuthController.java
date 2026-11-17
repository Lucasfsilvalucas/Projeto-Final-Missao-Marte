package missaomarte.servidor.auth;

import java.time.Duration;
import java.time.Instant;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.*;
import missaomarte.servidor.usuario.Credenciais;
import missaomarte.servidor.usuario.UsuarioService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Duration VALIDADE = Duration.ofHours(2);
    private final UsuarioService usuarios;
    private final JwtEncoder encoder;

    public AuthController(UsuarioService usuarios, JwtEncoder encoder) {
        this.usuarios = usuarios;
        this.encoder = encoder;
    }

    @PostMapping("/login")
    public Entrada entrar(@RequestBody Credenciais dados) {
        var usuario = usuarios.autenticar(dados);
        Instant agora = Instant.now();
        var claims = JwtClaimsSet.builder().subject(usuario.getId().toString())
                .issuedAt(agora).expiresAt(agora.plus(VALIDADE)).claim("nome", usuario.getNome()).build();
        var headers = JwsHeader.with(MacAlgorithm.HS256).build();
        var token = encoder.encode(JwtEncoderParameters.from(headers, claims));
        return new Entrada(token.getTokenValue(), "Bearer", VALIDADE.toSeconds(), usuario.getNome());
    }

    public record Entrada(String token, String tipo, long expiraEm, String nome) {
        @Override public String toString() { return "Entrada[nome=" + nome + "]"; }
    }
}
