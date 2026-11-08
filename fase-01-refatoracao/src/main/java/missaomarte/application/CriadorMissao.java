package missaomarte.application;
import missaomarte.domain.*;
import java.util.*;
public final class CriadorMissao {
    private final Random random;
    public CriadorMissao(Random random) { this.random = Objects.requireNonNull(random); }
    public Missao criar(Dificuldade dificuldade, int limite) {
        int ocupacoes = dificuldade.quantidadePassageiros() + dificuldade.quantidadeAsteroides() + dificuldade.quantidadeInimigos();
        int celulasDisponiveis = (limite * 2 + 1) * (limite * 2 + 1) - 1;
        if (limite < 1 || ocupacoes > celulasDisponiveis)
            throw new IllegalArgumentException("Mapa pequeno demais para essa dificuldade");
        Set<Posicao> ocupadas = new HashSet<>();
        ocupadas.add(new Posicao(0, 0));
        List<Passageiro> passageiros = new ArrayList<>();
        List<Asteroide> asteroides = new ArrayList<>();
        List<Inimigo> inimigos = new ArrayList<>();
        String[] nomes = {"Dr. Silva", "Eng. Rosa", "Dr. Lima", "Eng. Carlos", "Ast. Maria"};
        for (int i = 0; i < dificuldade.quantidadePassageiros(); i++) {
            Posicao p = livre(limite, ocupadas);
            passageiros.add(switch (i % 5) {
                case 0, 2 -> new Professor(nomes[i % 5], p);
                case 1, 3 -> new Engenheiro(nomes[i % 5], p);
                default -> new Astronauta(nomes[i % 5], p);
            });
        }
        for (int i = 0; i < dificuldade.quantidadeAsteroides(); i++) asteroides.add(new Asteroide(livre(limite, ocupadas)));
        for (int i = 0; i < dificuldade.quantidadeInimigos(); i++) inimigos.add(new Inimigo(livre(limite, ocupadas)));
        return new Missao(new Nave("A-1", 5), passageiros, asteroides, inimigos);
    }
    private Posicao livre(int limite, Set<Posicao> ocupadas) {
        Posicao p;
        do { p = new Posicao(random.nextInt(2 * limite + 1) - limite, random.nextInt(2 * limite + 1) - limite); }
        while (!ocupadas.add(p));
        return p;
    }
}
