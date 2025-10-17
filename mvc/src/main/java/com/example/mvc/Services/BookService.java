package com.example.mvc.Services;

import com.example.mvc.Models.*;
import com.example.mvc.Repositories.*;

public class BookService {
    private final BookRepo bookRepo = new BookRepo();
    public Book create(Book book){
        return bookRepo.save(book);
    }

    public Book update(Long id, Book bookupd){
        Book book = bookRepo.findById(id).orElseThrow();
        book.setTitle(bookupd.getTitle());
        book.setAuthor_id(bookupd.getAuthor_id());
        return bookRepo.save(book);
    }

    public void hardDelete(Long id){
        bookRepo.deleteById(id);
    }
}
