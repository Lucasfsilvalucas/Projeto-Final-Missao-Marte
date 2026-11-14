package missaomarte.gdx.visual;
import missaomarte.domain.EntidadeMapa;
import java.util.IdentityHashMap;
import java.util.Collection;
import java.util.Map;
public final class PosicoesVisuais {
    private final Map<EntidadeMapa,PosicaoVisual> posicoes=new IdentityHashMap<>();
    public void sincronizar(Collection<? extends EntidadeMapa> atuais,float delta) {
        Map<EntidadeMapa,Boolean> presentes=new IdentityHashMap<>();
        for(EntidadeMapa e:atuais){
            presentes.put(e,true);
            var p=e.getPosicao();
            PosicaoVisual v=posicoes.computeIfAbsent(e,k->new PosicaoVisual(p.x(),p.y()));
            v.alvo(p.x(),p.y());v.atualizar(delta,12f);
        }
        posicoes.keySet().removeIf(e->!presentes.containsKey(e));
    }
    public PosicaoVisual obter(EntidadeMapa e) {return posicoes.get(e);}
    public int tamanho(){return posicoes.size();}
}
