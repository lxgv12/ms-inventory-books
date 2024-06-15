package com.unir.test_app.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookRequest {
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
