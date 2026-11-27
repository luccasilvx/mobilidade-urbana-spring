package com.edu.web.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public String handleBusinessException(RuntimeException ex, Model model) {
        model.addAttribute("titulo", "Operação Inválida");
        model.addAttribute("mensagem", ex.getMessage());
        return "error/erro";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        model.addAttribute("titulo", "Erro Interno");
        model.addAttribute("mensagem", "Ocorreu um erro inesperado ao processar sua solicitação.");
        return "error/erro";
    }
}