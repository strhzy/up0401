package com.example.mvc.Controllers;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.example.mvc.Models.*;

import jakarta.validation.Valid;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

@Controller
@RequestMapping("/books")
@Valid
public class BookController {
    private final List<Book> books = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @GetMapping("/")
    public String getAllBooks(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String sort,
            Model model) {
        List<Book> result = new ArrayList<>(books);

        if (q != null && !q.trim().isEmpty()) {
            String qq = q.trim().toLowerCase();
            result = result.stream().filter(b -> {
                if (b.getTitle() != null && b.getTitle().toLowerCase().contains(qq)) return true;
                try {
                    Long aid = Long.parseLong(qq);
                    if (Objects.equals(b.getAuthor_id(), aid)) return true;
                    if (Objects.equals(b.getId(), aid)) return true;
                } catch (NumberFormatException ignored) {
                }
                try {
                    Integer pc = Integer.parseInt(qq);
                    if (Objects.equals(b.getPageCount(), pc)) return true;
                } catch (NumberFormatException ignored) {
                }
                return false;
            }).collect(Collectors.toList());
        }

        if (sort != null && !sort.trim().isEmpty()) {
            Comparator<Book> comparator = null;
            String[] parts = sort.split(",");
            for (String part : parts) {
                String[] p = part.trim().split(":", 2);
                String field = p[0].trim();
                String dir = p.length > 1 ? p[1].trim().toLowerCase() : "asc";
                Comparator<Book> c = null;
                switch (field) {
                    case "title":
                        c = Comparator.comparing(b -> Optional.ofNullable(b.getTitle()).orElse(""), String.CASE_INSENSITIVE_ORDER);
                        break;
                    case "author_id":
                        c = Comparator.comparing(b -> Optional.ofNullable(b.getAuthor_id()).orElse(-1L));
                        break;
                    case "pageCount":
                        c = Comparator.comparing(b -> Optional.ofNullable(b.getPageCount()).orElse(-1));
                        break;
                    case "id":
                        c = Comparator.comparing(b -> Optional.ofNullable(b.getId()).orElse(-1L));
                        break;
                    default:
                }
                if (c != null) {
                    if ("desc".equals(dir)) c = c.reversed();
                    comparator = comparator == null ? c : comparator.thenComparing(c);
                }
            }
            if (comparator != null) {
                result.sort(comparator);
            }
        }

        model.addAttribute("books", result);
        return "bookList";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("book", new Book());
        return "AddBook";
    }

    @PostMapping(value = "/add")
    public String addBookForm(@Valid @ModelAttribute("book") Book book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            return "AddBook";
        }
        book.setId(idCounter.getAndIncrement());
        books.add(book);
        return "redirect:/books/";
    }

    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Book addBookJson(@Valid @RequestBody Book book) {
        book.setId(idCounter.getAndIncrement());
        books.add(book);
        return book;
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Book book = books.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Book not found"));
        model.addAttribute("book", book);
        return "EditBook";
    }

    @PostMapping("/edit/{id}")
    public String editBookForm(@PathVariable Long id, @Valid @ModelAttribute("book") Book updatedBook, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", updatedBook);
            return "EditBook";
        }
        Book book = books.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Book not found"));
        book.setTitle(updatedBook.getTitle());
        book.setAuthor_id(updatedBook.getAuthor_id());
        book.setPageCount(updatedBook.getPageCount());
        return "redirect:/books/";
    }

    @PutMapping("/edit/{id}")
    @ResponseBody
    public Book editBook(@PathVariable Long id, @Valid @RequestBody Book updatedBook) {
        Book book = books.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Book not found"));
        book.setTitle(updatedBook.getTitle());
        book.setAuthor_id(updatedBook.getAuthor_id());
        book.setPageCount(updatedBook.getPageCount());
        return book;
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public void deleteBook(@PathVariable Long id) {
        books.removeIf(b -> b.getId().equals(id));
    }

    @PostMapping("/delete/{id}")
    public String deleteBookForm(@PathVariable Long id) {
        books.removeIf(b -> b.getId().equals(id));
        return "redirect:/books/";
    }
}
