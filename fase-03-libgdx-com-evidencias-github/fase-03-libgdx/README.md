# Missao Marte — Fase 3 (libGDX)

Migracao da Fase 2 para a janela desktop, sem modificar as regras do dominio.

## Requisitos
- JDK 17 (JAVA_HOME configurado)
- Rede para baixar bibliotecas do Maven Central e Gradle 8.7 na primeira execucao
- GPU com suporte a OpenGL 2.0 ou superior

## Comandos (PowerShell)
```powershell
.\gradlew.bat --version
.\gradlew.bat clean build
.\gradlew.bat :lwjgl3:run --console=plain
```

## Controles
- Menu: 1 nova partida, 2 ranking, 3 confirmar limpeza, 4 sair
- Nova partida: digite piloto; setas selecionam dificuldade; Enter inicia; Esc volta
- Jogo: W/A/S/D ou setas movem; E embarca; Q ou Esc aborta
- Ao terminar: Enter avanca ao ranking (vitoria) ou menu (derrota)

## Estrutura
- `core`: dominio e aplicacao (copiados sem modificacao), interface `gdx` e testes.
- `lwjgl3`: launcher desktop.
- A versao de console permanece exclusivamente na Fase 2.
- `ranking.json` e salvo na raiz da Fase 3.

**Divergencias do referencial:** os arquivos reais da Fase 2 usam pacote `missaomarte`, ranking **Top 5**, comando `Comando` e classe `Partida`, um metodo JUnit de regressao com varias assertivas, Gradle 8.14.3 e JUnit 5.11.4. Esses contratos foram preservados. O wrapper da Fase 3 foi configurado para Gradle 8.7 conforme o enunciado; valide com JDK 17.

Leia `docs/tutorial.md` antes de entregar.
