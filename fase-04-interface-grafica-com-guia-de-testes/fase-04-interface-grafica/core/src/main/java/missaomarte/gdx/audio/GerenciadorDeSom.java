package missaomarte.gdx.audio;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import missaomarte.gdx.recursos.Recursos;
import missaomarte.gdx.recursos.Som;
public final class GerenciadorDeSom {
    private final Recursos recursos;
    private boolean mudo;
    private boolean pausaFinal;
    public GerenciadorDeSom(Recursos recursos) {this.recursos=recursos;}
    public boolean mudo() {return mudo;}
    public void alternar() {mudo=!mudo;Music m=recursos.musica();if(m!=null) {m.setVolume(mudo?0f:0.22f);}}
    public void tocar(Som tipo) { if(mudo || tipo.musica) return; Sound som=recursos.efeito(tipo);if(som!=null) som.play(tipo==Som.COLISAO?.5f:.27f); }
    public void musicaMenu() { pausaFinal=false;Music m=recursos.musica();if(m!=null) {m.setLooping(true);m.setVolume(mudo?0f:.22f);if(!m.isPlaying())m.play();} }
    public void pausaFinal() { pausaFinal=true;Music m=recursos.musica();if(m!=null)m.pause(); }
}
