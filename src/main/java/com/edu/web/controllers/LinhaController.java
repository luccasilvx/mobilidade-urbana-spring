package com.edu.web.controllers;

import com.edu.web.entities.Linha;
import com.edu.web.services.LinhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/linhas")
public class LinhaController {

    @Autowired
    private LinhaService linhaService;

    @Autowired
    private CidadeRepository cidadeRepository;

    @GetMapping("/login")
    public String login() {
        return "login";
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
        model.addAttribute("cidades", cidadeRepository.findAll());
        model.addAttribute("modais", Modal.values());
        return "linhas/formulario";
    }

    @GetMapping("/web/linhas/editar/{id}")
    public String editarLinha(@PathVariable UUID id, Model model) {
        model.addAttribute("linha", linhaService.buscarPorId(id));
        model.addAttribute("cidades", cidadeRepository.findAll());
        model.addAttribute("modais", Modal.values());
        return "linhas/formulario";
    }

    @PostMapping("/web/linhas/salvar")
    public String salvar(@ModelAttribute Linha linha, RedirectAttributes redirectAttributes) {
        try {
            linhaService.salvar(linha);
            redirectAttributes.addFlashAttribute("sucesso", "Linha salva com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/web/linhas/nova";
        }
        return "redirect:/web/linhas";
    }

    @PostMapping("/web/linhas/excluir/{id}")
    public String excluir(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        linhaService.excluir(id);
        redirectAttributes.addFlashAttribute("sucesso", "Linha removida com sucesso!");
        return "redirect:/web/linhas";
    }
}