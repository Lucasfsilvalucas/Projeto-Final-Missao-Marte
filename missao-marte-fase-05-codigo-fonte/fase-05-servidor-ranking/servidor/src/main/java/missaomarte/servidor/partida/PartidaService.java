package missaomarte.servidor.partida;

import java.util.List;
import missaomarte.servidor.usuario.UsuarioRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PartidaService {
    private final PartidaRepository partidas;
    private final UsuarioRepository usuarios;

    public PartidaService(PartidaRepository partidas, UsuarioRepository usuarios) {
        this.partidas = partidas;
        this.usuarios = usuarios;
    }

    public Partida.Registro registrar(long usuarioId, ResultadoRecebido dados) {
        var usuario = usuarios.findById(usuarioId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Entre novamente."));
        if (dados.idEnvio() != null) {
            var anterior = partidas.findByUsuarioIdAndIdEnvio(usuarioId, dados.idEnvio());
            if (anterior.isPresent()) return anterior.get().registro();
        }
        try {
            return partidas.saveAndFlush(new Partida(usuario, dados)).registro();
        } catch (DataIntegrityViolationException ex) {
            if (dados.idEnvio() == null) throw ex;
            return partidas.findByUsuarioIdAndIdEnvio(usuarioId, dados.idEnvio())
                    .map(Partida::registro).orElseThrow(() -> ex);
        }
    }

    public List<Partida.Registro> historico(long usuarioId, int pagina, int tamanho) {
        if (pagina < 0 || tamanho < 1 || tamanho > 50 || (long) pagina * tamanho > Integer.MAX_VALUE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe página válida e tamanho entre 1 e 50.");
        }
        return partidas.findByUsuarioIdOrderByDataHoraDescIdDesc(usuarioId, PageRequest.of(pagina, tamanho))
                .stream().map(Partida::registro).toList();
    }
}
