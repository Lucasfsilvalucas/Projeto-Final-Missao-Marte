package missaomarte;
import missaomarte.domain.*;
import missaomarte.application.*;
import missaomarte.console.MapaRenderer;
import missaomarte.infrastructure.ArquivoRankingRepository;
import java.nio.file.*;
import java.util.*;

public final class TestesRegressao {
    private static int aprovados;
    private static void verificar(boolean condicao, String teste) {
        if (!condicao) throw new AssertionError(teste);
        aprovados++;
        System.out.println("OK: " + teste);
    }
    private static Random parado() {
        return new Random(1) { @Override public int nextInt(int limite) { return 0; } };
    }
    private static Partida umaPartida(List<Passageiro> passageiros, List<Asteroide> asteroides, List<Inimigo> inimigos, int limite) {
        return new Partida(new Missao(new Nave("A-1", 5), passageiros, asteroides, inimigos),
            Dificuldade.FACIL, limite, "Tester", parado());
    }
    public static void main(String[] args) throws Exception {
        for (Dificuldade d : Dificuldade.values()) {
            Missao m = new CriadorMissao(new Random(12345)).criar(d, 5);
            verificar(m.getTotalPassageiros() == d.quantidadePassageiros(), "Quantidade em " + d);
            verificar(m.indicadoresConsistentes(), "Indicadores em " + d);
            Set<Posicao> ocupadas = new HashSet<>();
            for (EntidadeMapa e : m.entidadesVisiveis()) {
                if (e instanceof Nave) { verificar(e.getPosicao().equals(m.getBase().getPosicao()), "Nave inicia na base " + d); continue; }
                verificar(ocupadas.add(e.getPosicao()), "Posição inicial exclusiva " + d + " " + e.getDescricao());
            }
        }
        verificar(Comando.deTexto("W") == Comando.CIMA && Comando.deTexto("z") == Comando.INVALIDO, "Comandos válidos/inválidos");
        Partida p = umaPartida(List.of(new Professor("P", new Posicao(1,0))), List.of(), List.of(), 5);
        verificar(p.executar(Comando.EMBARCAR).mensagem().contains("Nenhum passageiro"), "Embarque em célula vazia");
        p.executar(Comando.DIREITA);
        verificar(p.executar(Comando.EMBARCAR).mensagem().contains("embarcado"), "Embarque na célula com passageiro");
        verificar(p.getMissao().indicadoresConsistentes(), "Conservação de passageiros após embarque");
        verificar(p.getEstado() == EstadoPartida.EM_ANDAMENTO, "Ainda não venceu só por embarcar");
        p.executar(Comando.ESQUERDA);
        verificar(p.getMissao().getEntregues() == 1 && p.getMissao().getNave().getPassageiros().isEmpty(), "Desembarque libera capacidade");
        verificar(p.getEstado() == EstadoPartida.VITORIA, "Vitória somente após entrega");
        verificar(p.getMissao().indicadoresConsistentes(), "Conservação de passageiros após entrega");
        Partida choque = umaPartida(List.of(new Professor("P",new Posicao(2,2))), List.of(new Asteroide(new Posicao(0,-1))), List.of(), 5);
        int antes = choque.getPontos();
        choque.executar(Comando.CIMA);
        verificar(choque.getMissao().getNave().getVidas() == 2, "Colisão diminui vida");
        verificar(choque.getPontos() == antes - 6, "Colisão diminui pontos além do movimento");
        verificar(umaPartida(List.of(new Professor("P",new Posicao(1,1))), List.of(), List.of(), 5)
            .executar(Comando.SAIR).estado() == EstadoPartida.ABORTADA, "Comando Q aborta a partida");
        verificar(new MapaRenderer().desenhar(p).contains("Entregues: 1"), "Interface mostra indicadores de entrega");
        Path temporario = Files.createTempFile("marte-ranking-", ".json");
        try {
            RankingService ranking = new RankingService(new ArquivoRankingRepository(temporario));
            verificar(ranking.registrarVitoria(p), "Vitória registrada");
            verificar(ranking.listar().size() == 1 && ranking.listar().get(0).passageirosEntregues() == 1, "Consulta de ranking JSON");
            ranking.limpar();
            verificar(ranking.listar().isEmpty(), "Limpeza do ranking");
        } finally { Files.deleteIfExists(temporario); }
        System.out.println("TOTAL: " + aprovados + " verificações aprovadas.");
    }
}
