package missaomarte.gdx.recursos;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.EnumMap;

public final class Recursos implements AutoCloseable {
    private final AssetManager assets = new AssetManager();
    private final EnumMap<Imagem, TextureRegion[]> quadros = new EnumMap<>(Imagem.class);
    private final EnumMap<Imagem, Animation<TextureRegion>> animacoes = new EnumMap<>(Imagem.class);
    private final boolean semAudio;
    public Recursos(boolean semAudio) { this.semAudio=semAudio; }
    public void carregar() {
        for(Imagem i:Imagem.values()) assets.load(i.caminho,Texture.class);
        if (!semAudio) {
            for (Som s : Som.values()) {
                if (s.musica) assets.load(s.caminho, Music.class);
                else assets.load(s.caminho, Sound.class);
            }
        }
        assets.finishLoading();
        for(Imagem i:Imagem.values()) {
            Texture textura=assets.get(i.caminho,Texture.class);
            textura.setFilter(Texture.TextureFilter.Linear,Texture.TextureFilter.Linear);
            TextureRegion[] partes=new TextureRegion[i.quadros];
            for(int n=0;n<i.quadros;n++) partes[n]=new TextureRegion(textura,n*i.largura,0,i.largura,i.altura);
            quadros.put(i,partes);
            animacoes.put(i,new Animation<>(i==Imagem.PROPULSOR?.09f:i==Imagem.INIMIGO?.3f:.25f,partes));
        }
        Gdx.app.log("MissaoMarte", "Assets: "+Imagem.values().length+" imagens, "+(semAudio?0:Som.values().length)+" sons carregados");
    }
    public TextureRegion quadro(Imagem i) { return quadros.get(i)[0]; }
    public Animation<TextureRegion> animacao(Imagem i,float segundosPorQuadro) { return animacoes.get(i); }
    public TextureRegion quadro(Imagem i,int indice) {return quadros.get(i)[Math.max(0,Math.min(indice,i.quadros-1))];}
    public Sound efeito(Som s) { return semAudio?null:assets.get(s.caminho,Sound.class); }
    public Music musica() { return semAudio?null:assets.get(Som.MUSICA.caminho,Music.class); }
    public void dispose() { assets.dispose(); }
    @Override public void close() { dispose(); }
}
