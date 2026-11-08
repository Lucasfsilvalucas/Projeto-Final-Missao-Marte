package missaomarte.console;
import missaomarte.application.*;
import missaomarte.domain.Dificuldade;
import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;
public final class ConsoleApp {
    private final RankingService ranking;
    private final CriadorMissao criador;
    private final Random random;
    private final MapaRenderer renderer;
    private final PrintStream out;
    public ConsoleApp(RankingService ranking, CriadorMissao criador, Random random, MapaRenderer renderer, PrintStream out) {
        this.ranking = ranking; this.criador = criador; this.random = random; this.renderer = renderer; this.out = out;
    }
    private String ler(Scanner scanner, String prompt, String padrao) {
        out.print(prompt);
        if (!scanner.hasNextLine()) return null;
        String l = scanner.nextLine().trim(); return l.isEmpty() ? padrao : l;
    }
    public void executar(Scanner scanner) {
        out.println("MISSÃO MARTE UNIFOR - VERSÃO REFATORADA");
        while (true) {
            String opcao = ler(scanner, "\n1. Nova missão\n2. Ranking Top 5\n3. Limpar ranking\n4. Sair\nOpção: ", "1");
            if (opcao == null || opcao.equals("4")) { out.println("Obrigado por jogar!"); return; }
            switch (opcao) {
                case "1" -> jogar(scanner);
                case "2" -> mostrarRanking();
                case "3" -> {
                    String confirma = ler(scanner, "Confirma limpar ranking? (s/n): ", "n");
                    if (confirma == null) return;
                    if (confirma.equalsIgnoreCase("s") || confirma.equalsIgnoreCase("sim")) {
                        ranking.limpar(); out.println("Ranking limpo.");
                    } else out.println("Cancelado.");
                }
                default -> out.println("Opção inválida.");
            }
        }
    }
    private void mostrarRanking() {
        var entradas = ranking.listar();
        if (entradas.isEmpty()) { out.println("Ranking vazio."); return; }
        for (int i = 0; i < entradas.size(); i++) {
            var e = entradas.get(i);
            out.printf("%d. %s - %d pontos | %s | Entregues: %d | %ds%n", i+1, e.nome(), e.pontos(),
                e.dificuldade(), e.passageirosEntregues(), e.tempoSegundos());
        }
    }
    private void jogar(Scanner scanner) {
        String nome = ler(scanner, "Nome do piloto: ", "Piloto Anônimo"); if (nome == null) return;
        String dificuldadeTxt = ler(scanner, "Dificuldade (facil/medio/dificil): ", "medio"); if (dificuldadeTxt == null) return;
        Dificuldade dificuldade = Dificuldade.deString(dificuldadeTxt);
        String mapaTxt = ler(scanner, "Tamanho do mapa (padrão 5): ", "5"); if (mapaTxt == null) return;
        int limite = 5;
        try { limite = Integer.parseInt(mapaTxt); } catch (NumberFormatException ignorado) { out.println("Tamanho inválido, usando 5."); }
        if (limite < 2) { out.println("Mapa pequeno demais, usando tamanho mínimo 2."); limite = 2; }
        if (limite > 100) { out.println("Mapa muito grande, usando 100."); limite = 100; }
        String inicio = ler(scanner, "Pressione Enter para decolar: ", ""); if (inicio == null) return;
        Partida partida = new Partida(criador.criar(dificuldade, limite), dificuldade, limite, nome, random);
        while (partida.getEstado() == EstadoPartida.EM_ANDAMENTO) {
            out.print(renderer.desenhar(partida));
            String texto = ler(scanner, "Comando: ", "");
            if (texto == null) { out.println("Entrada encerrada; missão abortada."); return; }
            ResultadoTurno resultado = partida.executar(Comando.deTexto(texto));
            if (!resultado.mensagem().isEmpty()) out.println(resultado.mensagem());
        }
        if (partida.getEstado() == EstadoPartida.VITORIA) {
            out.printf("Pontos: %d | Movimentos: %d | Tempo: %ds | Entregues: %d%n", partida.getPontos(),
                partida.getMovimentos(), partida.getTempoSegundos(), partida.getMissao().getEntregues());
            if (ranking.registrarVitoria(partida)) out.println("Entrou para o Top 5!");
        }
    }
}
