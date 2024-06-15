package com.unir.test_app.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.unir.test_app.model.pojo.Book;
import com.unir.test_app.model.pojo.BookDto;
import com.unir.test_app.model.request.CreateBookRequest;
import com.unir.test_app.service.BooksService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Books Controller", description = "Microservicio encargado de exponer operaciones CRUD sobre libros alojados en una base de datos.")
public class BooksController {
    
    private final BooksService service;

    @GetMapping("/books")
	@Operation(
		operationId = "Obtener libros",
		description = "Operacion de lectura",
		summary = "Se devuelve una lista de todos los libros almacenados en la base de datos.")
	@ApiResponse(
        responseCode = "200",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
	public ResponseEntity<List<Book>> getBooks(
		@RequestHeader Map<String, String> headers,
		@Parameter(name = "name", description = "Nombre del libro. No tiene por que ser exacta", example = "El Alquimista", required = false)
        @RequestParam(required = false) String name,
		@Parameter(name = "author", description = "Nombre del autor. No tiene por que ser exacta", example = "Paulo Coelho", required = false)
        @RequestParam(required = false) String author,
		@Parameter(name = "genre", description = "Nombre del genero del libro. No tiene por que ser exacta", example = "Drama", required = false)
        @RequestParam(required = false) String genre,
        @Parameter(name = "language", description = "Nombre del idioma del libro. No tiene por que ser exacta", example = "Español", required = false)
        @RequestParam(required = false) String language,
        @Parameter(name = "iSBN", description = "ISBN del libro. No tiene por que ser exacta", example = "9786287667174", required = false)
        @RequestParam(required = false) String iSBN,
        @Parameter(name = "format", description = "Nombre del formato del libro. No tiene por que ser exacta", example = "Tapa blanda", required = false)
        @RequestParam(required = false) String format
		) {

		log.info("headers: {}", headers);
		List<Book> books = service.getBooks(name, author, genre, language, format, iSBN);

		if (books != null) {
			return ResponseEntity.ok(books);
		} else {
			return ResponseEntity.ok(Collections.emptyList());
		}
	}

    @GetMapping("/books/{bookId}")
	@Operation(
        operationId = "Obtener un libro",
        description = "Operacion de lectura",
        summary = "Se devuelve un libro a partir de su identificador.")
    @ApiResponse(
        responseCode = "200",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
	@ApiResponse(
        responseCode = "404",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
        description = "No se ha encontrado el libro con el identificador indicado.")
	public ResponseEntity<Book> getBook(@PathVariable String bookId) {

		log.info("Request received for book {}", bookId);
		Book book = service.getBook(bookId);

		if (book != null) {
			return ResponseEntity.ok(book);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

    @DeleteMapping("/books/{bookId}")
	@Operation(
            operationId = "Eliminar un libro",
            description = "Operacion de escritura",
            summary = "Se elimina un libro a partir de su identificador.")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "No se ha encontrado el libro con el identificador indicado.")
	public ResponseEntity<Void> deleteBook(@PathVariable String bookId) {

		Boolean removed = service.removeBook(bookId);

		if (Boolean.TRUE.equals(removed)) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

    @PostMapping("/books")
	@Operation(
            operationId = "Insertar un libro",
            description = "Operacion de escritura",
            summary = "Se crea un libro a partir de sus datos.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del libro a crear.",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateBookRequest.class))))
    @ApiResponse(
            responseCode = "201",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @ApiResponse(
            responseCode = "400",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "Datos incorrectos introducidos.")
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "No se ha encontrado el libro con el identificador indicado.")
	public ResponseEntity<Book> getBook(@RequestBody CreateBookRequest request) {

		Book createdBook = service.createBook(request);

		if (createdBook != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}

	@PatchMapping("/books/{bookId}")
    @Operation(
        operationId = "Modificar parcialmente un libro",
        description = "RFC 7386. Operacion de escritura",
        summary = "RFC 7386. Se modifica parcialmente un libro.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos del libro a crear.",
                required = true,
                content = @Content(mediaType = "application/merge-patch+json", schema = @Schema(implementation = String.class))))
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @ApiResponse(
            responseCode = "400",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "Libro inválido o datos incorrectos introducidos.")
	public ResponseEntity<Book> patchBook(@PathVariable String bookId, @RequestBody String patchBody) {

        Book patched = service.updateBook(bookId, patchBody);
        if (patched != null) {
            return ResponseEntity.ok(patched);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/books/{bookId}")
    @Operation(
            operationId = "Modificar totalmente un libro",
            description = "Operacion de escritura",
            summary = "Se modifica totalmente un libro.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del libro a actualizar.",
                    required = true,
                    content = @Content(mediaType = "application/merge-patch+json", schema = @Schema(implementation = BookDto.class))))
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "Libro no encontrado.")
    public ResponseEntity<Book> updateBook(@PathVariable String bookId, @RequestBody BookDto body) {

        Book updated = service.updateBook(bookId, body);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
