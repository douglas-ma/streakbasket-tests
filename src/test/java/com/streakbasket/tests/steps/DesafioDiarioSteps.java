package com.streakbasket.tests.steps;

import com.streakbasket.tests.pages.DesafioDiarioPage;
import com.streakbasket.tests.support.Contexto;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DesafioDiarioSteps {
    private final Contexto contexto;

    public DesafioDiarioSteps(Contexto contexto) {
        this.contexto = contexto;
    }

    private DesafioDiarioPage desafio() {
        return contexto.pagina(DesafioDiarioPage.class);
    }

    @Dado("abro o desafio diário")
    public void abrir() {
        desafio().abrir();
    }

    @Entao("devo ver {int} jogador revelado e {int} ocultos")
    public void verificarJogadores(int revelados, int ocultos) {
        assertAll(
                () -> assertEquals(revelados, desafio().jogadoresRevelados(), "Jogadores revelados"),
                () -> assertEquals(ocultos, desafio().jogadoresOcultos(), "Jogadores ocultos"));
    }

    @Entao("o botão de palpite deve indicar {string}")
    public void verificarTentativas(String restantes) {
        String botao = desafio().botaoConfirmar();
        assertTrue(botao.endsWith(restantes), "Botão de palpite: " + botao);
    }

    @Quando("confirmo o palpite sem preencher os campos")
    public void confirmarVazio() {
        assertEquals("", desafio().campoTime().getDomProperty("value"));
        assertEquals("", desafio().campoTemporada().getDomProperty("value"));
        desafio().confirmarPalpite();
    }

    @Quando("digito {string} no campo de time do palpite")
    public void digitarTime(String busca) {
        desafio().digitarTime(busca);
    }

    @Entao("devo ver a sugestão {string}")
    public void verificarSugestao(String franquia) {
        assertTrue(desafio().sugestaoVisivel(franquia), "Sugestão esperada: " + franquia);
    }

    @Entao("o campo de temporada deve aceitar no máximo {int} caracteres")
    public void verificarLimiteTemporada(int limite) {
        assertEquals(String.valueOf(limite), desafio().campoTemporada().getDomAttribute("maxlength"));
    }

    @Quando("abro as estatísticas do jogador revelado")
    public void abrirEstatisticas() {
        desafio().abrirEstatisticasDoRevelado();
    }

    @Quando("fecho as estatísticas do jogador")
    public void fecharEstatisticas() {
        desafio().fecharEstatisticas();
    }

    @Entao("as estatísticas devem estar fechadas")
    public void verificarEstatisticasFechadas() {
        assertTrue(desafio().estatisticasFechadas(), "A janela de estatísticas deve estar fechada");
    }

    @Quando("abro as instruções do desafio diário")
    public void abrirInstrucoes() {
        desafio().abrirInstrucoes();
    }

    @Quando("fecho as instruções do desafio diário")
    public void fecharInstrucoes() {
        desafio().fecharInstrucoes();
    }

    @Entao("as instruções do desafio diário devem estar fechadas")
    public void verificarInstrucoesFechadas() {
        assertFalse(desafio().instrucoesAbertas(), "As instruções do desafio devem estar fechadas");
    }
}
