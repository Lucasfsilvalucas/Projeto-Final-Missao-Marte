package missaomarte.servidor.partida;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import missaomarte.servidor.ranking.PosicaoRanking;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PartidaRepository extends JpaRepository<Partida, Long> {
    List<Partida> findByUsuarioIdOrderByDataHoraDescIdDesc(long usuarioId, Pageable pagina);
    Optional<Partida> findByUsuarioIdAndIdEnvio(long usuarioId, UUID idEnvio);

    @Query("""
            select new missaomarte.servidor.ranking.PosicaoRanking(
                p.usuario.nome, p.pontuacao, p.duracaoSegundos, p.dificuldade, p.dataHora)
            from Partida p
            where p.resultado = :resultado and p.pontuacao > 0
              and not exists (
                select 1 from Partida outra
                where outra.usuario = p.usuario and outra.resultado = :resultado
                  and (outra.pontuacao > p.pontuacao
                    or (outra.pontuacao = p.pontuacao and outra.duracaoSegundos < p.duracaoSegundos)
                    or (outra.pontuacao = p.pontuacao and outra.duracaoSegundos = p.duracaoSegundos
                        and (outra.dataHora < p.dataHora or (outra.dataHora = p.dataHora and outra.id < p.id))))
              )
            order by p.pontuacao desc, p.duracaoSegundos asc, p.dataHora asc, p.id asc
            """)
    List<PosicaoRanking> melhoresVitorias(@Param("resultado") Resultado resultado, Pageable pagina);
}
