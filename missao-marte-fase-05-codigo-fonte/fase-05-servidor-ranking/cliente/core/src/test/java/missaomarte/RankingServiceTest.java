package missaomarte;
import missaomarte.application.*;
import missaomarte.domain.*;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
class RankingServiceTest {
    static class Memoria implements RankingRepository {
        List<RankingEntry> items=new ArrayList<>();
        public List<RankingEntry> listar(){return new ArrayList<>(items);}
        public void salvar(List<RankingEntry> itens){items=new ArrayList<>(itens);}
        public void limpar(){items.clear();}
    }
    private static Partida vitoria(int bonus) {
        Partida p=new Partida(new Missao(new Nave("A",5),List.of(new Professor("P",new Posicao(1,0))),List.of(),List.of()),Dificuldade.FACIL,5,"P"+bonus,new Random(0));
        p.executar(Comando.DIREITA);p.executar(Comando.EMBARCAR);p.executar(Comando.ESQUERDA);
        assertEquals(EstadoPartida.VITORIA,p.getEstado());return p;
    }
    @Test void registraVitoria() {var r=new RankingService(new Memoria());assertTrue(r.registrarVitoria(vitoria(1)));assertEquals(1,r.listar().size());}
    @Test void naoRegistraDerrota() {var r=new RankingService(new Memoria());var p=new Partida(new Missao(new Nave("A",5),List.of(),List.of(),List.of()),Dificuldade.FACIL,5,"P",new Random());assertFalse(r.registrarVitoria(p));}
    @Test void ordenaECortaCinco() {
        var memoria=new Memoria();var r=new RankingService(memoria);
        for(int i=0;i<7;i++) memoria.items.add(new RankingEntry("P"+i,10+i,Dificuldade.FACIL,1,"",0));
        assertEquals(5,r.listar().size());assertEquals(16,r.listar().get(0).pontos());
    }
    @Test void naoSubstituiEmpateNoUltimo() {
        var memoria = new Memoria();
        var ranking = new RankingService(memoria);
        var tentativa = vitoria(99);
        int pontuacaoEmpatada = tentativa.getPontos();
        for (int i = 0; i < 5; i++) {
            memoria.items.add(new RankingEntry("P" + i, pontuacaoEmpatada,
                Dificuldade.FACIL, 1, "", 0));
        }
        var antes = ranking.listar();
        assertFalse(ranking.registrarVitoria(tentativa));
        assertEquals(antes, ranking.listar());
    }
    @Test void limpa() {var r=new RankingService(new Memoria());r.registrarVitoria(vitoria(1));r.limpar();assertTrue(r.listar().isEmpty());}
}
