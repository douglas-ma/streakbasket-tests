package com.streakbasket.tests.steps;

import com.streakbasket.tests.pages.PerfilPage;
import com.streakbasket.tests.support.Contexto;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PerfilSteps {
    private final Contexto contexto;

    public PerfilSteps(Contexto contexto) {
        this.contexto = contexto;
    }

    private PerfilPage perfil() {
        return contexto.pagina(PerfilPage.class);
    }

    @Quando("preencho o e-mail {string} e a senha {string}")
    public void preencher(String email, String senha) {
        perfil().preencher(email, senha);
    }

    @Quando("envio o formulário com {string}")
    public void enviar(String botao) {
        perfil().enviar(botao);
    }

    @Quando("escolho a aba {string} do perfil")
    public void escolherAba(String aba) {
        perfil().escolherAba(aba);
    }

    @Entao("devo ver o botão de envio {string}")
    public void verificarBotaoDeEnvio(String texto) {
        assertTrue(perfil().botaoDeEnvioVisivel(texto), "Botão de envio esperado: " + texto);
    }

    @Quando("fecho o perfil")
    public void fechar() {
        perfil().fechar();
    }

    @Entao("o formulário de acesso deve estar fechado")
    public void verificarFechado() {
        assertTrue(perfil().formularioFechado(), "O formulário de acesso deve estar fechado");
    }
}
