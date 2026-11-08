package missaomarte.application;
import missaomarte.domain.Dificuldade;
public record RankingEntry(String nome, int pontos, Dificuldade dificuldade,
                           int passageirosEntregues, String dataHora, long tempoSegundos) {}
