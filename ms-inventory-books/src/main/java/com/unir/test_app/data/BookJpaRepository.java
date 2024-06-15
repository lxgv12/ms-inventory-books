package com.unir.test_app.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.unir.test_app.model.pojo.Book;

public interface BookJpaRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    List<Book> findByName(String name);

	List<Book> findByAuthor(String author);

	List<Book> findByGenre(String genre);
}
