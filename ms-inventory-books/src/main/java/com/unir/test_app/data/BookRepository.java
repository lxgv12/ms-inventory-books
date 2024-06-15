package com.unir.test_app.data;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.unir.test_app.data.utils.SearchCriteria;
import com.unir.test_app.data.utils.SearchOperation;
import com.unir.test_app.data.utils.SearchStatement;
import com.unir.test_app.model.pojo.Book;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BookRepository {

    private final BookJpaRepository repository;

    public List<Book> getProducts() {
        return repository.findAll();
    }

    public Book getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Book save(Book book) {
        return repository.save(book);
    }

    public void delete(Book book) {
        repository.delete(book);
    }

    public List<Book> search(String name, String author, String genre, String language, String format, String iSBN) {
        SearchCriteria<Book> spec = new SearchCriteria<>();
        if (StringUtils.isNotBlank(name)) {
            spec.add(new SearchStatement("name", name, SearchOperation.MATCH));
        }

        if (StringUtils.isNotBlank(author)) {
            spec.add(new SearchStatement("author", author, SearchOperation.EQUAL));
        }

        if (StringUtils.isNotBlank(genre)) {
            spec.add(new SearchStatement("genre", genre, SearchOperation.MATCH));
        }

        if (StringUtils.isNotBlank(language)) {
            spec.add(new SearchStatement("language", language, SearchOperation.EQUAL));
        }

        if (StringUtils.isNotBlank(format)) {
            spec.add(new SearchStatement("format", format, SearchOperation.EQUAL));
        }

        if (StringUtils.isNotBlank(iSBN)) {
            spec.add(new SearchStatement("iSBN", iSBN, SearchOperation.EQUAL));
        }

        return repository.findAll(spec);
    }
}
