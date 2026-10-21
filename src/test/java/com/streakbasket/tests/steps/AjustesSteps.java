package com.streakbasket.tests.steps;

import com.streakbasket.tests.pages.AjustesPage;
import com.streakbasket.tests.support.Contexto;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AjustesSteps {
    private final Contexto contexto;

    public AjustesSteps(Contexto contexto) {
        this.contexto = contexto;
    }

    private AjustesPage ajustes() {
        return contexto.pagina(AjustesPage.class);
    }

    @Dado("abro o menu de ajustes")
    public void abrirMenu() {
        ajustes().abrirMenu();
    }

    @Quando("escolho o tema {string}")
    public void escolherTema(String tema) {
        ajustes().escolherTema(tema);
    }

    @Quando("escolho o idioma {string}")
    public void escolherIdioma(String idioma) {
        ajustes().escolherIdioma(idioma);
    }

    @Entao("o fundo da página deve ficar escuro")
    public void verificarFundoEscuro() {
        ajustes().aguardarFundo(true);
        double luminancia = ajustes().luminanciaDoFundo();
        assertTrue(luminancia < 0.2, "Luminância do fundo: " + luminancia);
    }

    @Entao("o fundo da página deve ficar claro")
    public void verificarFundoClaro() {
        ajustes().aguardarFundo(false);
        double luminancia = ajustes().luminanciaDoFundo();
        assertTrue(luminancia >= 0.2, "Luminância do fundo: " + luminancia);
    }
}
