package com.example.mvc.Services;

import com.example.mvc.Models.*;
import com.example.mvc.Repositories.*;

public class AuthorService {
    private final AuthorRepo authorRepo = new AuthorRepo();
    public Author create(Author author){
        return authorRepo.save(author);
    }

    public Author update(Long id, Author authorupd){
        Author author = authorRepo.findById(id).orElseThrow();
        author.setName(authorupd.getName());
        author.setSurname(authorupd.getSurname());
        author.setLastname(authorupd.getLastname());
        return authorRepo.save(author);
    }

    public void hardDelete(Long id){
        authorRepo.deleteById(id);
    }
}
