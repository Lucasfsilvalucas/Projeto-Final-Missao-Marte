package missaomarte.gdx.visual;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import missaomarte.domain.*;
import missaomarte.gdx.MapaGeometria;
import missaomarte.gdx.recursos.*;

public final class MundoRenderer {
    public static final float X = 18, Y = 100, TAMANHO = 418;
    private final Recursos recursos;
    private final PosicoesVisuais posicoes = new PosicoesVisuais();
    private final MapaGeometria geo;
    private final float celula;
    private float tempo;

    public MundoRenderer(Recursos recursos, int limite) {
        this.recursos = recursos;
        celula = TAMANHO / (2 * limite + 1);
        geo = new MapaGeometria(X, Y, celula, limite);
    }

    public void desenhar(SpriteBatch batch, Missao missao, float delta,
                        int orientacao, int quadroChama, float piscar, EfeitosVisuais efeitos) {
        tempo += delta;
        var objetos = missao.entidadesVisiveis();
        posicoes.sincronizar(objetos, delta);
        PosicaoVisual naveVisual = posicoes.obter(missao.getNave());
        batch.draw(recursos.quadro(Imagem.FUNDO_MAPA), X, Y, TAMANHO, TAMANHO);

        Posicao base = missao.getBase().getPosicao();
        float anel = celula * 2.7f;
        batch.draw(recursos.quadro(Imagem.ANEL_POUSO), geo.xCentro(base.x()) - anel / 2,
                geo.yCentro(base.y()) - (anel + celula) / 2, anel, anel);
        for (EntidadeMapa e : objetos) {
            if (e instanceof Nave || e instanceof Base) continue;
            PosicaoVisual v = posicoes.obter(e);
            Imagem imagem = SimboloParaImagem.de(e.getSimbolo());
            TextureRegion quadro = imagem == Imagem.INIMIGO
                ? recursos.animacao(imagem, .3f).getKeyFrame(tempo, true) : recursos.quadro(imagem);
            batch.draw(quadro, geo.xCanto(v.x()), geo.yCanto(v.y()), celula, celula);
        }

        if (!(piscar > 0 && ((int)(piscar * 12)) % 2 == 0)) {
            float x = geo.xCentro(naveVisual.x()), y = geo.yCentro(naveVisual.y());
            if (naveVisual.emMovimento()) {
                float tamanho = celula / 3;
                float cx = x + MathUtils.sinDeg(orientacao) * 1.27f * celula;
                float cy = y - MathUtils.cosDeg(orientacao) * 1.27f * celula;
                batch.draw(recursos.quadro(Imagem.PROPULSOR_MOVIMENTO, quadroChama),
                    cx - tamanho / 2, cy - tamanho, tamanho / 2, tamanho,
                    tamanho, tamanho, 1, 1, orientacao);
            }
            batch.draw(recursos.quadro(Imagem.NAVE_BASICA), x - celula / 2, y - 1.5f * celula,
                celula / 2, 1.5f * celula, celula, 2 * celula, 1, 1, orientacao);
        }

        for (EfeitosVisuais.Efeito e : efeitos.listar()) {
            batch.draw(recursos.quadro(e.imagem, e.quadro()),
                geo.xCanto(e.x), geo.yCanto(e.y), celula, celula);
        }
    }

    public MapaGeometria geometria() { return geo; }
}
