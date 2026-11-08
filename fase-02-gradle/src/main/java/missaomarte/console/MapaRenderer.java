package missaomarte.console;
import missaomarte.application.Partida;
import missaomarte.domain.*;
import java.util.*;
public final class MapaRenderer {
    public String desenhar(Partida partida) {
        Missao missao = partida.getMissao();
        int limite = partida.getLimite();
        List<EntidadeMapa> entidades = missao.entidadesVisiveis();
        Map<Posicao, Character> celulas = new HashMap<>();
        // Em caso de sobreposição durante o jogo, a nave sempre tem prioridade.
        for (EntidadeMapa e : entidades) celulas.put(e.getPosicao(), e.getSimbolo());
        StringBuilder saida = new StringBuilder();
        saida.append("\nMapa - Piloto: ").append(partida.getPiloto()).append(" | Pontos: ").append(partida.getPontos()).append('\n');
        for (int y = -limite; y <= limite; y++) {
            saida.append(String.format("%3d|", y));
            for (int x = -limite; x <= limite; x++)
                saida.append(String.format(" %2c", celulas.getOrDefault(new Posicao(x,y), '.')));
            saida.append('\n');
        }
        // A legenda é derivada das entidades, não de um texto fixo.
        Map<Character,String> legenda = new LinkedHashMap<>();
        for (EntidadeMapa e : entidades) legenda.putIfAbsent(e.getSimbolo(), e.getDescricao());
        saida.append("Legenda: ");
        legenda.forEach((simbolo, descricao) -> saida.append(simbolo).append('=').append(descricao).append("; "));
        saida.append(".=Vazio\n");
        saida.append("Comandos: w/s/a/d (mover), c (embarcar), q (sair)\n");
        Nave nave = missao.getNave();
        saida.append(String.format("Vidas: %d | A bordo: %d/%d | Entregues: %d | Restantes: %d | Total: %d%n",
            nave.getVidas(), nave.getPassageiros().size(), nave.getCapacidade(), missao.getEntregues(),
            missao.getPassageiros().size(), missao.getTotalPassageiros()));
        for (Passageiro p : missao.getPassageiros())
            saida.append(String.format(" - %s (%s) em (%d,%d)%n", p.getNome(), p.getDescricao(), p.getPosicao().x(), p.getPosicao().y()));
        return saida.toString();
    }
}
