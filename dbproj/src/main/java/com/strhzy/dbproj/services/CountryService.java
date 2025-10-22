package com.strhzy.dbproj.services;

import com.strhzy.dbproj.models.Country;
import com.strhzy.dbproj.repositories.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryService {
    private final CountryRepository repository;

    public CountryService(CountryRepository repository) {
        this.repository = repository;
    }

    public List<Country> findAll() {
        return repository.findAll();
    }

    public Optional<Country> findById(Long id) {
        return repository.findById(id);
    }

    public Country save(Country country) {
        return repository.save(country);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}

