package missaomarte.application;
public enum Comando {
    CIMA, BAIXO, ESQUERDA, DIREITA, EMBARCAR, SAIR, INVALIDO;
    public static Comando deTexto(String texto) {
        if (texto == null || texto.isBlank()) return INVALIDO;
        return switch (texto.trim().toLowerCase()) {
            case "w" -> CIMA; case "s" -> BAIXO; case "a" -> ESQUERDA;
            case "d" -> DIREITA; case "c" -> EMBARCAR; case "q" -> SAIR;
            default -> INVALIDO;
        };
    }
}
