# Guia para a equipe — Como compilar e testar a Fase 4

Este guia explica como um integrante da equipe pode executar o **Missão Marte — Fase 4** em seu próprio Windows, usando o código entregue no repositório. Não é necessário instalar Gradle: o projeto inclui o Gradle Wrapper.

## 1. Pré-requisitos

- Windows 10 ou 11 e conexão com a internet na primeira compilação (para baixar dependências).
- **JDK 17** instalado. O Gradle Wrapper do projeto usa **Gradle 8.7**; não inicie esse Gradle com Java 24.
- Placa de vídeo compatível com OpenGL 2.0 ou superior.
- Dispositivo de áudio para ouvir música e efeitos (opcional: há modo sem áudio).

É recomendável extrair o projeto **fora do OneDrive** e de caminhos acentuados, por exemplo: `C:\MissaoMarte\fase-04-interface-grafica`. Isso evita possíveis bloqueios ao apagar arquivos temporários do build.

## 2. Abrir o terminal na pasta certa

O arquivo `gradlew.bat` fica **dentro da pasta `fase-04-interface-grafica`**, não na pasta externa que pode ser criada ao extrair o ZIP.

Abra essa pasta no Explorador de Arquivos, clique na barra de endereço, digite `powershell` e pressione Enter. Confirme:

```powershell
Get-ChildItem .\gradlew.bat
```

Se aparecer que `gradlew.bat` não foi encontrado, confira a pasta atual com `Get-Location` e procure com:

```powershell
Get-ChildItem -Recurse -Filter gradlew.bat | Select-Object FullName
```

Entre na pasta indicada no resultado antes de continuar.

## 3. Conferir o Java 17

No PowerShell:

```powershell
java -version
echo $env:JAVA_HOME
.\gradlew.bat --version
```

O `java -version` deve mostrar versão **17** e o `gradlew --version` deve mostrar **Gradle 8.7 rodando em Java 17**. É possível que `java -version` exiba Java 17 enquanto `JAVA_HOME` ainda aponta para Java 24: **nesse caso, ajuste `JAVA_HOME`**.

Se o JDK 17 for Temurin e estiver instalado em `C:\Program Files\Eclipse Adoptium`, você pode configurá-lo **somente para a sessão atual**:

```powershell
$jdk17 = Get-ChildItem 'C:\Program Files\Eclipse Adoptium' -Directory |
    Where-Object { $_.Name -like 'jdk-17*' } |
    Select-Object -First 1
if (-not $jdk17) { throw 'JDK 17 não encontrado nessa pasta. Localize a instalação antes de continuar.' }
$env:JAVA_HOME = $jdk17.FullName
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
& "$env:JAVA_HOME\bin\java.exe" -version
.\gradlew.bat --version
```

Se o JDK 17 estiver em outro local, altere o valor de `JAVA_HOME` para a **pasta real do JDK 17**, não para `bin`.

> **Erro conhecido:** `Unsupported class file major version 68` significa que a execução encontrou classes de Java 24, incompatíveis com o Gradle 8.7. Confira `JAVA_HOME` e rode `gradlew --version` novamente.

## 4. Compilar e rodar os testes automatizados

Na raiz de `fase-04-interface-grafica`:

```powershell
.\gradlew.bat clean build --console=plain
```

O esperado é a mensagem `BUILD SUCCESSFUL`. Na primeira execução o Gradle pode precisar baixar dependências. **A conclusão do build não substitui os testes manuais na janela**.

Para verificar **quantos testes realmente rodaram**, abra após a compilação:

```powershell
Start-Process '.\core\build\reports\tests\test\index.html'
```

Anote a quantidade de testes, falhas e testes ignorados mostrada no relatório. Não presuma uma contagem fixa: ela depende do código efetivamente recebido.

Se ocorrer `Unable to delete directory ...\build` durante `clean`, feche o jogo e a IDE, pare o Gradle e tente novamente:

```powershell
.\gradlew.bat --stop
Remove-Item -LiteralPath '.\core\build' -Recurse -Force
Remove-Item -LiteralPath '.\lwjgl3\build' -Recurse -Force -ErrorAction SilentlyContinue
.\gradlew.bat build --no-build-cache --rerun-tasks --console=plain
```

Apague **apenas** as pastas `build` geradas; não apague `src`, `assets`, `gradle/wrapper` nem os arquivos `build.gradle`.

## 5. Abrir o jogo no desktop

```powershell
.\gradlew.bat :lwjgl3:run --console=plain
```

A janela do Missão Marte deve abrir em aproximadamente **800 × 600**. Deixe o PowerShell aberto enquanto joga, pois ele mostra mensagens de execução e eventuais erros.

### Execução com cenário reproduzível

```powershell
.\gradlew.bat :lwjgl3:run -Dmissaomarte.semente=4 --console=plain
```

A mesma semente reproduz as posições iniciais. A patrulha inimiga começa quando a nave sai da base e avança pelo tempo de jogo. Em uma partida nova, aguarde alguns segundos na base, saia e depois pare para conferir que os inimigos continuam se movendo.

