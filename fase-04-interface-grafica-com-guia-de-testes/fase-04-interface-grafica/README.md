# Missão Marte — Fase 04: Interface gráfica

Evolução da Fase 3 para um jogo desktop com **sprites, animações, sons, movimentação visual suave e HUD**, preservando o domínio do jogo e a dinâmica por turnos.

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

As **17 imagens PNG** e os **8 arquivos WAV** estão em `core/assets/`. Foram gerados localmente pelo código-fonte `tools/GeradorDeAssets.java` e não usam arte de terceiros. Para recriá-los:

```powershell
java tools\GeradorDeAssets.java
```

O catálogo de imagens e sons fica em `gdx/recursos`. Ajuste o catálogo se substituir arquivos por outros com nomes ou dimensões diferentes.

## Organização

- `core`: domínio, aplicação, persistência, telas e lógica gráfica;
- `core/assets`: sprites e WAV;
- `lwjgl3`: inicializador desktop;
- `docs/tutorial.md`: arquitetura e implementação;
- `docs/evidencias.md`: roteiro de evidências reais a preencher após execução no Windows;
- `docs/creditos.md`: autoria e licença dos recursos.

**Atenção à validação:** o código de domínio e de aplicação foi compilado isoladamente neste ambiente, mas a execução integral do Gradle, o resultado dos testes JUnit e a janela/audio ainda precisam ser conferidos no Windows. Não trate a fase como aprovada até concluir essa validação.
