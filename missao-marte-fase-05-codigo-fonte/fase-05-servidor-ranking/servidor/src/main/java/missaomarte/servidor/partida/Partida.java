package missaomarte.servidor.partida;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import missaomarte.servidor.usuario.Usuario;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "id_envio"}))
public class Partida {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    @Column(name = "id_envio")
    private UUID idEnvio;
    @Column(nullable = false) @Enumerated(EnumType.STRING)
    private Dificuldade dificuldade;
    @Column(nullable = false) @Enumerated(EnumType.STRING)
    private Resultado resultado;
    private int pontuacao;
    private int duracaoSegundos;
    private int movimentos;
    @Column(nullable = false)
    private Instant dataHora;

    protected Partida() {}

    Partida(Usuario usuario, ResultadoRecebido dados) {
        this.usuario = usuario;
        this.idEnvio = dados.idEnvio();
        this.dificuldade = dados.dificuldade();
        this.resultado = dados.resultado();
        this.pontuacao = dados.pontuacao();
        this.duracaoSegundos = dados.duracaoSegundos();
        this.movimentos = dados.movimentos();
        this.dataHora = Instant.now();
    }

    public record Registro(long id, Dificuldade dificuldade, Resultado resultado, int pontuacao,
                           int duracaoSegundos, int movimentos, Instant dataHora) {}

    Registro registro() {
        return new Registro(id, dificuldade, resultado, pontuacao, duracaoSegundos, movimentos, dataHora);
    }
}
