package com.strhzy.dbproj.repositories;

import com.strhzy.dbproj.models.Book;
import com.strhzy.dbproj.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
}
