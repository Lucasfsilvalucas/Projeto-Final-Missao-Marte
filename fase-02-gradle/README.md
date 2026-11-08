# Fase 2 — Gradle e JUnit 5

Esta fase é uma **cópia independente da Fase 1**, migrada para Gradle. A Fase 0 e a Fase 1 devem continuar preservadas.

## Pré-requisitos

- JDK 17 (ou Gradle com acesso a um toolchain JDK 17).
- Internet no primeiro build para baixar o Gradle e as dependências JUnit 5.

**Atenção:** neste pacote o `gradle-wrapper.jar` ainda precisa ser gerado. A configuração (`gradle-wrapper.properties`) e scripts de execução estão incluídos, mas não foi possível obter o binário do Wrapper no ambiente de geração.

Uma única vez, com Gradle instalado, rode na pasta `fase-02-gradle`:

```powershell
gradle wrapper --gradle-version 8.14.3
```

Isso gera `gradle/wrapper/gradle-wrapper.jar` oficial. Faça commit desse arquivo e dos scripts `gradlew`, `gradlew.bat` e `gradle-wrapper.properties`. Em seguida **não precisa instalar Gradle em outras máquinas**, apenas Java 17 e conexão inicial à internet.

## Executar no Windows (PowerShell)

```powershell
.\gradlew.bat clean build
.\gradlew.bat test
.\gradlew.bat run
```

## Linux / macOS

```bash
./gradlew clean build
./gradlew test
./gradlew run
```

O relatório de testes JUnit fica em `build/reports/tests/test/index.html`.

## Estrutura

- `src/main/java/missaomarte/`: mesmo código da Fase 1, com quatro camadas.
- `src/test/java/missaomarte/TestesRegressaoTest.java`: migração das verificações automatizadas anteriores para JUnit 5.
- `build.gradle`: plugin `application`, Java 17, JUnit 5, `run` e `test`.
- `settings.gradle`: nome do projeto.
- `gradle/wrapper/gradle-wrapper.properties`: distribuição fixada do Gradle.

## Evidências e limites

Os fontes Java de produção são os mesmos da Fase 1. A compilação com `javac --release 17` pode ser validada independentemente do Gradle. **Os comandos Gradle/JUnit ainda precisam ser executados em um computador com Gradle disponível e acesso ao Maven Central**, antes de marcar a Fase 2 como concluída no GitHub.
