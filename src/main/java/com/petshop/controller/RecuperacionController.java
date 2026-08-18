package com.petshop.controller;

import com.petshop.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RecuperacionController {
    private final UsuarioService usuarioService;
    public RecuperacionController(UsuarioService usuarioService) { this.usuarioService = usuarioService; }
    @GetMapping("/recuperar-contrasena") public String formulario() { return "recuperar-contrasena"; }
    @PostMapping("/recuperar-contrasena") public String restablecer(@RequestParam String correo, @RequestParam String password, RedirectAttributes flash) { try { usuarioService.restablecerPassword(correo, password); flash.addFlashAttribute("exito", "Contraseña actualizada. Ya puedes iniciar sesión."); return "redirect:/login"; } catch (IllegalArgumentException e) { flash.addFlashAttribute("error", e.getMessage()); return "redirect:/recuperar-contrasena"; } }
}
