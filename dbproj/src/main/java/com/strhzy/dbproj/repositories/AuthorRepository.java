package com.strhzy.dbproj.repositories;

import com.strhzy.dbproj.models.Author;
import com.strhzy.dbproj.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository  extends JpaRepository<Author, Long> {
}
