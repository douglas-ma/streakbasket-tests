package com.streakbasket.tests.pages;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Draft do Modo Streak: sorteio de franquias, escalação do quinteto e simulação da partida. */
public class DraftPage extends BasePage {
    private static final By CONFIRMAR = botaoComTexto("CONFIRMAR E BUSCAR FRANQUIA");
    private static final By CARTAS = By.cssSelector("#draft-player-roster > div[tabindex='0']");
    private static final By POSICOES_VAZIAS = By.cssSelector("button[aria-label$=': posição vazia']");

    /** Jogador escolhido pelo último passo de escalação. */
    private Jogador ultimoEscalado;

    public record Jogador(String posicao, String nome) {
    }

    public DraftPage(WebDriver driver) {
        super(driver);
    }

    public boolean confirmarHabilitado() {
        return visivel(CONFIRMAR).isEnabled();
    }

    public void confirmarEBuscarFranquia() {
        clicar(CONFIRMAR);
        aguardarElenco();
    }

    /** Título da franquia sorteada, por exemplo "CHI - Chicago Bulls". */
    public String franquiaSorteada() {
        return visivel(By.xpath("//*[@id='draft-player-roster']/preceding::*"
                + "[contains(normalize-space(text()), ' - ')][1]")).getText();
    }

    public List<WebElement> aguardarElenco() {
        return wait.until(d -> {
            List<WebElement> cartas = d.findElements(CARTAS).stream().filter(WebElement::isDisplayed).toList();
            return cartas.isEmpty() ? null : cartas;
        });
    }

    public void sortearOutroTime() {
        WebElement primeiraCarta = aguardarElenco().get(0);
        clicar(controleComTexto("Sortear Outro Time"));
        wait.until(ExpectedConditions.stalenessOf(primeiraCarta));
        aguardarElenco();
    }

    public List<String> posicoesVazias() {
        return driver.findElements(POSICOES_VAZIAS).stream()
                .filter(WebElement::isDisplayed)
                .map(b -> b.getDomAttribute("aria-label").split(":")[0])
                .distinct()
                .toList();
    }

    /** Escala o primeiro jogador do elenco sorteado na posição dele. */
    public Jogador escalarPrimeiroJogador() {
        return escalar(aguardarElenco().get(0));
    }

    /** Preenche as posições vazias; sorteia outro time quando o atual não tem quem sirva. */
    public void montarQuintetoCompleto() {
        for (int rodada = 0; rodada < 10 && !posicoesVazias().isEmpty(); rodada++) {
            List<String> vazias = posicoesVazias();
            Optional<WebElement> carta = aguardarElenco().stream()
                    .filter(c -> vazias.contains(lerJogador(c).posicao()))
                    .findFirst();
            if (carta.isPresent()) {
                escalar(carta.get());
            } else {
                sortearOutroTime();
            }
        }
    }

    public Jogador ultimoEscalado() {
        return ultimoEscalado;
    }

    public boolean jogadorNaPosicao(Jogador jogador) {
        return visivel(By.cssSelector("button[aria-label=\"" + jogador.posicao() + ": " + jogador.nome() + "\"]"))
                .isDisplayed();
    }

    public void buscarAdversario() {
        clicar(controleComTexto("BUSCAR ADVERSÁRIO"));
        visivel(controleComTexto("INICIAR PARTIDA"));
    }

    /** Inicia a partida e pula a animação quando o atalho aparece. */
    public void simularPartida() {
        clicar(controleComTexto("INICIAR PARTIDA"));
        By pular = By.xpath("//button[normalize-space(.)='PULAR PARA O FIM']"
                + " | //div[@tabindex='0'][normalize-space(.)='PULAR PARA O FIM']");
        clicarSeAparecer(pular, Duration.ofSeconds(5));
        new WebDriverWait(driver, Duration.ofSeconds(Long.getLong("timeout.seconds", 20L) * 4))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[normalize-space(text())='FIM DE JOGO']")));
    }

    private Jogador escalar(WebElement carta) {
        Jogador jogador = lerJogador(carta);
        clicar(carta);
        clicar(By.cssSelector("button[aria-label='Escalar jogador na posição " + jogador.posicao() + "']"));
        jogadorNaPosicao(jogador);
        if (!posicoesVazias().isEmpty()) {
            wait.until(ExpectedConditions.stalenessOf(carta));
        }
        ultimoEscalado = jogador;
        return jogador;
    }

    /** O texto de cada carta tem três linhas: posição, nome e overall. */
    private static Jogador lerJogador(WebElement carta) {
        String[] linhas = carta.getText().trim().split("\\R");
        return new Jogador(linhas[0].trim(), linhas[1].trim());
    }
}
