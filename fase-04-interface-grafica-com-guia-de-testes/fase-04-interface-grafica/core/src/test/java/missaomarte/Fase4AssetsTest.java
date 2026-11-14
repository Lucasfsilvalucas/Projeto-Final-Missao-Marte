package missaomarte;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import missaomarte.gdx.recursos.*;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import java.nio.file.*;

class Fase4AssetsTest {
    private static Path asset(String nome){
        Path p=Path.of("core","assets",nome);
        if(!Files.exists(p))p=Path.of("assets",nome);
        if(!Files.exists(p))p=Path.of("..","core","assets",nome);
        return p;
    }
    @Test void pngExistemEQuadrosTemDimensoesValidas()throws Exception{
        for(Imagem i:Imagem.values()){
            var arquivo=asset(i.caminho);assertTrue(Files.exists(arquivo),i.caminho);
            var img=ImageIO.read(arquivo.toFile());assertNotNull(img,i.caminho);
            assertEquals(i.largura*i.quadros,img.getWidth(),i.caminho);
            assertEquals(i.altura,img.getHeight(),i.caminho);
        }
    }
    @Test void wavExistemEFormatosValidos()throws Exception{
        int trilhas=0;
        for(Som som:Som.values()){
            var arquivo=asset(som.caminho);assertTrue(Files.exists(arquivo),som.caminho);
            try(var audio=AudioSystem.getAudioInputStream(arquivo.toFile())){
                assertTrue(audio.getFrameLength()>0);
                assertTrue(audio.getFormat().getSampleRate()>0);
            }
            if(som.musica)trilhas++;
        }
        assertEquals(1,trilhas);
    }
}
