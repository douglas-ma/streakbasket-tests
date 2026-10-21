package com.streakbasket.tests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/** Tela multiplayer. Os testes não criam salas: param na configuração. */
public class MultiplayerPage extends BasePage {
    public MultiplayerPage(WebDriver driver) {
        super(driver);
    }

    public WebElement campoNome() {
        return visivel(By.cssSelector("input[aria-label='Seu nome na sala']"));
    }

    public WebElement campoCodigo() {
        return visivel(By.cssSelector("input[aria-label='Código da sala']"));
    }

    public boolean botaoEntrarNaSalaVisivel() {
        return visivel(By.cssSelector("button[aria-label='Entrar na sala']")).isDisplayed();
    }

    public void abrirCriacaoDeSala() {
        clicar(botaoComTexto("CRIAR UMA SALA"));
        aguardarTextoNoModal("NÚMERO DE JOGADORES");
    }

    public void voltarAsOpcoes() {
        clicarNoModal("Voltar às opções");
        aguardarTextoNoModal("SELECIONE UMA OPÇÃO");
    }
}
