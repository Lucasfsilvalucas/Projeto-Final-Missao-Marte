package missaomarte.application.servidor;

public record Resposta<T>(T valor, String erro, boolean naoAutorizada) {
    public boolean sucesso() { return erro == null; }
    public static <T> Resposta<T> sucesso(T valor) { return new Resposta<>(valor, null, false); }
    public static <T> Resposta<T> erro(String erro, boolean naoAutorizada) {
        return new Resposta<>(null, erro, naoAutorizada);
    }
}
