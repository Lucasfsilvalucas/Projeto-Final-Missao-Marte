# Missão Marte Unifor — Fase 1

Refatoração Java da versão POO original do exercício. A arquitetura separa `domain`, `application`, `console`, `infrastructure`; contém scripts de build e testes de regressão sem dependências externas.

**Requisitos:** JDK 17+; PowerShell para `build.ps1`.

```powershell
.\build.ps1
.\build.ps1 -Run
.\build.ps1 -Test
```

Veja [docs/tutorial.md](docs/tutorial.md) para SOLID, GRASP, padrões, mudanças de comportamento, diagramas e validação.
