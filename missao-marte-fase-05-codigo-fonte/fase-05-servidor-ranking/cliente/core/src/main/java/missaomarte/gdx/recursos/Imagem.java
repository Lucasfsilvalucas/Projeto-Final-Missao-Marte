package missaomarte.gdx.recursos;
public enum Imagem {
    FUNDO("fundo",800,600,1), TERRENO("terreno",48,48,1), NAVE("nave",48,48,1),
    FUNDO_MAPA("fundo_mapa_basico",798,798,1), NAVE_BASICA("nave_basica",48,96,1),
    PROPULSOR_MOVIMENTO("propulsor_movimento",16,16,2), ANEL_POUSO("base_pouso_anel",128,128,1),
    PROPULSOR("propulsor",48,48,4), BASE("base",48,48,4), ASTEROIDE("asteroide",48,48,1),
    INIMIGO("inimigo",48,48,2), PROFESSOR("professor",48,48,1), ENGENHEIRO("engenheiro",48,48,1),
    ASTRONAUTA("astronauta",48,48,1), DESCONHECIDO("desconhecido",48,48,1),
    EXPLOSAO("explosao",48,48,6), BRILHO("brilho",48,48,5),
    VIDA_CHEIA("vida_cheia",24,24,1), VIDA_VAZIA("vida_vazia",24,24,1),
    VAGA_OCUPADA("vaga_ocupada",24,24,1), VAGA_LIVRE("vaga_livre",24,24,1);
    public final String caminho; public final int largura, altura, quadros;
    Imagem(String nome,int largura,int altura,int quadros) { this.caminho="imagens/"+nome+".png";this.largura=largura;this.altura=altura;this.quadros=quadros; }
}
