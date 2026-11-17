package missaomarte.servidor.usuario;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.nio.charset.StandardCharsets;

public record Credenciais(
        @NotNull(message = "Informe o nome.")
        @Pattern(regexp = "[A-Za-z0-9_-]{3,20}", message = "Nome deve ter de 3 a 20 letras, números, _ ou -.")
        String nome,
        @NotNull(message = "Informe a senha.") String senha) {
    @AssertTrue(message = "Senha deve ter pelo menos 8 caracteres e no máximo 72 bytes.")
    public boolean isSenhaValida() {
        return senha == null || (senha.codePointCount(0, senha.length()) >= 8
                && senha.getBytes(StandardCharsets.UTF_8).length <= 72);
    }

    @Override public String toString() {
        return "Credenciais[nome=" + nome + "]";
    }
}
