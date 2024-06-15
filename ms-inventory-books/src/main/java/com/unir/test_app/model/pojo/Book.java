package com.unir.test_app.model.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "books")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "price")
    private String price;

    @Column(name = "author")
    private String author;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "published")
    private String published;

    @Column(name = "genre")
    private String genre;

    @Column(name = "language")
    private String language;

    @Column(name = "iSBN")
    private String iSBN;

    @Column(name = "format")
    private String format;

    public void update(BookDto bookDtoDto) {
		this.name = bookDtoDto.getName();
		this.price = bookDtoDto.getPrice();
		this.publisher = bookDtoDto.getPublisher();
		this.published = bookDtoDto.getPublished();
        this.genre = bookDtoDto.getGenre();
		this.language = bookDtoDto.getLanguage();
		this.iSBN = bookDtoDto.getISBN();
        this.format = bookDtoDto.getFormat();
	}
}
