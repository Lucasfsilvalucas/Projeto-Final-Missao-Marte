package missaomarte.servidor.ranking;

import java.time.Instant;
import missaomarte.servidor.partida.Dificuldade;

public record PosicaoRanking(String nome, int pontuacao, int duracaoSegundos, Dificuldade dificuldade, Instant dataHora) {}
