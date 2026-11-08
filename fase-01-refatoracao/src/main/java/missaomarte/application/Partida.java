package missaomarte.application;
import missaomarte.domain.*;
import java.util.Objects;
import java.util.Random;
public final class Partida {
    private final Missao missao;
    private final Dificuldade dificuldade;
    private final int limite;
    private final Random random;
    private final String piloto;
    private final long inicio = System.nanoTime();
    private int pontos, movimentos;
    private EstadoPartida estado = EstadoPartida.EM_ANDAMENTO;
    public Partida(Missao missao, Dificuldade dificuldade, int limite, String piloto, Random random) {
        this.missao = Objects.requireNonNull(missao); this.dificuldade = dificuldade;
        this.limite = limite; this.piloto = piloto; this.random = Objects.requireNonNull(random);
        this.pontos = dificuldade.pontosIniciais();
    }
    public Missao getMissao() { return missao; }
    public String getPiloto() { return piloto; }
    public Dificuldade getDificuldade() { return dificuldade; }
    public int getLimite() { return limite; }
    public int getPontos() { return pontos; }
    public int getMovimentos() { return movimentos; }
    public EstadoPartida getEstado() { return estado; }
    public long getTempoSegundos() { return (System.nanoTime() - inicio) / 1_000_000_000L; }
    public ResultadoTurno executar(Comando comando) {
        if (estado != EstadoPartida.EM_ANDAMENTO) return new ResultadoTurno("Partida encerrada.", estado);
        if (comando == Comando.INVALIDO) return new ResultadoTurno("Comando inválido.", estado);
        if (comando == Comando.SAIR) {
            estado = EstadoPartida.ABORTADA;
            return new ResultadoTurno("Missão abortada pelo piloto.", estado);
        }
        String mensagem = "";
        Nave nave = missao.getNave();
        if (comando == Comando.EMBARCAR) {
            Passageiro passageiro = missao.passageiroNaPosicao();
            if (passageiro == null) mensagem = "Nenhum passageiro nesta posição.";
            else if (missao.embarcar(passageiro)) {
                pontos += passageiro.getPontuacao();
                mensagem = passageiro.getNome() + " embarcado! +" + passageiro.getPontuacao() + " pontos.";
            } else mensagem = "Nave cheia.";
        } else {
            int dx = 0, dy = 0;
            switch (comando) {
                case CIMA -> dy = -1; case BAIXO -> dy = 1;
                case ESQUERDA -> dx = -1; case DIREITA -> dx = 1;
                default -> throw new IllegalStateException("Comando inesperado");
            }
            nave.mover(dx, dy, limite);
            movimentos++; pontos--;
        }
        missao.moverInimigos(random, limite);
        if (missao.verificaColisao()) {
            nave.perderVida(); pontos -= 5;
            mensagem += " Colisão! -1 vida e -5 pontos.";
        }
        int entregues = missao.desembarcarNaBase();
        if (entregues > 0) mensagem += " " + entregues + " passageiro(s) entregues na base.";
        if (nave.getVidas() <= 0 || pontos <= 0) {
            estado = EstadoPartida.DERROTA;
            mensagem += " GAME OVER.";
        } else if (missao.venceu()) {
            estado = EstadoPartida.VITORIA;
            mensagem += " Missão cumprida!";
        }
        return new ResultadoTurno(mensagem.strip(), estado);
    }
}
