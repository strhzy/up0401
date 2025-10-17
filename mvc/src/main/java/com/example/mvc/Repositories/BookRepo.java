package com.example.mvc.Repositories;

import java.util.*;
import com.example.mvc.Models.*;

public class BookRepo {
    private final Map<Long, Book> bookstorage = new HashMap<>();
    private long idCounter = 1;

    public Book save(Book book){
        if (book.getId() == null){
            book.setId(idCounter++);
        }
        bookstorage.put(book.getId(), book);
        return book;
    }
    public Optional<Book> findById(Long id){
        return Optional.ofNullable(bookstorage.get(id));
    }
    public List<Book> findAll(){
        return new ArrayList<>(bookstorage.values());
    }
    public void deleteById(Long id){
        bookstorage.remove(id);
    }
    public void deleteManyById(List<Long> ids){
        ids.forEach(bookstorage::remove);
    }
}
