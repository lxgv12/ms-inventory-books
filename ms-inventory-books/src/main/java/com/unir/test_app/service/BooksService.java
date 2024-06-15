package com.unir.test_app.service;

import java.util.List;

import com.unir.test_app.model.pojo.Book;
import com.unir.test_app.model.pojo.BookDto;
import com.unir.test_app.model.request.CreateBookRequest;

public interface BooksService {
    List<Book> getBooks(String name, String author, String genre, String language, String format, String iSBN);
	
	Book getBook(String bookId);
	
	Boolean removeBook(String bookId);
	
	Book createBook(CreateBookRequest request);

	Book updateBook(String productId, String updateRequest);

	Book updateBook(String productId, BookDto updateRequest);
}
