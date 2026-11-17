package missaomarte;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import missaomarte.gdx.visual.*;
import missaomarte.gdx.recursos.*;
import missaomarte.gdx.MapaGeometria;

class Fase4LogicaVisualTest {
    @Test void posicaoVisualAvancaSemUltrapassar(){
        PosicaoVisual p=new PosicaoVisual(0,0);p.alvo(3,4);p.atualizar(.1f,10);
        assertEquals(.6f,p.x(),.0001f);assertEquals(.8f,p.y(),.0001f);
        p.atualizar(5,10);assertEquals(3,p.x(),.0001f);assertEquals(4,p.y(),.0001f);
    }
    @Test void novaPosicaoComecaNoDestino(){
        PosicaoVisual p=new PosicaoVisual(4,2);p.atualizar(.1f,12);
        assertEquals(4,p.x());assertEquals(2,p.y());
    }
    @Test void repeticaoRespeitaIntervaloEParada(){
        RepetidorDeTecla r=new RepetidorDeTecla();r.iniciar();
        assertFalse(r.atualizar(.13f));assertTrue(r.atualizar(.02f));
        assertFalse(r.atualizar(.12f));assertTrue(r.atualizar(.03f));
        r.parar();assertFalse(r.atualizar(1));r.iniciar();assertFalse(r.atualizar(.1f));
    }
    @Test void tremorDecaiEPara(){
        Tremor t=new Tremor();t.iniciar(.4f,8);assertEquals(8,t.amplitude());
        t.atualizar(.2f);assertEquals(4,t.amplitude(),.001f);
        t.atualizar(.3f);assertEquals(0,t.amplitude());
    }
    @Test void efeitoTemQuadrosEExpira(){
        EfeitosVisuais fx=new EfeitosVisuais();fx.adicionar(Imagem.EXPLOSAO,2,3,.6f);
        assertEquals(0,fx.listar().get(0).quadro());
        fx.atualizar(.31f);assertTrue(fx.listar().get(0).quadro()>=3);
        fx.atualizar(.31f);assertTrue(fx.listar().isEmpty());
    }
    @Test void textoFlutuaParaCimaEDesaparece(){
        MensagensFlutuantes m=new MensagensFlutuantes();m.adicionar("+10",1,2);
        m.atualizar(.5f);assertTrue(m.listar().get(0).yVisual()<2);
        assertTrue(m.listar().get(0).opacidade()<1);
        m.atualizar(1);assertTrue(m.listar().isEmpty());
    }
    @Test void simbolosPossuemImagem(){
        assertEquals(Imagem.NAVE,SimboloParaImagem.de('@'));
        assertEquals(Imagem.ASTEROIDE,SimboloParaImagem.de('#'));
        assertEquals(Imagem.BASE,SimboloParaImagem.de('L'));
        assertEquals(Imagem.INIMIGO,SimboloParaImagem.de('X'));
        assertEquals(Imagem.PROFESSOR,SimboloParaImagem.de('P'));
        assertEquals(Imagem.ENGENHEIRO,SimboloParaImagem.de('E'));
        assertEquals(Imagem.ASTRONAUTA,SimboloParaImagem.de('T'));
        assertEquals(Imagem.DESCONHECIDO,SimboloParaImagem.de('?'));
    }
    @Test void coordenadaFracionariaInverteEixoY(){
        MapaGeometria g=new MapaGeometria(42,90,35,5);
        assertEquals(42+5.5f*35,g.xCanto(.5f),.01f);
        assertEquals(90+4.5f*35,g.yCanto(.5f),.01f);
    }
}
