package com.streakbasket.tests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Menu de ajustes do cabeçalho: tema e idioma. */
public class AjustesPage extends BasePage {
    /** O rótulo acessível do botão muda com o idioma; o ícone fica sempre no mesmo lugar do cabeçalho. */
    private static final By BOTAO_AJUSTES = By.cssSelector(
            "button[aria-label='Configurações e informações'],"
                    + "button[aria-label='Settings and information'],"
                    + "button[aria-label='Ajustes e información']");

    public AjustesPage(WebDriver driver) {
        super(driver);
    }

    public void abrirMenu() {
        clicar(BOTAO_AJUSTES);
        visivel(botaoComTexto("PT"));
    }

    public void escolherTema(String tema) {
        clicar(botaoComTexto(tema));
    }

    public void escolherIdioma(String idioma) {
        clicar(botaoComTexto(idioma));
    }

    /** Luminância relativa (0 = preto, 1 = branco) do primeiro fundo opaco atrás da marca do cabeçalho. */
    public double luminanciaDoFundo() {
        Number luminancia = (Number) js("""
                let e = document.querySelector("[role='link']");
                while (e && getComputedStyle(e).backgroundColor === 'rgba(0, 0, 0, 0)') e = e.parentElement;
                const [r, g, b] = getComputedStyle(e || document.body).backgroundColor
                    .match(/\\d+/g).slice(0, 3).map(c => {
                        const v = c / 255;
                        return v <= 0.03928 ? v / 12.92 : Math.pow((v + 0.055) / 1.055, 2.4);
                    });
                return 0.2126 * r + 0.7152 * g + 0.0722 * b;
                """);
        return luminancia.doubleValue();
    }

    public void aguardarFundo(boolean escuro) {
        wait.until(d -> escuro == (luminanciaDoFundo() < 0.2));
    }
}
