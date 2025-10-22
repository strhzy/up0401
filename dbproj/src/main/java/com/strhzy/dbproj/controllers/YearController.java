package com.strhzy.dbproj.controllers;

import com.strhzy.dbproj.models.Year;
import com.strhzy.dbproj.services.YearService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/years")
public class YearController {
    private final YearService service;

    public YearController(YearService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("years", service.findAll());
        return "yearlist";
    }

    @GetMapping("/add")
    public String createForm(Model model) {
        model.addAttribute("year", new Year());
        return "yearform";
    }

    @PostMapping("/add")
    public String create(@Valid @ModelAttribute("year") Year year, BindingResult br) {
        if (br.hasErrors()) {
            return "yearform";
        }
        service.save(year);
        return "redirect:/years";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        service.findById(id).ifPresent(y -> model.addAttribute("year", y));
        return "yearform";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @Valid @ModelAttribute("year") Year year, BindingResult br) {
        if (br.hasErrors()) {
            return "yearform";
        }
        year.setId(id);
        service.save(year);
        return "redirect:/years";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/years";
    }
}
