package com.app.bookeepy.controllers;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.app.bookeepy.entity.groups.BookCreation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.bookeepy.api.misc.BookRequestObject;
import com.app.bookeepy.api.misc.GenericErrors;
import com.app.bookeepy.api.misc.ValidationErrors;
import com.app.bookeepy.dto.BookView;
import com.app.bookeepy.entity.Book;
import com.app.bookeepy.entity.Image;
import com.app.bookeepy.service.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Book", description = "REST Endpoint responsible for handling books management.")
public class BookController {

	@Autowired
	BookService bookService;

	@Operation(summary = "Fetches all the books.", method = "GET")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Fetched all Books from the Database.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BookView.class)))) })
	@GetMapping(value = "/books", produces = "application/json")
	public ResponseEntity<List<BookView>> findAllBooks() {
		return new ResponseEntity<>(bookService.findAllBooks(), HttpStatus.OK);
	}

	@Operation(summary = "Fetch books using pagination.", description = "Fetch books using paging number, page size is 30.", method = "GET", parameters = {
			@Parameter(description = "Number of the page.", name = "page", allowEmptyValue = false) })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Fetched the books of the specified page.", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BookView.class))) }) })
	@GetMapping(value = "/books/page/{page}", produces = "application/json")
	public ResponseEntity<Map<String, Object>> findBooks(@PathVariable("page") int page) {
		return new ResponseEntity<Map<String, Object>>(bookService.findBooks(page), HttpStatus.OK);
	}

	@Operation(summary = "Fetches all books based on the specified book status.", method = "GET", parameters = @Parameter(description = "The status to be filtered", name = "status", allowEmptyValue = false))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Fetched all books based on book status.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BookView.class)))),
			@ApiResponse(responseCode = "400", description = "Invalid book status.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = GenericErrors.class)) }) })
	@GetMapping(value = "/books/{status}", produces = "application/json")
	public ResponseEntity<List<BookView>> getBooksByStatus(@PathVariable("status") String status) {
		return new ResponseEntity<>(bookService.findBooksByStatus(status), HttpStatus.OK);
	}

	@Operation(summary = "Fetch using an Id.", description = "Fetch a book using its Id.", method = "GET", parameters = {
			@Parameter(description = "id of the book to be fetched.", name = "id", allowEmptyValue = false) })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Fetched the book of the specified id and it's images.", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(arraySchema = @Schema(implementation = Image.class)), schema = @Schema(implementation = Book.class)) }),
			@ApiResponse(responseCode = "404", description = "Could not find the book.", content = @Content) })
	@GetMapping(value = "/book/{id}", produces = "application/json")
	public ResponseEntity<Book> getBook(@PathVariable("id") Long id) {
		Book book = bookService.findBookById(id);

		return new ResponseEntity<>(book, HttpStatus.OK);
	}

	@Operation(summary = "Adds a new book", description = "Adds a new book to the Database", method = "POST", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookRequestObject.class))))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Successfully added a new book.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class))),
			@ApiResponse(responseCode = "409", description = "Failed to add new book because the specified ISBN already exists.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GenericErrors.class))),
			@ApiResponse(responseCode = "400", description = "Constraints violated.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrors.class))) })
	@PostMapping(value = "/books", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Book> newBook(@RequestBody @Validated(BookCreation.class) Book book) {

		Book newBook = bookService.storeBook(book);

		MultiValueMap<String, String> headers = new HttpHeaders();

		return new ResponseEntity<>(newBook, headers, HttpStatus.CREATED);
	}

	@Operation(summary = "Removes a book.", description = "Removes a book from the Database!", method = "DELETE", parameters = @Parameter(allowEmptyValue = false, name = "id", description = "id of the book that will be removed."))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully removed the book.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class))),
			@ApiResponse(responseCode = "404", description = "Book doesn't exist.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GenericErrors.class))) })
	@DeleteMapping(value = "/book/{id}", produces = "application/json")
	public ResponseEntity<Book> deleteBook(@PathVariable Long id) {

		return new ResponseEntity<>(bookService.deleteBook(id), HttpStatus.OK);
	}

	@Operation(summary = "Updates a book.", description = "Updates a book from the Database!", method = "PUT", parameters = @Parameter(allowEmptyValue = false, name = "id", description = "id of the book that will be updated."), requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookRequestObject.class))))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully updated the book.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class))),
			@ApiResponse(responseCode = "404", description = "The specified Book doesn't exist.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GenericErrors.class))),
			@ApiResponse(responseCode = "409", description = "Failed to add new book because the specified ISBN already exists.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GenericErrors.class))),
			@ApiResponse(responseCode = "400", description = "Constraints violated.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrors.class))) })
	@PutMapping(value = "/book/{id}", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Book> updateBook(@RequestBody @Valid Book book, @PathVariable("id") Long id) {
		book.setId(id);
		Book updatedBook = bookService.updateBook(book);

		return new ResponseEntity<>(updatedBook, HttpStatus.OK);
	}

}
