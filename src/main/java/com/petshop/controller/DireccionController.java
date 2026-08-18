package com.petshop.controller;

import com.petshop.domain.Direccion;
import com.petshop.service.DireccionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DireccionController {
    private final DireccionService direccionService;
    public DireccionController(DireccionService direccionService) { this.direccionService = direccionService; }
    @GetMapping("/direcciones") public String listado(Authentication auth, Model model) { model.addAttribute("direcciones", direccionService.obtenerDirecciones(auth.getName())); return "direcciones/listado"; }
    @GetMapping("/direcciones/nueva") public String nueva(Model model) { Direccion direccion = new Direccion(); direccion.setPredeterminada(true); model.addAttribute("direccion", direccion); return "direcciones/formulario"; }
    @GetMapping("/direcciones/{id}/editar") public String editar(Authentication auth, @PathVariable Integer id, Model model) { Direccion direccion = direccionService.obtenerDireccion(auth.getName(), id); if (direccion == null) return "redirect:/direcciones"; model.addAttribute("direccion", direccion); return "direcciones/formulario"; }
    @PostMapping("/direcciones/guardar") public String guardar(Authentication auth, Direccion direccion, RedirectAttributes flash) { try { direccionService.guardar(auth.getName(), direccion); flash.addFlashAttribute("exito", "Dirección guardada."); } catch (IllegalArgumentException e) { flash.addFlashAttribute("error", e.getMessage()); } return "redirect:/direcciones"; }
    @PostMapping("/direcciones/{id}/eliminar") public String eliminar(Authentication auth, @PathVariable Integer id, RedirectAttributes flash) { try { direccionService.eliminar(auth.getName(), id); flash.addFlashAttribute("exito", "Dirección eliminada."); } catch (IllegalArgumentException e) { flash.addFlashAttribute("error", e.getMessage()); } return "redirect:/direcciones"; }
}
