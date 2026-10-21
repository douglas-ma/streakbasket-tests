package com.streakbasket.tests.steps;

import com.streakbasket.tests.pages.DraftPage;
import com.streakbasket.tests.pages.DraftPage.Jogador;
import com.streakbasket.tests.support.Contexto;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DraftSteps {
    private final Contexto contexto;

    public DraftSteps(Contexto contexto) {
        this.contexto = contexto;
    }

    private DraftPage draft() {
        return contexto.pagina(DraftPage.class);
    }

    @Quando("preparo o draft em {string} na dificuldade {string}")
    public void prepararDraft(String modo, String dificuldade) {
        draft().escolherOpcao("MODO DE JOGO", modo);
        draft().escolherOpcao("DIFICULDADE", dificuldade);
        draft().confirmarEBuscarFranquia();
    }

    @Entao("devo ver o elenco de uma franquia sorteada")
    public void verificarElenco() {
        String franquia = draft().franquiaSorteada();
        assertAll(
                () -> assertTrue(franquia.matches("[A-Z]{3} - .+"), "Franquia sorteada: " + franquia),
                () -> assertFalse(draft().aguardarElenco().isEmpty(), "O elenco sorteado deve ter jogadores"));
    }

    @Quando("sorteio outro time")
    public void sortearOutroTime() {
        draft().sortearOutroTime();
    }

    @Quando("escalo o primeiro jogador do elenco sorteado")
    public void escalarPrimeiro() {
        draft().escalarPrimeiroJogador();
    }

    @Entao("o jogador escalado deve ocupar sua posição na quadra")
    public void verificarJogadorEscalado() {
        Jogador jogador = draft().ultimoEscalado();
        assertTrue(draft().jogadorNaPosicao(jogador), "Jogador escalado: " + jogador);
    }

    @Entao("devem restar {int} posições vazias")
    public void verificarPosicoesVazias(int quantidade) {
        assertEquals(quantidade, draft().posicoesVazias().size());
    }

    @Quando("monto o quinteto completo")
    public void montarQuinteto() {
        draft().montarQuintetoCompleto();
        assertEquals(0, draft().posicoesVazias().size(), "Todas as posições devem estar preenchidas");
    }

    @Quando("busco um adversário")
    public void buscarAdversario() {
        draft().buscarAdversario();
    }

    @Quando("simulo a partida")
    public void simularPartida() {
        draft().simularPartida();
    }

    @Entao("devo ver o placar final com os quatro quartos")
    public void verificarPlacarFinal() {
        assertAll(
                () -> assertTrue(draft().textoVisivel("FINAL")),
                () -> assertTrue(draft().textoVisivel("Q1")),
                () -> assertTrue(draft().textoVisivel("Q2")),
                () -> assertTrue(draft().textoVisivel("Q3")),
                () -> assertTrue(draft().textoVisivel("Q4")),
                () -> assertTrue(draft().textoVisivel("FIM DE JOGO")));
    }
}
