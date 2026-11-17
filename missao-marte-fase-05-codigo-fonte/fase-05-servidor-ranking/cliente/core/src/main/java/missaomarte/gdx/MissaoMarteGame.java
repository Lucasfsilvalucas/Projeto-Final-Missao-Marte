package missaomarte.gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import missaomarte.application.CriadorMissao;
import missaomarte.application.RankingService;
import java.util.Random;
import missaomarte.application.servidor.ServidorDoJogo;
import missaomarte.application.servidor.Sessao;
import missaomarte.gdx.recursos.Recursos;
import missaomarte.gdx.audio.GerenciadorDeSom;

public final class MissaoMarteGame extends Game {
    static final Color COR_TEXTO = new Color(0xffe81fff);
    public final CriadorMissao criador;
    public final RankingService ranking;
    public final Random random;
    public OrthographicCamera camera;
    public FitViewport viewport;
    public SpriteBatch batch;
    public ShapeRenderer shapes;
    public BitmapFont font;
    public Recursos recursos;
    public GerenciadorDeSom sons;
    private final boolean semAudio;
    final ServidorDoJogo servidor;
    Sessao sessao;
    public MissaoMarteGame(CriadorMissao criador, RankingService ranking, Random random, boolean semAudio,
                          ServidorDoJogo servidor) {
        this.servidor = servidor;
        this.criador=criador; this.ranking=ranking; this.random=random; this.semAudio=semAudio;
    }
    @Override public void create() {
        Gdx.app.log("MissaoMarte", "create()");
        camera = new OrthographicCamera();
        viewport = new FitViewport(800,600,camera);
        batch = new SpriteBatch();
        shapes = new ShapeRenderer();
        font = new BitmapFont();
        font.setColor(COR_TEXTO);
        font.getData().setScale(1.2f);
        recursos=new Recursos(semAudio);recursos.carregar();sons=new GerenciadorDeSom(recursos);
        menu();
    }
    public void menu() { menu(""); }
    void menu(String aviso) { sons.musicaMenu();setScreen(new MenuScreen(this, aviso)); }
    void expirarSessao() {
        sessao = null;
        menu("Sessão expirada. Entre novamente.");
    }
    public void novaPartida() { setScreen(new NovaPartidaScreen(this)); }
    public void ranking() {
        if (sessao == null) setScreen(new RankingScreen(this, null, false));
        else setScreen(new ResultadosOnlineScreen(this, false, false));
    }
    @Override public void dispose() {
        Gdx.app.log("MissaoMarte", "dispose()");
        if (getScreen()!=null) { getScreen().hide(); getScreen().dispose(); }
        batch.dispose(); shapes.dispose(); font.dispose();recursos.dispose();
        super.dispose();
    }
}
