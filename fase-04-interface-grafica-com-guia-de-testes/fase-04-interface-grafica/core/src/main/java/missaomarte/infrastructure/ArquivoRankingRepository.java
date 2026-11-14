package missaomarte.infrastructure;
import missaomarte.application.*;
import missaomarte.domain.Dificuldade;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
public final class ArquivoRankingRepository implements RankingRepository {
    private final Path arquivo;
    public ArquivoRankingRepository(Path arquivo) { this.arquivo = arquivo; }
    @Override public List<RankingEntry> listar() {
        if (!Files.exists(arquivo)) return List.of();
        try {
            String json = Files.readString(arquivo, StandardCharsets.UTF_8);
            List<RankingEntry> resultado = new ArrayList<>();
            Matcher matcher = Pattern.compile("\\{[^{}]*\\}").matcher(json);
            while (matcher.find()) {
                String obj = matcher.group();
                try {
                    resultado.add(new RankingEntry(valor(obj,"name"), Integer.parseInt(valor(obj,"score")),
                        Dificuldade.valueOf(valor(obj,"dificuldade")), Integer.parseInt(valor(obj,"passageirosColetados")),
                        valor(obj,"dataHora"), Long.parseLong(valor(obj,"tempoJogo"))));
                } catch (IllegalArgumentException erro) { /* Ignora registro malformado */ }
            }
            return resultado;
        } catch (IOException e) { throw new IllegalStateException("Falha ao ler ranking", e); }
    }
    private String valor(String json, String chave) {
        Matcher m = Pattern.compile("\"" + chave + "\"\\s*:\s*(?:\"((?:\\\\.|[^\"\\\\])*)\"|(-?\\d+))").matcher(json);
        if (!m.find()) throw new IllegalArgumentException("Campo ausente: " + chave);
        return m.group(1) != null ? m.group(1).replace("\\\"", "\"").replace("\\\\", "\\") : m.group(2);
    }
    private String escapar(String texto) { return texto.replace("\\", "\\\\").replace("\"", "\\\""); }
    @Override public void salvar(List<RankingEntry> entradas) {
        StringJoiner joiner = new StringJoiner(",", "[", "]");
        for (RankingEntry e : entradas) joiner.add("{\"name\":\"" + escapar(e.nome()) + "\",\"score\":" + e.pontos()
            + ",\"dificuldade\":\"" + e.dificuldade().name() + "\",\"passageirosColetados\":" + e.passageirosEntregues()
            + ",\"dataHora\":\"" + escapar(e.dataHora()) + "\",\"tempoJogo\":" + e.tempoSegundos() + "}");
        try {
            if (arquivo.getParent() != null) Files.createDirectories(arquivo.getParent());
            Files.writeString(arquivo, joiner.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) { throw new IllegalStateException("Falha ao salvar ranking", e); }
    }
    @Override public void limpar() {
        try { Files.deleteIfExists(arquivo); }
        catch (IOException e) { throw new IllegalStateException("Falha ao limpar ranking", e); }
    }
}
