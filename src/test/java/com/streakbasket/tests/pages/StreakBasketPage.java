package com.streakbasket.tests.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class StreakBasketPage {
    private static final By MODAL = By.cssSelector("[role='dialog'][aria-modal='true']");
    private static final By ESCOLHER_MODO = botaoComTexto("ESCOLHER MODO");
    private static final By EMAIL = By.cssSelector("input[aria-label='E-mail da conta']");
    private static final By SENHA = By.cssSelector("input[aria-label='Senha da conta']");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public StreakBasketPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver,
                Duration.ofSeconds(Long.getLong("timeout.seconds", 20L)));
    }

    public void abrir() {
        driver.get(System.getProperty("base.url", "https://streakbasket.vercel.app"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(ESCOLHER_MODO));
    }

    public String titulo() {
        return driver.getTitle();
    }

    public boolean textoVisivel(String texto) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[normalize-space(text())='" + texto + "']"))).isDisplayed();
    }

    public boolean botaoVisivel(String texto) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(botaoComTexto(texto))).isDisplayed();
    }

    public void abrirInstrucoes() {
        clicar(By.cssSelector("button[aria-label='Como jogar']"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("button[aria-label='Fechar instruções']")));
    }

    public String textoDoModal() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(MODAL)).getText();
    }

    public void fecharInstrucoes() {
        clicar(botaoComTexto("ENTENDI"));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(MODAL));
    }

    public boolean modalFechado() {
        return driver.findElements(MODAL).stream().noneMatch(WebElement::isDisplayed);
    }

    public void escolherModo(String modo) {
        clicar(ESCOLHER_MODO);
        clicar(botaoComTexto(modo));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("button[aria-label='Voltar ao menu']")));
    }

    public boolean posicaoVazia(String posicao) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("button[aria-label='" + posicao + ": posição vazia']"))).isDisplayed();
    }

    public void abrirPerfil() {
        clicar(By.cssSelector("button[aria-label='Entrar para sincronizar progresso']"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL));
    }

    public WebElement campoEmail() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL));
    }

    public WebElement campoSenha() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(SENHA));
    }

    public void abrirRanking() {
        clicar(By.cssSelector("button[aria-label='Ranking geral']"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("button[aria-label='Atualizar ranking geral']")));
    }

    private void clicar(By seletor) {
        wait.until(ExpectedConditions.elementToBeClickable(seletor)).click();
    }

    private static By botaoComTexto(String texto) {
        return By.xpath("//button[normalize-space(.)='" + texto
                + "' or .//*[normalize-space(text())='" + texto + "']]");
    }
}
