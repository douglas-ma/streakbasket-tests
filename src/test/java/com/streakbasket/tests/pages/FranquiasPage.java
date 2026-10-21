package com.streakbasket.tests.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/** Guia de elencos: /teams, /teams/{sigla} e /teams/{sigla}/{temporada}. */
public class FranquiasPage extends BasePage {
    private static final By FRANQUIAS = By.cssSelector("[role='link'][aria-label$='. Ver temporadas']");
    private static final By TEMPORADAS = By.cssSelector("[role='link'][aria-label$='. Ver elenco']");
    private static final By JOGADORES = By.cssSelector("button[aria-label$='. Ver estatísticas']");
    private static final By FECHAR_ESTATISTICAS = By.cssSelector("button[aria-label='Fechar estatísticas']");

    public FranquiasPage(WebDriver driver) {
        super(driver);
    }

    public void abrirPeloRodape() {
        clicar(By.cssSelector("[role='link'][aria-label='Ver franquias']"));
        aguardarUrlTerminadaEm("/teams");
        aguardarTodos(FRANQUIAS);
    }

    public void abrirEndereco(String caminho) {
        driver.get(baseUrl() + caminho);
    }

    public List<String> franquias() {
        return rotulos(FRANQUIAS).stream().map(r -> r.replace(". Ver temporadas", "")).toList();
    }

    public void abrirFranquia(String franquia) {
        clicar(By.cssSelector("[role='link'][aria-label=\"" + franquia + ". Ver temporadas\"]"));
        aguardarTodos(TEMPORADAS);
    }

    public List<String> temporadas() {
        return rotulos(TEMPORADAS).stream()
                .map(r -> r.replace("Temporada ", "").replace(". Ver elenco", ""))
                .toList();
    }

    public void abrirTemporada(String temporada) {
        clicar(By.cssSelector("[role='link'][aria-label='Temporada " + temporada + ". Ver elenco']"));
        aguardarTodos(JOGADORES);
    }

    public List<String> jogadores() {
        return rotulos(JOGADORES);
    }

    /** Rótulo do jogador: "Camisa 34, Paul Pierce, SF, overall 90. Ver estatísticas". */
    public boolean jogadorNoElenco(String nome, String posicao, String overall) {
        String trecho = nome + ", " + posicao + ", overall " + overall + ".";
        return jogadores().stream().anyMatch(r -> r.contains(trecho));
    }

    public void abrirEstatisticas(String nome) {
        clicar(By.cssSelector("button[aria-label*=\", " + nome + ", \"]"));
        visivel(FECHAR_ESTATISTICAS);
    }

    /** O fundo da janela também se chama "Fechar estatísticas"; o X é o último botão visível. */
    public void fecharEstatisticas() {
        List<WebElement> botoes = driver.findElements(FECHAR_ESTATISTICAS).stream()
                .filter(WebElement::isDisplayed).toList();
        clicar(botoes.get(botoes.size() - 1));
        wait.until(d -> d.findElements(FECHAR_ESTATISTICAS).stream().noneMatch(WebElement::isDisplayed));
    }

    /**
     * O primeiro link visível com o texto é o da trilha; o rodapé repete "FRANQUIAS".
     * As telas visitadas antes continuam no DOM, ocultas, por isso a busca ignora as invisíveis.
     */
    public void voltarPelaTrilha(String item) {
        By seletor = By.xpath("//*[@role='link'][normalize-space(.)=" + literal(item) + "]");
        WebElement link = wait.until(d -> d.findElements(seletor).stream()
                .filter(WebElement::isDisplayed).findFirst().orElse(null));
        clicar(link);
    }

    public void aguardarListaDeFranquias() {
        aguardarTodos(FRANQUIAS);
    }

    private List<String> rotulos(By seletor) {
        return aguardarTodos(seletor).stream().map(e -> e.getDomAttribute("aria-label")).toList();
    }

    private List<WebElement> aguardarTodos(By seletor) {
        return wait.until(d -> {
            List<WebElement> elementos = d.findElements(seletor);
            return elementos.isEmpty() ? null : elementos;
        });
    }
}
