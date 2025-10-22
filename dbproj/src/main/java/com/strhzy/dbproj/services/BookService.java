package com.strhzy.dbproj.services;

import com.strhzy.dbproj.models.Book;
import com.strhzy.dbproj.repositories.BookRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> findAll() {
        return repository.findAll();
    }

    public Optional<Book> findById(Long id) {
        return repository.findById(id);
    }

    public Book save(Book book) {
        return repository.save(book);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<Book> findAllFiltered(Long authorId, Long genreId, Long yearId, Long countryId, String sort) {
        List<Book> all = repository.findAll();

        List<Book> filtered = all.stream().filter(b -> {
            if (authorId != null) {
                if (b.getAuthor() == null || b.getAuthor().getId() != authorId) return false;
            }
            if (genreId != null) {
                if (b.getGenre() == null || b.getGenre().getId() != genreId) return false;
            }
            if (yearId != null) {
                if (b.getYear() == null || b.getYear().getId() != yearId) return false;
            }
            if (countryId != null) {
                if (b.getCountry() == null || b.getCountry().getId() != countryId) return false;
            }
            return true;
        }).collect(Collectors.toList());

        Comparator<Book> comparator;
        if ("title".equals(sort)) {
            comparator = Comparator.comparing(Book::getTitle, Comparator.nullsLast(String::compareToIgnoreCase));
        } else if ("pages".equals(sort)) {
            comparator = Comparator.comparing(b -> b.getPages() == null ? 0 : b.getPages());
        } else {
            comparator = Comparator.comparingLong(Book::getId);
        }

        return filtered.stream().sorted(comparator).collect(Collectors.toList());
    }
}
