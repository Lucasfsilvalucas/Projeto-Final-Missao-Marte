package missaomarte.application.servidor;

import java.nio.charset.StandardCharsets;

public final class FormularioDeConta {
    private final StringBuilder nome = new StringBuilder();
    private final StringBuilder senha = new StringBuilder();
    private boolean campoSenha;
    private boolean cadastro;

    public void trocarCampo() { campoSenha = !campoSenha; }
    public void trocarModo() { cadastro = !cadastro; }
    public boolean campoSenha() { return campoSenha; }
    public boolean cadastro() { return cadastro; }
    public String nome() { return nome.toString(); }
    public String senha() { return senha.toString(); }
    public String senhaMascarada() { return "*".repeat(senha.codePointCount(0, senha.length())); }
    public void limparSenha() { senha.setLength(0); }

    public void digitar(char letra) {
        if (campoSenha) {
            if (!Character.isISOControl(letra)) {
                senha.append(letra);
                if (bytesSenha() > 72) senha.setLength(senha.length() - 1);
            }
        } else if (nome.length() < 20 && String.valueOf(letra).matches("[A-Za-z0-9_-]")) {
            nome.append(letra);
        }
    }

    public void apagar() {
        StringBuilder campo = campoSenha ? senha : nome;
        if (!campo.isEmpty()) campo.setLength(campo.offsetByCodePoints(campo.length(), -1));
    }

    public String erro() {
        if (!nome().matches("[A-Za-z0-9_-]{3,20}")) return "Nome: use de 3 a 20 letras, números, _ ou -.";
        if (senha.isEmpty()) return "Informe a senha.";
        if (cadastro && senha.codePointCount(0, senha.length()) < 8) return "A senha precisa ter 8 caracteres.";
        return null;
    }

    private int bytesSenha() { return senha().getBytes(StandardCharsets.UTF_8).length; }
}
