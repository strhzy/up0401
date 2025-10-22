package com.strhzy.dbproj.controllers;

import com.strhzy.dbproj.models.Book;
import com.strhzy.dbproj.services.AuthorService;
import com.strhzy.dbproj.services.BookService;
import com.strhzy.dbproj.services.CountryService;
import com.strhzy.dbproj.services.GenreService;
import com.strhzy.dbproj.services.YearService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService service;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final YearService yearService;
    private final CountryService countryService;

    public BookController(BookService service, AuthorService authorService, GenreService genreService, YearService yearService, CountryService countryService) {
        this.service = service;
        this.authorService = authorService;
        this.genreService = genreService;
        this.yearService = yearService;
        this.countryService = countryService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) Long authorId,
                       @RequestParam(required = false) Long genreId,
                       @RequestParam(required = false) Long yearId,
                       @RequestParam(required = false) Long countryId,
                       @RequestParam(required = false) String sort,
                       Model model) {

        // Конвертируем 0 в null для фильтрации
        Long actualAuthorId = (authorId != null && authorId == 0) ? null : authorId;
        Long actualGenreId = (genreId != null && genreId == 0) ? null : genreId;
        Long actualYearId = (yearId != null && yearId == 0) ? null : yearId;
        Long actualCountryId = (countryId != null && countryId == 0) ? null : countryId;
        String actualSort = (sort != null && sort.equals("0")) ? null : sort;

        model.addAttribute("books", service.findAllFiltered(actualAuthorId, actualGenreId, actualYearId, actualCountryId, actualSort));
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("years", yearService.findAll());
        model.addAttribute("countries", countryService.findAll());

        // Сохраняем оригинальные значения для отображения в форме
        model.addAttribute("selectedAuthor", authorId);
        model.addAttribute("selectedGenre", genreId);
        model.addAttribute("selectedYear", yearId);
        model.addAttribute("selectedCountry", countryId);
        model.addAttribute("selectedSort", sort);

        return "booklist";
    }

    @GetMapping("/add")
    public String createForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("years", yearService.findAll());
        model.addAttribute("countries", countryService.findAll());
        return "bookform";
    }

    @PostMapping("/add")
    public String create(@Valid @ModelAttribute("book") Book book, BindingResult br, Model model) {
        if (br.hasErrors()) {
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("genres", genreService.findAll());
            model.addAttribute("years", yearService.findAll());
            model.addAttribute("countries", countryService.findAll());
            return "bookform";
        }
        service.save(book);
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        service.findById(id).ifPresent(b -> model.addAttribute("book", b));
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("years", yearService.findAll());
        model.addAttribute("countries", countryService.findAll());
        return "bookform";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @Valid @ModelAttribute("book") Book book, BindingResult br, Model model) {
        if (br.hasErrors()) {
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("genres", genreService.findAll());
            model.addAttribute("years", yearService.findAll());
            model.addAttribute("countries", countryService.findAll());
            return "bookform";
        }
        book.setId(id);
        service.save(book);
        return "redirect:/books";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/books";
    }
}
