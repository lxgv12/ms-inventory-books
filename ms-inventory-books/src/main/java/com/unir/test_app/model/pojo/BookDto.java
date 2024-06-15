package com.unir.test_app.model.pojo;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BookDto {
    private String name;
    private String price;
    private String author;
    private String publisher;
    private String published;
    private String genre;
    private String language;
    private String iSBN;
    private String format;
}
