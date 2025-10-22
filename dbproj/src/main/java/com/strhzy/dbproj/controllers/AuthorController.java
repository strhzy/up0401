package com.strhzy.dbproj.controllers;

import com.strhzy.dbproj.models.Author;
import com.strhzy.dbproj.services.AuthorService;
import com.strhzy.dbproj.services.CountryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService service;
    private final CountryService countryService;

    public AuthorController(AuthorService service, CountryService countryService) {
        this.service = service;
        this.countryService = countryService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("authors", service.findAll());
        return "authorlist";
    }

    @GetMapping("/add")
    public String createForm(Model model) {
        model.addAttribute("author", new Author());
        model.addAttribute("countries", countryService.findAll());
        return "authorform";
    }

    @PostMapping("/add")
    public String create(@Valid @ModelAttribute("author") Author author, BindingResult br, Model model) {
        if (br.hasErrors()) {
            model.addAttribute("countries", countryService.findAll());
            return "authorform";
        }
        service.save(author);
        return "redirect:/authors";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        service.findById(id).ifPresent(a -> model.addAttribute("author", a));
        model.addAttribute("countries", countryService.findAll());
        return "authorform";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @Valid @ModelAttribute("author") Author author, BindingResult br, Model model) {
        if (br.hasErrors()) {
            model.addAttribute("countries", countryService.findAll());
            return "authorform";
        }
        author.setId(id);
        service.save(author);
        return "redirect:/authors";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/authors";
    }
}
