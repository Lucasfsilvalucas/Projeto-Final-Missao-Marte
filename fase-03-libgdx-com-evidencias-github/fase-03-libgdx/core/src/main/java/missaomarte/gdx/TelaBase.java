package missaomarte.gdx;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;

abstract class TelaBase implements Screen {
    protected final MissaoMarteGame game;
    protected InputProcessor input;
    protected TelaBase(MissaoMarteGame game) { this.game=game; }
    protected void log(String evento) { Gdx.app.log("MissaoMarte",getClass().getSimpleName()+"."+evento); }
    protected void instalar(InputProcessor input) { this.input=input; Gdx.input.setInputProcessor(input); }
    @Override public void show() { log("show()"); }
    @Override public final void render(float delta) {
        Gdx.gl.glClearColor(.035f,.055f,.095f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.camera.combined);
        game.shapes.setProjectionMatrix(game.camera.combined);
        desenhar(delta);
    }
    protected abstract void desenhar(float delta);
    protected void escrever(String s,float x,float y) {
        game.batch.begin(); game.font.setColor(Color.WHITE); game.font.draw(game.batch,s,x,y); game.batch.end();
    }
    protected void centralizar(String s,float y) {
        GlyphLayout gl=new GlyphLayout(game.font,s);
        escrever(s,400-gl.width/2,y);
    }
    protected void retangulo(float x,float y,float w,float h,Color cor) {
        game.shapes.begin(ShapeRenderer.ShapeType.Filled);
        game.shapes.setColor(cor);game.shapes.rect(x,y,w,h);game.shapes.end();
    }
    @Override public void resize(int w,int h) { log("resize("+w+","+h+")");game.viewport.update(w,h,true); }
    @Override public void pause() { log("pause()"); }
    @Override public void resume() { log("resume()"); }
    @Override public void hide() { log("hide()");if(Gdx.input.getInputProcessor()==input) Gdx.input.setInputProcessor(null); }
    @Override public void dispose() { log("dispose()"); }
}
