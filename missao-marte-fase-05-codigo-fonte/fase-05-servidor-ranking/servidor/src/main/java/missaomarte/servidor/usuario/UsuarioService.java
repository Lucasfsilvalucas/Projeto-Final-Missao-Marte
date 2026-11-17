package missaomarte.servidor.usuario;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {
    public static final String CREDENCIAIS_INVALIDAS = "Usuário ou senha inválidos.";

    private final UsuarioRepository usuarios;
    private final PasswordEncoder senhas;
    private final String hashFalso;

    public UsuarioService(UsuarioRepository usuarios, PasswordEncoder senhas) {
        this.usuarios = usuarios;
        this.senhas = senhas;
        this.hashFalso = senhas.encode(UUID.randomUUID().toString());
    }

    public Usuario cadastrar(Credenciais dados) {
        String normalizado = dados.nome().toLowerCase(Locale.ROOT);
        var usuario = new Usuario(dados.nome(), normalizado, senhas.encode(dados.senha()));
        try {
            return usuarios.saveAndFlush(usuario);
        } catch (DataIntegrityViolationException ex) {
            if (usuarios.findByNomeNormalizado(normalizado).isEmpty()) throw ex;
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Nome de usuário já existe.");
        }
    }

    public Usuario autenticar(Credenciais dados) {
        if (dados.nome() == null || dados.senha() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o nome e a senha.");
        }
        String nome = dados.nome();
        String senha = dados.senha();
        boolean senhaAceitavel = senha.getBytes(StandardCharsets.UTF_8).length <= 72;
        var usuario = usuarios.findByNomeNormalizado(nome.toLowerCase(Locale.ROOT));
        String hash = usuario.map(Usuario::getSenhaHash).orElse(hashFalso);
        boolean confere = senhas.matches(senhaAceitavel ? senha : "", hash);
        if (!senhaAceitavel || !confere || usuario.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, CREDENCIAIS_INVALIDAS);
        }
        return usuario.orElseThrow();
    }
}
