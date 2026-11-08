package missaomarte;
import missaomarte.application.*;
import missaomarte.console.*;
import missaomarte.infrastructure.ArquivoRankingRepository;
import java.nio.file.Path;
import java.util.Random;
import java.util.Scanner;
public final class Main {
    public static void main(String[] args) {
        Random random = new Random();
        RankingRepository repository = new ArquivoRankingRepository(Path.of("ranking.json"));
        ConsoleApp app = new ConsoleApp(new RankingService(repository), new CriadorMissao(random), random,
            new MapaRenderer(), System.out);
        try (Scanner scanner = new Scanner(System.in)) { app.executar(scanner); }
    }
}
