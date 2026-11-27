package com.edu.web.controllers;

import com.edu.web.entities.StatusViagem;
import com.edu.web.services.ViagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/web/viagens")
public class ViagemController {

    @Autowired
    private ViagemService viagemService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("viagens", viagemService.listarTodas());
        model.addAttribute("statusList", StatusViagem.values());
        return "viagens/lista";
    }

    @PostMapping("/{id}/status")
    public String atualizarStatus(@PathVariable UUID id,
                                  @RequestParam StatusViagem novoStatus,
                                  RedirectAttributes redirectAttributes) {
        try {
            viagemService.atualizarStatus(id, novoStatus);
            redirectAttributes.addFlashAttribute("sucesso", "Status da viagem atualizado para " + novoStatus + ".");
        } catch (IllegalStateException | IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/web/viagens";
    }
}