package com.example.mvc.Repositories;

import java.util.*;
import com.example.mvc.Models.*;

public class AuthorRepo {
    private final Map<Long, Author> authorStorage = new HashMap<>();
    private long idCounter = 1;

    public Author save(Author author){
        if (author.getId() == null){
            author.setId(idCounter++);
        }
        authorStorage.put(author.getId(), author);
        return author;
    }
    public Optional<Author> findById(Long id){
        return Optional.ofNullable(authorStorage.get(id));
    }
    public List<Author> findAll(){
        return new ArrayList<>(authorStorage.values());
    }
    public void deleteById(Long id){
        authorStorage.remove(id);
    }
    public void deleteManyById(List<Long> ids){
        ids.forEach(authorStorage::remove);
    }
}
