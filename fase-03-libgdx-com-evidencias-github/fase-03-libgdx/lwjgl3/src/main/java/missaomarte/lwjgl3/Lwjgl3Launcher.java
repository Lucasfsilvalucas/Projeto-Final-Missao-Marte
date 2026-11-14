package missaomarte.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import missaomarte.application.CriadorMissao;
import missaomarte.application.RankingService;
import missaomarte.gdx.MissaoMarteGame;
import missaomarte.infrastructure.ArquivoRankingRepository;
import java.nio.file.Paths;
import java.util.Random;

public final class Lwjgl3Launcher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("Missao Marte Unifor - libGDX");
        cfg.setWindowedMode(800, 600);
        cfg.useVsync(true);
        cfg.setForegroundFPS(60);
        Random random = new Random();
        new Lwjgl3Application(new MissaoMarteGame(new CriadorMissao(random),
            new RankingService(new ArquivoRankingRepository(Paths.get("ranking.json"))), random), cfg);
    }
}
