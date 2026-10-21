package com.streakbasket.tests.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/** Desafio diário: descobrir o time e a temporada a partir dos jogadores revelados. */
public class DesafioDiarioPage extends BasePage {
    private static final By CAMPO_TIME = By.cssSelector("input[aria-label='Time do palpite']");
    private static final By CAMPO_TEMPORADA = By.cssSelector("input[aria-label='Temporada do palpite']");
    private static final By JOGADORES = By.cssSelector(
            "button[aria-label^='PG: '], button[aria-label^='SG: '], button[aria-label^='SF: '],"
                    + " button[aria-label^='PF: '], button[aria-label^='C: ']");
    private static final By FECHAR_ESTATISTICAS = By.cssSelector("button[aria-label='Fechar estatísticas']");

    public DesafioDiarioPage(WebDriver driver) {
        super(driver);
    }

    public void abrir() {
        clicar(By.cssSelector("button[aria-label='Abrir desafio diário']"));
        visivel(CAMPO_TIME);
    }

    public List<WebElement> jogadores() {
        return driver.findElements(JOGADORES).stream().filter(WebElement::isDisplayed).toList();
    }

    public long jogadoresOcultos() {
        return jogadores().stream().filter(j -> j.getDomAttribute("aria-label").endsWith(": jogador oculto")).count();
    }

    public long jogadoresRevelados() {
        return jogadores().size() - jogadoresOcultos();
    }

    public WebElement campoTime() {
        return visivel(CAMPO_TIME);
    }

    public WebElement campoTemporada() {
        return visivel(CAMPO_TEMPORADA);
    }

    public void confirmarPalpite() {
        clicar(By.xpath("//button[starts-with(normalize-space(.), 'CONFIRMAR PALPITE')]"));
    }

    public String botaoConfirmar() {
        return visivel(By.xpath("//button[starts-with(normalize-space(.), 'CONFIRMAR PALPITE')]")).getText();
    }

    public void digitarTime(String busca) {
        WebElement campo = campoTime();
        campo.clear();
        campo.sendKeys(busca);
    }

    public boolean sugestaoVisivel(String franquia) {
        return visivel(By.cssSelector("button[aria-label='Selecionar " + franquia + "']")).isDisplayed();
    }

    public void abrirEstatisticasDoRevelado() {
        WebElement revelado = jogadores().stream()
                .filter(j -> !j.getDomAttribute("aria-label").endsWith(": jogador oculto"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Nenhum jogador revelado no desafio"));
        clicar(revelado);
        visivel(FECHAR_ESTATISTICAS);
    }

    /** O fundo da janela também se chama "Fechar estatísticas"; o X é o último botão visível. */
    public void fecharEstatisticas() {
        List<WebElement> botoes = driver.findElements(FECHAR_ESTATISTICAS).stream()
                .filter(WebElement::isDisplayed).toList();
        clicar(botoes.get(botoes.size() - 1));
        wait.until(d -> d.findElements(FECHAR_ESTATISTICAS).stream().noneMatch(WebElement::isDisplayed));
    }

    public boolean estatisticasFechadas() {
        return driver.findElements(FECHAR_ESTATISTICAS).stream().noneMatch(WebElement::isDisplayed);
    }

    public void abrirInstrucoes() {
        clicar(By.cssSelector("button[aria-label='Como jogar o desafio diário']"));
        aguardarTextoNoModal("Verde: resposta certa.");
    }

    public void fecharInstrucoes() {
        clicarNoModal("ENTENDI");
        wait.until(d -> !instrucoesAbertas());
    }

    public boolean instrucoesAbertas() {
        return ultimoModal().getText().contains("Verde: resposta certa.");
    }
}
