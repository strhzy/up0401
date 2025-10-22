package com.strhzy.dbproj.controllers;

import com.strhzy.dbproj.models.Genre;
import com.strhzy.dbproj.services.GenreService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/genres")
public class GenreController {
    private final GenreService service;

    public GenreController(GenreService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("genres", service.findAll());
        return "genrelist";
    }

    @GetMapping("/add")
    public String createForm(Model model) {
        model.addAttribute("genre", new Genre());
        return "genreform";
    }

    @PostMapping("/add")
    public String create(@Valid @ModelAttribute("genre") Genre genre, BindingResult br) {
        if (br.hasErrors()) {
            return "genreform";
        }
        service.save(genre);
        return "redirect:/genres";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        service.findById(id).ifPresent(g -> model.addAttribute("genre", g));
        return "genreform";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @Valid @ModelAttribute("genre") Genre genre, BindingResult br) {
        if (br.hasErrors()) {
            return "genreform";
        }
        genre.setId(id);
        service.save(genre);
        return "redirect:/genres";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/genres";
    }
}
