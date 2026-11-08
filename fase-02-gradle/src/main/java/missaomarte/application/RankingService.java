package missaomarte.application;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
public final class RankingService {
    private final RankingRepository repository;
    public RankingService(RankingRepository repository) { this.repository = repository; }
    public List<RankingEntry> listar() {
        return repository.listar().stream().sorted(Comparator.comparingInt(RankingEntry::pontos).reversed()).limit(5).toList();
    }
    public boolean registrarVitoria(Partida partida) {
        if (partida.getEstado() != EstadoPartida.VITORIA || partida.getPontos() <= 0) return false;
        List<RankingEntry> atual = listar();
        RankingEntry entrada = new RankingEntry(partida.getPiloto(), partida.getPontos(), partida.getDificuldade(),
            partida.getMissao().getEntregues(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            partida.getTempoSegundos());
        if (atual.size() == 5 && entrada.pontos() <= atual.get(4).pontos()) return false;
        java.util.ArrayList<RankingEntry> nova = new java.util.ArrayList<>(atual);
        nova.add(entrada);
        nova.sort(Comparator.comparingInt(RankingEntry::pontos).reversed());
        repository.salvar(nova.subList(0, Math.min(5, nova.size())));
        return true;
    }
    public void limpar() { repository.limpar(); }
}
