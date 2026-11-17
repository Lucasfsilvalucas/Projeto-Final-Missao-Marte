package missaomarte.application;
import missaomarte.domain.*;
import java.util.*;
public final class CriadorMissao {
    private final Random random;
    public CriadorMissao(Random random) { this.random = Objects.requireNonNull(random); }
    public Missao criar(Dificuldade dificuldade, int limite) {
        int ocupacoes = dificuldade.quantidadePassageiros() + dificuldade.quantidadeAsteroides() + dificuldade.quantidadeInimigos();
        int celulasDisponiveis = (limite * 2 + 1) * (limite * 2 + 1) - 2;
        if (limite < 1 || ocupacoes > celulasDisponiveis)
            throw new IllegalArgumentException("Mapa pequeno demais para essa dificuldade");
        Set<Posicao> ocupadas = new HashSet<>();
        Nave nave = new Nave("A-1", 5);
        ocupadas.add(nave.getPosicao());
        ocupadas.add(nave.getCauda());
        List<Passageiro> passageiros = new ArrayList<>();
        List<Asteroide> asteroides = new ArrayList<>();
        List<Inimigo> inimigos = new ArrayList<>();
        passageiros.add(new Professor("Dr. Silva", livre(limite, ocupadas)));
        for (String nome : List.of("Eng. Rosa", "Eng. Carlos", "Eng. Ana", "Eng. Bruno", "Eng. Lima")) {
            passageiros.add(new Engenheiro(nome, livre(limite, ocupadas)));
        }
        for (String nome : List.of("Ast. Maria", "Ast. Clara", "Ast. Pedro", "Ast. Rafael")) {
            passageiros.add(new Astronauta(nome, livre(limite, ocupadas)));
        }
        for (int i = 0; i < dificuldade.quantidadeAsteroides(); i++) asteroides.add(new Asteroide(livre(limite, ocupadas)));
        for (int i = 0; i < dificuldade.quantidadeInimigos(); i++) inimigos.add(new Inimigo(livre(limite, ocupadas)));
        return new Missao(nave, passageiros, asteroides, inimigos);
    }
    private Posicao livre(int limite, Set<Posicao> ocupadas) {
        Posicao p;
        do { p = new Posicao(random.nextInt(2 * limite + 1) - limite, random.nextInt(2 * limite + 1) - limite); }
        while (!ocupadas.add(p));
        return p;
    }
}
