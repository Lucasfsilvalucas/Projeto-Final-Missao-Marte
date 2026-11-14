# Evidências — Fase 04 (preencher após execução no Windows)

**Estado atual:** código e arquivos preparados; verificação integral pendente. Não há capturas reais da Fase 4 anexadas ainda.

## Testes automatizados

1. Na pasta da fase, executar `./gradlew.bat --version` e confirmar Gradle 8.7 e JDK 17.
2. Executar `./gradlew.bat clean build`. Copiar aqui o resultado **real**, com `BUILD SUCCESSFUL` e a quantidade de testes do relatório `core/build/reports/tests/test/index.html`.
3. Registrar qualquer erro e a correção aplicada. Não reutilizar screenshots da Fase 3 como se fossem da Fase 4.

## Testes manuais (anotar aprovado/reprovado)

| Teste | Resultado | Print/log |
| --- | --- | --- |
| Menu com fundo e nave animada | Pendente | |
| Música e alternância M (sem alterar nome) | Pendente | |
| Mapa com sprites; giro da nave | Pendente | |
| Deslizamento e repetição ao segurar D | Pendente | |
| Embarque com brilho, texto e vaga ocupada | Pendente | |
| Entrega com progresso atualizado | Pendente | |
| Colisão com explosão, tremor, flash e perda de vida | Pendente | |
| Vitória, ranking e derrota | Pendente | |
| Mesma semente gera mesmo cenário | Pendente | |
| `-Dmissaomarte.semaudio=true` | Pendente | |
| Redimensionamento e encerramento sem exceções | Pendente | |

Capturas sugeridas para `docs/imagens/`: `01-menu.png`, `02-sprites-hud.png`, `03-embarque.png`, `04-colisao.png`, `05-vitoria.png`, `06-derrota.png`, `07-ranking.png`, `08-gradle-testes.png`.

## Hashes e camadas

Confira hashes com `Get-FileHash` nas Fases 3 e 4. Nenhuma fase anterior deve ser modificada. A relação de fontes preservados está em `docs/comparacao-fontes.md`.
