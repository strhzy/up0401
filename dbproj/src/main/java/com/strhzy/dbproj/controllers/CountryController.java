package com.strhzy.dbproj.controllers;

import com.strhzy.dbproj.models.Country;
import com.strhzy.dbproj.services.CountryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/countries")
public class CountryController {
    private final CountryService service;

    public CountryController(CountryService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("countries", service.findAll());
        return "countrylist";
    }

    @GetMapping("/add")
    public String createForm(Model model) {
        model.addAttribute("country", new Country());
        return "countryform";
    }

    @PostMapping("/add")
    public String create(@Valid @ModelAttribute("country") Country country, BindingResult br) {
        if (br.hasErrors()) {
            return "countryform";
        }
        service.save(country);
        return "redirect:/countries";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        service.findById(id).ifPresent(c -> model.addAttribute("country", c));
        return "countryform";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @Valid @ModelAttribute("country") Country country, BindingResult br) {
        if (br.hasErrors()) {
            return "countryform";
        }
        country.setId(id);
        service.save(country);
        return "redirect:/countries";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/countries";
    }
}
