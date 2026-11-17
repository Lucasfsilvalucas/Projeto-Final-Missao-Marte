package missaomarte.gdx;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputAdapter;
import missaomarte.gdx.recursos.Imagem;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

abstract class TelaBase implements Screen {
    protected final MissaoMarteGame game;
    protected InputProcessor input;
    protected TelaBase(MissaoMarteGame game) { this.game=game; }
    protected void log(String evento) { Gdx.app.log("MissaoMarte",getClass().getSimpleName()+"."+evento); }
    protected void instalar(InputProcessor input) {
        if(this instanceof NovaPartidaScreen) this.input=input;
        else this.input=new InputMultiplexer(new InputAdapter() {
            @Override public boolean keyDown(int key) {
                if(key==Input.Keys.M){game.sons.alternar();return true;}return false;
            }
        },input);
        Gdx.input.setInputProcessor(this.input);
    }
    @Override public void show() { log("show()"); }
    @Override public final void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.camera.combined);
        game.shapes.setProjectionMatrix(game.camera.combined);
        game.batch.begin();
        game.batch.draw(game.recursos.quadro(Imagem.FUNDO),0,0,800,600);
        game.batch.end();
        desenhar(delta);
    }
    protected abstract void desenhar(float delta);
    protected void escrever(String s,float x,float y) {
        game.batch.begin(); game.font.draw(game.batch,s,x,y); game.batch.end();
    }
    protected void centralizar(String s,float y) {
        GlyphLayout gl=new GlyphLayout(game.font,s);
        escrever(s,400-gl.width/2,y);
    }
    @Override public void resize(int w,int h) { log("resize("+w+","+h+")");game.viewport.update(w,h,true); }
    @Override public void pause() { log("pause()"); }
    @Override public void resume() { log("resume()"); }
    @Override public void hide() { log("hide()");if(Gdx.input.getInputProcessor()==input) Gdx.input.setInputProcessor(null); }
    @Override public void dispose() { log("dispose()"); }
}
