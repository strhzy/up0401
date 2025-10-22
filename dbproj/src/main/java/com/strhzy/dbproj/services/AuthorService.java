package com.strhzy.dbproj.services;

import com.strhzy.dbproj.models.Author;
import com.strhzy.dbproj.repositories.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {
    private final AuthorRepository repository;

    public AuthorService(AuthorRepository repository) {
        this.repository = repository;
    }

    public List<Author> findAll() {
        return repository.findAll();
    }

    public Optional<Author> findById(Long id) {
        return repository.findById(id);
    }

    public Author save(Author author) {
        return repository.save(author);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}

