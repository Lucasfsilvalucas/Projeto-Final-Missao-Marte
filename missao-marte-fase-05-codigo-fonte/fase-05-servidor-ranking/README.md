# Missão Marte Unifor · Fase 05

A Sprint 3 liga o cliente desktop da Fase 4 a uma API REST independente para contas, histórico e ranking global. O jogador convidado joga com o ranking local Top 5 em `cliente/ranking.json`. Com uma conta, as vitórias e derrotas entram no histórico remoto, e a melhor vitória com pontuação positiva pode entrar no Top 10 global.

## Executar no Windows

Use um **JDK 17** no `JAVA_HOME` e no `PATH`. O cliente usa Gradle 8.7 e libGDX 1.12.1. O servidor usa Gradle 9.8.2, Spring Boot 4.1.2 e H2. Os wrappers baixam as dependências na primeira execução. Para abrir o jogo, é necessário suporte a OpenGL 2.0 ou superior.

Abra dois PowerShells na pasta `fase-05-servidor-ranking`.

**Terminal 1 — servidor**

```powershell
cd .\servidor
.\gradlew.bat --version
.\gradlew.bat test
.\gradlew.bat bootRun --console=plain
```

**Terminal 2 — cliente**

```powershell
cd .\cliente
.\gradlew.bat --version
.\gradlew.bat clean build
.\gradlew.bat :lwjgl3:run --console=plain
```

O servidor escuta em `127.0.0.1:8080`. Abra `http://localhost:8080/swagger-ui.html` para explorar a API. Para distribuir o executável do servidor, gere o JAR com `.\gradlew.bat bootJar` e execute `java -jar .\build\libs\missao-marte-servidor-0.5.0.jar` na pasta `servidor`. Para obter os scripts de distribuição do cliente, execute `.\gradlew.bat :lwjgl3:installDist`. O modo convidado está disponível com o servidor desligado.

| Configuração | Efeito |
| --- | --- |
| `-Dmissaomarte.servidor=http://localhost:8080` | URL da API no cliente. Passe junto de `:lwjgl3:run`. |
| `-Dmissaomarte.semente=4` | Torna reproduzível a primeira partida do processo. |
| `-Dmissaomarte.semaudio=true` | Desliga o áudio. |
| `MISSAOMARTE_JWT_SEGREDO` | Segredo com pelo menos 32 bytes. Se omitido, o servidor gera um segredo por execução. |
| `MISSAOMARTE_DADOS` | Diretório do banco H2. O padrão é `servidor/dados` ao rodar a partir de `servidor`. |

## Controles

| Tela | Teclas |
| --- | --- |
| Menu convidado | `1` Jogar, `2` ranking local, `3` limpar ranking local, `4` entrar ou criar conta, `5` sair |
| Menu logado | `1` Jogar, `2` ranking global, `3` meu histórico, `4` sair da conta, `5` sair |
| Conta | `Tab` ou setas verticais trocam o campo, `Alt` alterna login e cadastro, `Enter` confirma, `Esc` volta |
| Nova missão | Convidado digita o nome. Logado usa o nome da conta. Setas mudam a dificuldade, `Enter` inicia |
| Partida | `WASD` ou setas movem, `E` embarca, `M` alterna o som, `Q` ou `Esc` abortam |
| Fim da partida logada | `R` reenvia em caso de erro, `Enter` continua quando o envio termina |
| Ranking e histórico | `R` recarrega, `Enter` ou `Esc` voltam |

## API

| Método e caminho | Token | Sucesso |
| --- | --- | --- |
| `POST /api/usuarios` | — | `201` com ID e nome |
| `POST /api/auth/login` | — | `200` com token Bearer, nome e duração da sessão em segundos |
| `POST /api/partidas` | Sim | `201` com o resultado registrado |
| `GET /api/partidas/minhas?pagina=0&tamanho=10` | Sim | `200` com o histórico mais recente primeiro |
| `GET /api/ranking` | Sim | `200` com o Top 10 |

Para as rotas protegidas, envie `Authorization: Bearer <token>`. No Swagger UI, use **Authorize** e cole apenas o token. As chamadas de demonstração estão em [requests.http](servidor/requests.http). Consulte o [tutorial](docs/tutorial.md), o [roteiro de validação](docs/validacao.md) e os [créditos](docs/creditos.md).

O servidor recebe as estatísticas da partida e valida os campos. Como a simulação ocorre no cliente, a API não consegue verificar se a pontuação foi obtida durante uma partida real.
