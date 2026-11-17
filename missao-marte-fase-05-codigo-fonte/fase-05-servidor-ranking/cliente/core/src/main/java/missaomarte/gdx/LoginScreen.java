package missaomarte.gdx;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import missaomarte.application.servidor.FormularioDeConta;

final class LoginScreen extends TelaBase {
    private final FormularioDeConta formulario = new FormularioDeConta();
    private String aviso = "";
    private boolean aguardando;
    private boolean contaCriada;

    LoginScreen(MissaoMarteGame game) { super(game); }

    @Override public void show() {
        super.show();
        instalar(new InputAdapter() {
            @Override public boolean keyDown(int tecla) {
                if (tecla == Input.Keys.ESCAPE) {
                    formulario.limparSenha();
                    game.menu();
                    return true;
                }
                if (aguardando) return true;
                switch (tecla) {
                    case Input.Keys.TAB, Input.Keys.UP, Input.Keys.DOWN -> formulario.trocarCampo();
                    case Input.Keys.ALT_LEFT, Input.Keys.ALT_RIGHT -> {
                        formulario.trocarModo();
                        aviso = "";
                    }
                    case Input.Keys.BACKSPACE -> formulario.apagar();
                    case Input.Keys.ENTER -> enviar();
                    default -> { return false; }
                }
                return true;
            }

            @Override public boolean keyTyped(char letra) {
                if (!aguardando) formulario.digitar(letra);
                return true;
            }
        });
    }

    private void enviar() {
        String erro = formulario.erro();
        if (erro != null) {
            aviso = erro;
            return;
        }
        String nome = formulario.nome();
        String senha = formulario.senha();
        aguardando = true;
        if (formulario.cadastro()) {
            aviso = "Criando conta...";
            game.servidor.cadastrar(nome, senha, resposta -> {
                if (game.getScreen() != this) return;
                if (resposta.sucesso()) {
                    formulario.trocarModo();
                    contaCriada = true;
                    entrar(nome, senha);
                } else {
                    aviso = resposta.erro();
                    aguardando = false;
                }
            });
        } else {
            entrar(nome, senha);
        }
    }

    private void entrar(String nome, String senha) {
        aviso = "Entrando...";
        game.servidor.entrar(nome, senha, resposta -> {
            if (game.getScreen() != this) return;
            aguardando = false;
            if (resposta.sucesso()) {
                formulario.limparSenha();
                game.sessao = resposta.valor();
                game.menu(contaCriada ? "Conta criada. Login efetuado." : "Login efetuado.");
            } else {
                aviso = resposta.erro();
            }
        });
    }

    @Override protected void desenhar(float delta) {
        centralizar(formulario.cadastro() ? "CRIAR CONTA" : "ENTRAR", 450);
        centralizar((formulario.campoSenha() ? "" : "> ") + "Nome: " + formulario.nome(), 370);
        centralizar((formulario.campoSenha() ? "> " : "") + "Senha: " + formulario.senhaMascarada(), 330);
        centralizar(aviso, 265);
        centralizar("Tab/Setas: campo | Alt: trocar modo", 180);
        centralizar("Enter: confirmar | Esc: menu", 145);
    }
}
