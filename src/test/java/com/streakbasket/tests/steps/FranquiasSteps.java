package com.streakbasket.tests.steps;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.streakbasket.tests.pages.FranquiasPage;
import com.streakbasket.tests.support.Contexto;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FranquiasSteps {
    private final Contexto contexto;

    public FranquiasSteps(Contexto contexto) {
        this.contexto = contexto;
    }

    private FranquiasPage franquias() {
        return contexto.pagina(FranquiasPage.class);
    }

    @Quando("abro o guia de franquias")
    public void abrirGuia() {
        franquias().abrirPeloRodape();
    }

    @Quando("acesso o endereço {string}")
    public void acessarEndereco(String caminho) {
        franquias().abrirEndereco(caminho);
    }

    @Entao("devo ver pelo menos {int} franquias")
    public void verificarQuantidade(int minimo) {
        franquias().aguardarListaDeFranquias();
        int total = franquias().franquias().size();
        assertTrue(total >= minimo, "Franquias listadas: " + total);
    }

    @Entao("o catálogo deve conter as franquias:")
    public void verificarFranquias(List<String> esperadas) {
        List<String> listadas = franquias().franquias();
        assertAll(esperadas.stream().map(f -> (Executable) () ->
                assertTrue(listadas.contains(f), "Franquia ausente do catálogo: " + f)));
    }

    @Quando("abro a franquia {string}")
    public void abrirFranquia(String franquia) {
        franquias().abrirFranquia(franquia);
    }

    @Entao("devo ver as temporadas {string}")
    public void verificarTemporadas(String temporadas) {
        List<String> listadas = franquias().temporadas();
        assertAll(Arrays.stream(temporadas.split(",\\s*")).map(t -> (Executable) () ->
                assertTrue(listadas.contains(t), "Temporada ausente: " + t)));
    }

    @Quando("abro a temporada {string}")
    public void abrirTemporada(String temporada) {
        franquias().abrirTemporada(temporada);
    }

    @Entao("o elenco deve conter os jogadores:")
    public void verificarElenco(List<Map<String, String>> jogadores) {
        assertAll(jogadores.stream().map(j -> (Executable) () -> assertTrue(
                franquias().jogadorNoElenco(j.get("nome"), j.get("posicao"), j.get("overall")),
                "Jogador ausente do elenco: " + j)));
    }

    @Quando("abro as estatísticas de {string}")
    public void abrirEstatisticas(String nome) {
        franquias().abrirEstatisticas(nome);
    }

    @Quando("fecho as estatísticas do elenco")
    public void fecharEstatisticas() {
        franquias().fecharEstatisticas();
    }

    @Quando("volto pela trilha para {string}")
    public void voltarPelaTrilha(String item) {
        franquias().voltarPelaTrilha(item);
    }
}