### Execução sem dispositivo de áudio

```powershell
.\gradlew.bat :lwjgl3:run -Dmissaomarte.semaudio=true --console=plain
```

Esse modo ajuda a isolar problemas com som; o jogo deve continuar jogável.

## 6. Controles

| Local | Controles |
| --- | --- |
| Menu | `1` iniciar, `2` ranking, `3` limpar ranking, `4` sair |
| Nova partida | Digitar o nome; `Backspace` apagar; `←/→` dificuldade; `Enter` iniciar; `Esc` voltar |
| Partida | `W/A/S/D` ou setas mover; segurar direção repete; `E` embarcar; `Q/Esc` abortar |
| Fim de partida | `Enter` continuar |
| Outras telas, exceto nome do piloto | `M` ligar/desligar o som |

## 7. Roteiro de verificação manual

Marque cada item como **aprovado**, **reprovado** ou **não testado**. Se algo não funcionar, registre o que ocorreu e, se possível, uma captura de tela ou trecho do log.

| # | Verificação | Resultado |
| --- | --- | --- |
| 1 | Menu abre com fundo espacial, nave/propulsor animado e som de seleção | Pendente |
| 2 | Música de fundo toca; `M` silencia e reativa | Pendente |
| 3 | Na tela de nome, digitar `m` insere a letra sem mutar o jogo | Pendente |
| 4 | Nome, dificuldade e início da partida funcionam | Pendente |
| 5 | Mapa mostra nave, base, passageiros, inimigos e asteroides como sprites | Pendente |
| 6 | Nave gira e desliza ao movimentar; cada movimento válido é um turno | Pendente |
| 7 | Segurar `D` repete movimentos e desconta pontos por turno | Pendente |
| 8 | Embarque com `E` mostra brilho, pontos e vaga ocupada no HUD | Pendente |
| 9 | Entrega na base atualiza contadores/barra, brilho e som | Pendente |
| 10 | Colisão perde vida/pontos e mostra explosão, tremor, flash e som | Pendente |
| 11 | Vitória exibe mensagem, pontuação e registro no ranking | Pendente |
| 12 | Derrota exibe mensagem e permite retornar ao menu | Pendente |
| 13 | A mesma semente reproduz o cenário ao repetir a execução | Pendente |
| 14 | Modo `semaudio=true` abre e funciona sem som | Pendente |
| 15 | Redimensionar, minimizar, restaurar e fechar a janela não causa exceção | Pendente |

O arquivo `ranking.json` é criado **na raiz desta fase** quando o jogo grava o ranking. Ele está ignorado pelo Git para não misturar pontuações individuais dos colegas.

## 8. Enviar evidências ao restante do grupo

Guarde capturas de tela do **menu, mapa com sprites/HUD, embarque, colisão, vitória, derrota, ranking** e do **relatório de testes Gradle**. Acrescente os resultados e eventuais falhas ao arquivo `docs/evidencias.md` e salve as imagens em `docs/imagens/`.

Inclua uma observação simples sobre o áudio (ouvido e funcionando / sem áudio / não testado). O resultado de um teste não deve ser registrado como aprovado antes de ser executado naquela máquina.

## 9. Solução rápida de problemas

| Erro ou sintoma | Como verificar/corrigir |
| --- | --- |
| `gradlew.bat` não reconhecido | Entrar na pasta que contém `gradlew.bat` |
| `Unsupported class file major version 68` | Gradle 8.7 está usando Java 24; configurar JDK 17 e conferir `gradlew --version` |
| Falha em `:core:clean` / arquivos bloqueados | Fechar jogo/IDE, parar Gradle, remover apenas `build/`; preferir pasta fora do OneDrive |
| `Could not resolve` dependências | Verificar internet/Maven Central e tentar novamente |
| Erro de compilação em `Recursos.java` ao carregar `Music`/`Sound` | Usar `if/else` com `assets.load(caminho, Music.class)` ou `assets.load(caminho, Sound.class)`, separadamente; esta versão do ZIP já inclui a correção |
| Janela não abre / erro OpenGL | Conferir drivers de vídeo e suporte a OpenGL 2.0+; copiar o erro exibido no PowerShell |
| Erro relacionado a áudio | Testar com `-Dmissaomarte.semaudio=true` |
| Imagens/sons não aparecem | Verificar `core/assets/imagens`, `core/assets/sons` e a execução via `:lwjgl3:run` |

## 10. Resultado para comunicar à equipe

Ao concluir, o colega pode registrar em `docs/evidencias.md`:

- **Ambiente:** versão do Windows, `java -version` e `gradlew --version`.
- **Build:** comando executado, `BUILD SUCCESSFUL` ou erro completo, número real de testes/falhas.
- **Interface:** itens do roteiro aprovados/reprovados/não testados.
- **Problemas:** erros encontrados e procedimentos usados para corrigir.
- **Comprovação:** prints e logs reais da máquina.

Assim cada integrante consegue reproduzir o teste sem precisar alterar o código-fonte.
