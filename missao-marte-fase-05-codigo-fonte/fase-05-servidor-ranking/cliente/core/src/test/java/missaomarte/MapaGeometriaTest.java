package missaomarte;
import missaomarte.gdx.MapaGeometria;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class MapaGeometriaTest {
    @Test void eixoYInvertido() {var g=new MapaGeometria(40,70,20,5);assertEquals(270,g.yCanto(-5));assertEquals(70,g.yCanto(5));}
    @Test void eixoXCrescente() {var g=new MapaGeometria(40,70,20,5);assertEquals(40,g.xCanto(-5));assertEquals(240,g.xCanto(5));}
    @Test void centro() {var g=new MapaGeometria(40,70,20,5);assertEquals(150,g.xCentro(0));assertEquals(180,g.yCentro(0));}
}
