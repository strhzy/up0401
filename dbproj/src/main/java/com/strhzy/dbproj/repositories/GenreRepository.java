package com.strhzy.dbproj.repositories;

import com.strhzy.dbproj.models.Book;
import com.strhzy.dbproj.models.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository  extends JpaRepository<Genre, Long> {

}
