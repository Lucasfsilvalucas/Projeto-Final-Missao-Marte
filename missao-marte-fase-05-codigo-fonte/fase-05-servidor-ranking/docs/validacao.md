# Validação da Fase 05

## Resultados automatizados

Os dois builds rodaram com JDK 17. O ambiente não conseguia baixar as dependências pelo Gradle diretamente. Para concluir a validação, foi usado um repositório temporário de dependências locais, externo aos projetos.

| Projeto | Gradle | Execução | Resultado |
| --- | --- | --- | --- |
| Cliente | 8.7 | `clean build` | `BUILD SUCCESSFUL`, 36 testes aprovados e distribuição desktop gerada |
| Servidor | 9.8.2 | `test bootJar` | `BUILD SUCCESSFUL`, 5 testes aprovados com MockMvc e H2 em memória e JAR executável gerado |

Os 36 testes do cliente incluem os 26 da Fase 4 entregue e 10 novos de JSON, sessão, formulário, dados da partida e apresentação do histórico. Os cinco testes do servidor cobrem hash e sal, conflito de nome, validação, login, autenticação, histórico paginado e isolado, reenvio e classificação por pontuação, duração e antiguidade.

A comparação por SHA-256 dos 13 fontes de domínio conferiu com a Fase 4 entregue. Os hashes estão em [domain.sha256](domain.sha256).

| Camada | Arquivos envolvidos |
| --- | --- |
| Domínio | Os 13 arquivos existentes foram conferidos byte a byte. |
| Aplicação | Novos `ServidorDoJogo`, `Resposta`, `Sessao`, `PartidaConcluida` e `FormularioDeConta` para o contrato remoto, a sessão e a entrada de dados. |
| Infraestrutura | Ranking local e persistência em arquivo da Fase 4. |
| Adaptador | Novos `ServidorHttp` e `JsonDaApi` para chamadas assíncronas e conversão. |
| Telas novas | `LoginScreen` para conta e `ResultadosOnlineScreen` para classificação e histórico. |
| Telas adaptadas | `MenuScreen` alterna opções, `NovaPartidaScreen` define o nome, `GameScreen` encaminha o resultado, `TelaBase` libera a letra M no login e `MissaoMarteGame` mantém a sessão. |
| Desktop | `Lwjgl3Launcher` injeta o adaptador. O Gradle repassa a nova opção de endereço. |

Para conferir os hashes no PowerShell, rode na pasta da Fase 5.

```powershell
Get-Content .\docs\domain.sha256 | ForEach-Object {
    $hash, $arquivo = $_ -split '  ', 2
    $atual = (Get-FileHash -Algorithm SHA256 -LiteralPath (Join-Path .\cliente $arquivo)).Hash
    if ($atual -ne $hash) { throw "Hash diferente em $arquivo" }
}
```

## Execução HTTP do servidor

O JAR gerado abriu na porta de teste 18081 e gravou os dados em um arquivo H2 temporário. A chamada autenticada ao histórico devolveu a derrota seguida da vitória, e o ranking mostrou apenas a vitória. A descrição OpenAPI apresentou o esquema Bearer.

```text
POST /api/usuarios -> 201
POST /api/usuarios -> 409
POST /api/usuarios -> 400
POST /api/auth/login -> 401
POST /api/auth/login -> 200
GET /api/ranking -> 401
POST /api/partidas -> 201
POST /api/partidas -> 201
GET /api/partidas/minhas -> 200
GET /api/ranking -> 200
GET /v3/api-docs -> 200
```

## Roteiro de aceitação no Windows

Registre no relatório a saída dos wrappers, os totais de testes, as respostas HTTP com token omitido e as capturas da interface feitas após executar estes passos. As capturas disponíveis na conversa são da Fase 4.

| Cenário | Ação | Resultado esperado |
| --- | --- | --- |
| Ambiente | Rode `.\gradlew.bat --version` dentro de cada projeto. | JVM 17, Gradle 9.8.2 no servidor e 8.7 no cliente. |
| Testes | Rode `test` no servidor e `clean build` no cliente. | `BUILD SUCCESSFUL` e relatório HTML dos testes. |
| API | Use o Swagger UI ou [requests.http](../servidor/requests.http) para cadastro, login, vitória, derrota, histórico e ranking. | `201` no cadastro e gravação, `200` no login e consultas. |
| Erros da API | Cadastre duas vezes com caixa diferente, envie um nome de dois caracteres, tente senha errada e consulte ranking sem token. | `409`, `400`, `401` e `401`, respectivamente. |
| Convidado | Com o servidor desligado, jogue até vencer e veja o ranking local. | Vitória aparece em `ranking.json` e na opção `2`. |
| Cadastro no jogo | Menu `4`, `Alt`, digite nome e senha, pressione `Enter`. | Senha mascarada e entrada automática. |
| Vitória e derrota | Jogue logado, vença uma partida e perca outra. | Os dois resultados aparecem no histórico. Só a vitória disputa o Top 10. |
| Duas contas | Registre mais de uma vitória por conta. | Uma classificação por jogador com a melhor vitória de cada um. |
| Falha de envio | Com o servidor parado, termine uma missão. Inicie o servidor com o mesmo segredo e pressione `R`. | Erro visível durante a indisponibilidade. Novo envio registra a partida uma vez. |
| Falha de login | Tente entrar com senha errada ou servidor desligado. | Mensagem para cada situação e retorno ao menu com `Esc`. |
| Sair da conta | Pressione `4` no menu logado. | Opções do modo convidado reaparecem. |

Para testar o reenvio após reiniciar o servidor, use o mesmo `MISSAOMARTE_JWT_SEGREDO` nas duas execuções. Quando ele é gerado a cada inicialização, o token antigo recebe `401` e o jogo volta ao modo convidado.
