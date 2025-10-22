package com.strhzy.dbproj.services;

import com.strhzy.dbproj.models.Year;
import com.strhzy.dbproj.repositories.YearRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class YearService {
    private final YearRepository repository;

    public YearService(YearRepository repository) {
        this.repository = repository;
    }

    public List<Year> findAll() {
        return repository.findAll();
    }

    public Optional<Year> findById(Long id) {
        return repository.findById(id);
    }

    public Year save(Year year) {
        return repository.save(year);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}

