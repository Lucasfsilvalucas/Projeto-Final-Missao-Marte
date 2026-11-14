# Comparação de fontes: Fase 3 → Fase 4

Comparação SHA-256 sobre os arquivos das camadas `domain`, `application` e `infrastructure` da cópia de trabalho utilizada.

## Arquivos idênticos

- `domain/Asteroide.java`
- `domain/Posicao.java`
- `domain/Astronauta.java`
- `domain/Missao.java`
- `domain/EntidadeFixa.java`
- `domain/Inimigo.java`
- `domain/Dificuldade.java`
- `domain/Base.java`
- `domain/Nave.java`
- `domain/Engenheiro.java`
- `domain/Passageiro.java`
- `domain/Professor.java`
- `domain/EntidadeMapa.java`
- `application/CriadorMissao.java`
- `application/RankingEntry.java`
- `application/RankingRepository.java`
- `application/EstadoPartida.java`
- `application/Comando.java`
- `application/RankingService.java`
- `infrastructure/ArquivoRankingRepository.java`

## Arquivos modificados

- `application/Partida.java` — alterado
- `application/ResultadoTurno.java` — alterado

## Novo arquivo de aplicação

- `application/EventoTurno.java`: enum de eventos do turno.

A comparação não abrange as telas e arquivos novos de apresentação, que foram redesenhados para a Fase 4.