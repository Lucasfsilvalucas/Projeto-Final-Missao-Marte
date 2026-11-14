package missaomarte.application;
import missaomarte.domain.*;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public final class Partida {
    private final Missao missao;
    private final Dificuldade dificuldade;
    private final int limite;
    private final Random random;
    private final String piloto;
    private final long inicio = System.nanoTime();
    private long fim = -1;
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
    public long getTempoSegundos() { return ((fim < 0 ? System.nanoTime() : fim) - inicio) / 1_000_000_000L; }
    public ResultadoTurno executar(Comando comando) {
        if (estado != EstadoPartida.EM_ANDAMENTO) return new ResultadoTurno("Partida encerrada.", estado);
        if (comando == Comando.INVALIDO) return new ResultadoTurno("Comando inválido.", estado);
        if (comando == Comando.SAIR) {
            estado = EstadoPartida.ABORTADA; fim = System.nanoTime();
            return new ResultadoTurno("Missão abortada pelo piloto.", estado);
        }
        String mensagem = "";
        List<EventoTurno> eventos = new ArrayList<>();
        Nave nave = missao.getNave();
        if (comando == Comando.EMBARCAR) {
            Passageiro passageiro = missao.passageiroNaPosicao();
            if (passageiro == null) mensagem = "Nenhum passageiro nesta posição.";
            else if (missao.embarcar(passageiro)) {
                pontos += passageiro.getPontuacao();
                mensagem = passageiro.getNome() + " embarcado! +" + passageiro.getPontuacao() + " pontos.";
                eventos.add(EventoTurno.EMBARQUE);
            } else mensagem = "Nave cheia.";
        } else {
            int dx = 0, dy = 0;
            switch (comando) {
                case CIMA -> dy = -1; case BAIXO -> dy = 1;
                case ESQUERDA -> dx = -1; case DIREITA -> dx = 1;
                default -> throw new IllegalStateException("Comando inesperado");
            }
            boolean moveu = nave.mover(dx, dy, limite);
            movimentos++; pontos--;
            if (moveu) eventos.add(EventoTurno.MOVIMENTO);
        }
        missao.moverInimigos(random, limite);
        if (missao.verificaColisao()) {
            nave.perderVida(); pontos -= 5;
            mensagem += " Colisão! -1 vida e -5 pontos.";
            eventos.add(EventoTurno.COLISAO);
        }
        int entregues = missao.desembarcarNaBase();
        if (entregues > 0) { mensagem += " " + entregues + " passageiro(s) entregues na base."; eventos.add(EventoTurno.ENTREGA); }
        if (nave.getVidas() <= 0 || pontos <= 0) {
            estado = EstadoPartida.DERROTA;
            mensagem += " GAME OVER.";
            eventos.add(EventoTurno.DERROTA);
        } else if (missao.venceu()) {
            estado = EstadoPartida.VITORIA;
            mensagem += " Missão cumprida!";
            eventos.add(EventoTurno.VITORIA);
        }
        if (estado != EstadoPartida.EM_ANDAMENTO && fim < 0) fim = System.nanoTime();
        return new ResultadoTurno(mensagem.strip(), estado, eventos);
    }
}
