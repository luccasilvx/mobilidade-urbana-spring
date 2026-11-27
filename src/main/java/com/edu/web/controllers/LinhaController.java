package com.edu.web.controllers;

import com.edu.web.entities.Linha;
import com.edu.web.entities.Modal;
import com.edu.web.repositories.CidadeRepository;
import com.edu.web.services.LinhaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
public class LinhaController {

    @Autowired
    private LinhaService linhaService;

    @Autowired
    private CidadeRepository cidadeRepository;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/403")
    public String acessoNegado() {
        return "error/403";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/web/linhas";
    }

    @GetMapping("/web/linhas")
    public String listar(Model model) {
        model.addAttribute("linhas", linhaService.listarTodas());
        return "linhas/lista";
    }

    @GetMapping("/web/linhas/nova")
    public String novaLinha(Model model) {
        model.addAttribute("linha", new Linha());
        carregarDependenciasFormulario(model);
        return "linhas/formulario";
    }

    @GetMapping("/web/linhas/editar/{id}")
    public String editarLinha(@PathVariable UUID id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("linha", linhaService.buscarPorId(id));
            carregarDependenciasFormulario(model);
            return "linhas/formulario";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/web/linhas";
        }
    }

    @PostMapping("/web/linhas/salvar")
    public String salvar(@Valid @ModelAttribute("linha") Linha linha,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            carregarDependenciasFormulario(model);
            return "linhas/formulario";
        }

        try {
            linhaService.salvar(linha);
            redirectAttributes.addFlashAttribute("sucesso", "Linha salva com sucesso!");
            return "redirect:/web/linhas";
        } catch (IllegalArgumentException | DataIntegrityViolationException e) {
            model.addAttribute("erro", e instanceof DataIntegrityViolationException
                    ? "Já existe uma linha cadastrada com esse código."
                    : e.getMessage());
            carregarDependenciasFormulario(model);
            return "linhas/formulario";
        }
    }

    @PostMapping("/web/linhas/excluir/{id}")
    public String excluir(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        try {
            linhaService.excluir(id);
            redirectAttributes.addFlashAttribute("sucesso", "Linha removida com sucesso!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("erro",
                    "Não é possível excluir esta linha pois existem viagens ou estações vinculadas a ela.");
        }
        return "redirect:/web/linhas";
    }

    private void carregarDependenciasFormulario(Model model) {
        model.addAttribute("cidades", cidadeRepository.findAll());
        model.addAttribute("modais", Modal.values());
    }
}