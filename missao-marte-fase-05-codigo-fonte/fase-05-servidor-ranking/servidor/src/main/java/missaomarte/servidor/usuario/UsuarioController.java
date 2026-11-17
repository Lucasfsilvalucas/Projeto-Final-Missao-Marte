package missaomarte.servidor.usuario;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioService usuarios;

    public UsuarioController(UsuarioService usuarios) { this.usuarios = usuarios; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioCriado cadastrar(@Valid @RequestBody Credenciais dados) {
        var usuario = usuarios.cadastrar(dados);
        return new UsuarioCriado(usuario.getId(), usuario.getNome());
    }

    public record UsuarioCriado(long id, String nome) {}
}
