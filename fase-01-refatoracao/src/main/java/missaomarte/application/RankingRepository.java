package missaomarte.application;
import java.util.List;
public interface RankingRepository {
    List<RankingEntry> listar();
    void salvar(List<RankingEntry> entradas);
    void limpar();
}
