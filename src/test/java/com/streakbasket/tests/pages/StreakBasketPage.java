package com.streakbasket.tests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class StreakBasketPage extends BasePage {
    private static final By ESCOLHER_MODO = botaoComTexto("ESCOLHER MODO");
    private static final By EMAIL = By.cssSelector("input[aria-label='E-mail da conta']");
    private static final By SENHA = By.cssSelector("input[aria-label='Senha da conta']");

    public StreakBasketPage(WebDriver driver) {
        super(driver);
    }

    public void abrir() {
        driver.get(baseUrl());
        wait.until(ExpectedConditions.visibilityOfElementLocated(ESCOLHER_MODO));
    }

    public void recarregar() {
        driver.navigate().refresh();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button[aria-label]")));
    }

    public String titulo() {
        return driver.getTitle();
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

    public void aguardarModalFechado() {
        wait.until(d -> modalFechado());
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

    public void abrirMeuRanking() {
        clicar(botaoComTexto("MEU RANKING"));
        aguardarTextoNoModal("Campanhas salvas por modo de jogo");
    }

    public void abrirSobre() {
        clicar(By.cssSelector("button[aria-label='Sobre o projeto']"));
        aguardarTextoNoModal("SOBRE O PROJETO");
    }

    public boolean linkVisivel(String rotulo) {
        return visivel(By.cssSelector("[role='link'][aria-label='" + rotulo + "']")).isDisplayed();
    }
}
