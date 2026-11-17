# Validação técnica realizada durante a preparação

- Compilação isolada de `domain`, `application`, `infrastructure` e classes Java puras de visual com `javac` (Java 21, `--release` não especificado): **sucesso**. O projeto configura Java 17 para o Gradle.
- Execução de teste local de aplicação (sem JUnit): movimento, embarque, entrega, vitória e congelamento da duração: **sucesso**.
- Verificação independente de 17 arquivos PNG (Pillow) e 8 WAV (wave): **válidos**.
- Comparação SHA-256 dos fontes `domain`, `application`, `infrastructure`: 20 idênticos e 2 alterados (`Partida`, `ResultadoTurno`); `EventoTurno` é novo.
- Gradle 8.7 completo e JUnit: **não executados** neste ambiente; a distribuição não pôde ser baixada por ausência de acesso de rede (`UnknownHostException: services.gradle.org`).
- Janela OpenGL e reprodução audível: **não executadas** aqui. Validar no Windows antes da entrega.
