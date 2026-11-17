package missaomarte.gdx.recursos;
public final class SimboloParaImagem {
    private SimboloParaImagem() {}
    public static Imagem de(char simbolo) {
        return switch(simbolo) {
            case '@' -> Imagem.NAVE; case 'L' -> Imagem.BASE; case '#' -> Imagem.ASTEROIDE;
            case 'X' -> Imagem.INIMIGO; case 'P' -> Imagem.PROFESSOR;
            case 'E' -> Imagem.ENGENHEIRO; case 'T' -> Imagem.ASTRONAUTA;
            default -> Imagem.DESCONHECIDO;
        };
    }
}
