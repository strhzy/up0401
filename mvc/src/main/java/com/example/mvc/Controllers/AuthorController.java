package com.example.mvc.Controllers;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.example.mvc.Models.Author;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/authors")
@Valid
public class AuthorController {
    private final List<Author> authors = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @GetMapping("/")
    public String getAllAuthors(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String sort,
            Model model) {
        List<Author> result = new ArrayList<>(authors);

        if (q != null && !q.trim().isEmpty()) {
            String qq = q.trim().toLowerCase();
            result = result.stream().filter(a -> {
                if (a.getName() != null && a.getName().toLowerCase().contains(qq)) return true;
                if (a.getSurname() != null && a.getSurname().toLowerCase().contains(qq)) return true;
                if (a.getLastname() != null && a.getLastname().toLowerCase().contains(qq)) return true;
                try {
                    Long id = Long.parseLong(qq);
                    if (Objects.equals(a.getId(), id)) return true;
                } catch (NumberFormatException ignored) {
                }
                return false;
            }).collect(Collectors.toList());
        }

        if (sort != null && !sort.trim().isEmpty()) {
            Comparator<Author> comparator = null;
            String[] parts = sort.split(",");
            for (String part : parts) {
                String[] p = part.trim().split(":", 2);
                String field = p[0].trim();
                String dir = p.length > 1 ? p[1].trim().toLowerCase() : "asc";
                Comparator<Author> c = null;
                switch (field) {
                    case "name":
                        c = Comparator.comparing(a -> Optional.ofNullable(a.getName()).orElse(""), String.CASE_INSENSITIVE_ORDER);
                        break;
                    case "surname":
                        c = Comparator.comparing(a -> Optional.ofNullable(a.getSurname()).orElse(""), String.CASE_INSENSITIVE_ORDER);
                        break;
                    case "lastname":
                        c = Comparator.comparing(a -> Optional.ofNullable(a.getLastname()).orElse(""), String.CASE_INSENSITIVE_ORDER);
                        break;
                    case "id":
                        c = Comparator.comparing(a -> Optional.ofNullable(a.getId()).orElse(-1L));
                        break;
                    default:
                        // skip
                }
                if (c != null) {
                    if ("desc".equals(dir)) c = c.reversed();
                    comparator = comparator == null ? c : comparator.thenComparing(c);
                }
            }
            if (comparator != null) result.sort(comparator);
        }

        model.addAttribute("authors", result);
        return "authorList";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("author", new Author());
        return "AddAuthor";
    }

    @PostMapping("/add")
    public String addAuthorForm(@Valid @ModelAttribute("author") Author author, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("author", author);
            return "AddAuthor";
        }
        author.setId(idCounter.getAndIncrement());
        authors.add(author);
        return "redirect:/authors/";
    }

    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Author addAuthorJson(@Valid @RequestBody Author author) {
        author.setId(idCounter.getAndIncrement());
        authors.add(author);
        return author;
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Author author = authors.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Author not found"));
        model.addAttribute("author", author);
        return "EditAuthor";
    }

    @PostMapping("/edit/{id}")
    public String editAuthorForm(@PathVariable Long id, @Valid @ModelAttribute("author") Author updatedAuthor, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("author", updatedAuthor);
            return "EditAuthor";
        }
        Author author = authors.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Author not found"));
        author.setName(updatedAuthor.getName());
        author.setSurname(updatedAuthor.getSurname());
        author.setLastname(updatedAuthor.getLastname());
        return "redirect:/authors/";
    }

    @PutMapping("/edit/{id}")
    @ResponseBody
    public Author editAuthor(@PathVariable Long id, @Valid @RequestBody Author updatedAuthor) {
        Author author = authors.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Author not found"));
        author.setName(updatedAuthor.getName());
        author.setSurname(updatedAuthor.getSurname());
        author.setLastname(updatedAuthor.getLastname());
        return author;
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public void deleteAuthor(@PathVariable Long id) {
        authors.removeIf(a -> a.getId().equals(id));
    }

    @PostMapping("/delete/{id}")
    public String deleteAuthorForm(@PathVariable Long id) {
        authors.removeIf(a -> a.getId().equals(id));
        return "redirect:/authors/";
    }
}
