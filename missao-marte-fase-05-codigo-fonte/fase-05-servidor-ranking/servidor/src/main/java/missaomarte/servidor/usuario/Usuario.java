package missaomarte.servidor.usuario;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 20)
    private String nome;
    @Column(nullable = false, unique = true, length = 20)
    private String nomeNormalizado;
    @Column(nullable = false, length = 60)
    private String senhaHash;
    @Column(nullable = false)
    private Instant criadoEm;

    protected Usuario() {}

    Usuario(String nome, String nomeNormalizado, String senhaHash) {
        this.nome = nome;
        this.nomeNormalizado = nomeNormalizado;
        this.senhaHash = senhaHash;
        this.criadoEm = Instant.now();
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    String getSenhaHash() { return senhaHash; }
}
