package missaomarte.application.servidor;

public record Sessao(String nome, String token) {
    @Override public String toString() { return "Sessao[nome=" + nome + "]"; }
}
