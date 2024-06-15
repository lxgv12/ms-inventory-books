package com.unir.test_app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.unir.test_app.data.BookRepository;
import com.unir.test_app.model.pojo.Book;
import com.unir.test_app.model.pojo.BookDto;
import com.unir.test_app.model.request.CreateBookRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BooksServiceImpl implements BooksService {

    @Autowired
    private BookRepository repository;

    @Autowired
	private ObjectMapper objectMapper;

    @Override
    public List<Book> getBooks(String name, String author, String genre, String language, String format, String iSBN) {
        if (StringUtils.hasLength(name) || StringUtils.hasLength(author) || StringUtils.hasLength(genre) || StringUtils.hasLength(language) || StringUtils.hasLength(format) || StringUtils.hasLength(iSBN)) {
			return repository.search(name, author, genre, language, format, iSBN);
		}

        List<Book> books = repository.getProducts();
        return books.isEmpty() ? null : books;
    }

    @Override
    public Book getBook(String bookId) {
        return repository.getById(Long.valueOf(bookId));
    }

    @Override
    public Boolean removeBook(String bookId) {
        Book book = repository.getById(Long.valueOf(bookId));

        if (book != null)
        {
            repository.delete(book);
            return Boolean.TRUE;
        }
        else{
            return Boolean.FALSE;
        }
    }

    @Override
    public Book createBook(CreateBookRequest request) {
        if (request != null && StringUtils.hasLength(request.getName().trim())
        && StringUtils.hasLength(request.getPrice().trim()) && StringUtils.hasLength(request.getAuthor().trim()) 
        && request.getPublisher() != null && StringUtils.hasLength(request.getPublished().trim()) 
        && request.getGenre() != null && StringUtils.hasLength(request.getLanguage().trim()) 
        && request.getISBN() != null && StringUtils.hasLength(request.getFormat().trim())
        ) {
            Book book = Book.builder().name(request.getName()).price(request.getPrice())
            .author(request.getAuthor()).publisher(request.getPublisher()).published(request.getPublished())
            .genre(request.getGenre()).language(request.getLanguage()).iSBN(request.getISBN()).format(request.getFormat())
            .build();

            return repository.save(book);
        }
        else
        {
            return null;
        }
    }

    @Override
    public Book updateBook(String bookId, String request) {
        Book book = repository.getById(Long.valueOf(bookId));
		if (book != null) {
			try {
				JsonMergePatch jsonMergePatch = JsonMergePatch.fromJson(objectMapper.readTree(request));
				JsonNode target = jsonMergePatch.apply(objectMapper.readTree(objectMapper.writeValueAsString(book)));
				Book patched = objectMapper.treeToValue(target, Book.class);
				repository.save(patched);
				return patched;
			} catch (JsonProcessingException | JsonPatchException e) {
				log.error("Error updating book {}", bookId, e);
                return null;
            }
        } else {
			return null;
		}
    }

    @Override
    public Book updateBook(String bookId, BookDto updateRequest) {
        Book book = repository.getById(Long.valueOf(bookId));
		if (book != null) {
			book.update(updateRequest);
			repository.save(book);
			return book;
		} else {
			return null;
		}
    }
}
