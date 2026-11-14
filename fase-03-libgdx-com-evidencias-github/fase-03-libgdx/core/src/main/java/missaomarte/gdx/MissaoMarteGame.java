package missaomarte.gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import missaomarte.application.CriadorMissao;
import missaomarte.application.RankingService;
import java.util.Random;

public final class MissaoMarteGame extends Game {
    public final CriadorMissao criador;
    public final RankingService ranking;
    public final Random random;
    public OrthographicCamera camera;
    public FitViewport viewport;
    public SpriteBatch batch;
    public ShapeRenderer shapes;
    public BitmapFont font;
    public MissaoMarteGame(CriadorMissao criador, RankingService ranking, Random random) {
        this.criador=criador; this.ranking=ranking; this.random=random;
    }
    @Override public void create() {
        Gdx.app.log("MissaoMarte", "create()");
        camera = new OrthographicCamera();
        viewport = new FitViewport(800,600,camera);
        batch = new SpriteBatch();
        shapes = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(1.2f);
        setScreen(new MenuScreen(this));
    }
    public void menu() { setScreen(new MenuScreen(this)); }
    public void novaPartida() { setScreen(new NovaPartidaScreen(this)); }
    public void ranking() { setScreen(new RankingScreen(this, null, false)); }
    @Override public void dispose() {
        Gdx.app.log("MissaoMarte", "dispose()");
        if (getScreen()!=null) { getScreen().hide(); getScreen().dispose(); }
        batch.dispose(); shapes.dispose(); font.dispose();
        super.dispose();
    }
}
