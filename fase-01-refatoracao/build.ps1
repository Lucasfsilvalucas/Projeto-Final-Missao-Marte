param([switch]$Run, [switch]$Test)
$ErrorActionPreference = 'Stop'
Set-Location $PSScriptRoot
New-Item -ItemType Directory -Force -Path out | Out-Null
$files = @(Get-ChildItem -Recurse src/main/java -Filter *.java | ForEach-Object FullName)
& javac --release 17 -encoding UTF-8 -d out $files
if ($LASTEXITCODE -ne 0) { throw 'Falha na compilacao' }
Write-Host 'Compilacao concluida.'
if ($Test) {
    $tests = @(Get-ChildItem -Recurse src/test/java -Filter *.java | ForEach-Object FullName)
    & javac --release 17 -encoding UTF-8 -cp out -d out $tests
    if ($LASTEXITCODE -ne 0) { throw 'Falha ao compilar testes' }
    & java -Dfile.encoding=UTF-8 -cp out missaomarte.TestesRegressao
    if ($LASTEXITCODE -ne 0) { throw 'Falha nos testes' }
}
if ($Run) {
    & java -Dfile.encoding=UTF-8 -cp out missaomarte.Main
    if ($LASTEXITCODE -ne 0) { throw 'Falha ao executar jogo' }
}
