package com.edu.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SecurityAndWebIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void usuarioNaoAutenticadoDeveSerRedirecionadoParaLogin() throws Exception {
        mockMvc.perform(get("/web/linhas"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "operador", roles = {"OPERADOR"})
    void operadorPodeConsultarLinhasEViagens() throws Exception {
        mockMvc.perform(get("/web/linhas"))
                .andExpect(status().isOk())
                .andExpect(view().name("linhas/lista"));

        mockMvc.perform(get("/web/viagens"))
                .andExpect(status().isOk())
                .andExpect(view().name("viagens/lista"));
    }

    @Test
    @WithMockUser(username = "operador", roles = {"OPERADOR"})
    void operadorDeveReceber403AoTentarAcessarRotasDeEscritaOuH2() throws Exception {
        mockMvc.perform(get("/web/linhas/nova"))
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/web/viagens/123e4567-e89b-12d3-a456-426614174006/status")
                        .with(csrf())
                        .param("novoStatus", "EM_CURSO"))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/h2-console/"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminConsegueAcessarFormularioESalvarLinhaValida() throws Exception {
        mockMvc.perform(get("/web/linhas/nova"))
                .andExpect(status().isOk())
                .andExpect(view().name("linhas/formulario"));

        mockMvc.perform(post("/web/linhas/salvar")
                        .with(csrf())
                        .param("codigo", "L99001")
                        .param("nome", "Linha Integrada Teste")
                        .param("extensaoKm", "10.5")
                        .param("modal", "ONIBUS")
                        .param("cidade", "123e4567-e89b-12d3-a456-426614174000")
                        .param("estaAtiva", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/web/linhas"))
                .andExpect(flash().attributeExists("sucesso"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveRetornarFormularioComErroQuandoExtensaoForInvalida() throws Exception {
        mockMvc.perform(post("/web/linhas/salvar")
                        .with(csrf())
                        .param("codigo", "L99002")
                        .param("nome", "Linha Invalida")
                        .param("extensaoKm", "-5.0")
                        .param("modal", "ONIBUS")
                        .param("cidade", "123e4567-e89b-12d3-a456-426614174000"))
                .andExpect(status().isOk())
                .andExpect(view().name("linhas/formulario"))
                .andExpect(model().attributeHasFieldErrors("linha", "extensaoKm"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminConsegueTransitarViagemAgendadaParaEmCurso() throws Exception {
        mockMvc.perform(post("/web/viagens/123e4567-e89b-12d3-a456-426614174006/status")
                        .with(csrf())
                        .param("novoStatus", "EM_CURSO"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/web/viagens"))
                .andExpect(flash().attributeExists("sucesso"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveBloquearTransicaoInvalidaDeCanceladaParaConcluidaViaWeb() throws Exception {
        mockMvc.perform(post("/web/viagens/123e4567-e89b-12d3-a456-426614174007/status")
                        .with(csrf())
                        .param("novoStatus", "CONCLUIDA"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/web/viagens"))
                .andExpect(flash().attributeExists("erro"));
    }
}