package com.strhzy.dbproj.services;

import com.strhzy.dbproj.models.Genre;
import com.strhzy.dbproj.repositories.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {
    private final GenreRepository repository;

    public GenreService(GenreRepository repository) {
        this.repository = repository;
    }

    public List<Genre> findAll() {
        return repository.findAll();
    }

    public Optional<Genre> findById(Long id) {
        return repository.findById(id);
    }

    public Genre save(Genre genre) {
        return repository.save(genre);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}

