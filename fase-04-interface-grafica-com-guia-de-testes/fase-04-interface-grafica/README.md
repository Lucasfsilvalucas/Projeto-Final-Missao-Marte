# Missão Marte — Fase 04: Interface gráfica

Evolução da Fase 3 para um jogo desktop com **sprites, animações, sons, movimentação visual suave e HUD**, com movimento do jogador por casas e patrulha inimiga em tempo real.

## Requisitos

- JDK 17 no `JAVA_HOME` e `PATH`;
- OpenGL 2.0+;
- Gradle Wrapper 8.7 (não é preciso instalar o Gradle separadamente).

**Guia para colegas de equipe:** [Como compilar e testar a Fase 4](docs/guia-de-testes-para-equipe.md).

## Executar no Windows

Abra o PowerShell **dentro da pasta `fase-04-interface-grafica`**:

```powershell
.\gradlew.bat --version
.\gradlew.bat clean build
.\gradlew.bat :lwjgl3:run --console=plain
```

Para reproduzir um cenário com semente fixa:

```powershell
.\gradlew.bat :lwjgl3:run -Dmissaomarte.semente=4 --console=plain
```

Sem dispositivo de áudio:

```powershell
.\gradlew.bat :lwjgl3:run -Dmissaomarte.semaudio=true --console=plain
```

As propriedades são repassadas ao launcher por `lwjgl3/build.gradle`. O `ranking.json` é salvo na raiz desta fase e está ignorado pelo Git.

## Controles

| Tela | Teclas |
| --- | --- |
| Menu | `1` Jogar, `2` ranking, `3` limpar, `4` sair |
| Nova partida | Digitar nome, `Backspace`, setas para dificuldade, `Enter` iniciar, `Esc` voltar |
| Partida | `W/A/S/D` ou setas; segure para repetir; `E` embarcar; `Q/Esc` abortar |
| Fim de partida | `Enter` avançar |
| Outras telas | `M` silenciar/reativar (exceto entrada do nome) |

## Assets

O mapa básico tem **19 × 19 casas**, com fundo estrelado, exibidas por completo durante a partida. A grade serve para movimento e colisões. A nave do jogador ocupa duas casas, com o bico como posição de referência para embarque e entrega. A propulsão alterna duas chamas a cada movimento concluído. A patrulha inimiga começa após a primeira saída da base.

Os sprites personalizados e os oito sons estão em `core/assets/`. Os catálogos ficam em `gdx/recursos`. O script `tools/GeradorDeAssets.java` gera os recursos provisórios da versão original e pode sobrescrever arquivos de mesmo nome.

## Organização

- `core`: domínio, aplicação, persistência, telas e lógica gráfica;
- `core/assets`: sprites e WAV;
- `lwjgl3`: inicializador desktop;
- `docs/tutorial.md`: arquitetura e implementação;
- [Evidências dos testes no Windows](docs/evidencias.md): relatório e capturas da Fase 4;
- `docs/creditos.md`: autoria e licença dos recursos.

**Validação no Windows.** O build passou, o jogo foi executado e a vitória no Médio e a limpeza do ranking foram registradas no [relatório de evidências](docs/evidencias.md).
