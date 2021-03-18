package com.app.bookeepy.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.app.bookeepy.BooKeepy;
import com.app.bookeepy.entity.Book;
import com.app.bookeepy.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = BooKeepy.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class BookRestControllerIntegrationTest {

	@Autowired
	private MockMvc mvc;			

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	BookRepository bookRepository;

	@AfterEach
	public void deleteAll() {
		bookRepository.deleteAll();
	}

	@Test
	public void givenBooks_whenGetBooks_thenStatus200() throws Exception {

		createBook("Title 1", "Author 1", "Edition 1", "Owned", "111-111-111-111-1");
		createBook("Title 2", "Author 2", "Edition 2", "Wished", "111-111-111-111-2");
		createBook("Title 3", "Author 3", "Edition 3", "Owned", "111-111-111-111-3");
		createBook("Title 4", "Author 4", "Edition 4", "Wished", "111-111-111-111-4");

		mvc.perform(get("/api/v1/books"))
			.andExpect(status().isOk())
			.andExpect(content()
			.contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

	}

	@Test
	public void whenGivenValidBook_thenStatus200() throws Exception {
		Book book = new Book("Title 5", "Author 1", "Edition 1", "Owned", "111-101-111-111-5");

		mvc.perform(
				post("/api/v1//books").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(book)))
				.andExpect(status().isCreated())
				.andExpect(content()
				.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("title", is(book.getTitle())));

	}

	@Test
	public void givenBook_whenAnyInvalidField_thenStatus400() throws Exception {
		Book book = new Book("Title 5", "", "Edition 1", "Owned", "111-111-111-111-5");

		mvc.perform(post("/api/v1/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(book)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void givenBook_whenDuplicated_thenStatus409() throws Exception {
		Book book = new Book("Title 5", "Author 1", "Edition 1", "Owned", "111-111-111-111-9");
		Book duplicatedBook = new Book("Title 5", "Author 1", "Edition 1", "Owned", "111-111-111-111-9");

		mvc.perform(post("/api/v1/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(book)))
				.andExpect(status().isCreated());

		mvc.perform(post("/api/v1//books").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(duplicatedBook))).andExpect(status().isConflict());
	}

	@Test
	public void givenBookId_WhenDoesntExist_thenStatus404() throws Exception {

		createBook("Title 1 exists", "Author 1", "Edition 1", "Owned", "111-111-191-111-1");

		mvc.perform(get("/book/{id}", 20L))
			.andExpect(status().isNotFound())
			.andExpect(content()
			.contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

	}

	@Test
	public void givenBookId_whenExist_thenStatus200() throws Exception {

		Book book = createBook("Title 1 exists", "Author 1", "Edition 1", "Owned", "111-111-191-111-1");

		mvc.perform(get("/api/v1/book/{id}", book.getId())).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.isbn", is(book.getIsbn())));

	}

	@Test
	public void givenBook_whenUpdateSucceeds_thenStatus200() throws Exception {
		Book book = createBook("Title 1 exists", "Author 1", "Edition 1", "Owned", "111-111-191-111-1");

		Book updatedBook = new Book("Title 2", "Author 2", "Edition 2", "Owned", "111-211-191-411-1");

		mvc.perform(put("/api/v1/book/{id}", book.getId()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedBook))).andExpect(status().isOk())
				.andExpect(jsonPath("title", is(updatedBook.getTitle())));
	}
	
	@Test
	public void givenBook_whenUpdateFails_thenStatus409() throws Exception {
		createBook("Title 1", "Author 1", "Edition 1", "Owned", "111-111-191-111-1");
		Book book2 = createBook("Title 2", "Author 2", "Edition 2", "Owned", "211-131-191-111-1");
		Book updated = new Book("Title 1 exists", "Author 1", "Edition 1", "Owned", "111-111-191-111-1");

		
		mvc.perform(put("/api/v1/book/{id}", book2.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updated)))
				.andExpect(status().isConflict())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
		
	}

	@Test
	public void givenBookId_whenDeleteSucceeds_thenStatus200() throws Exception {
		Book book = createBook("Title 1 exists", "Author 1", "Edition 1", "Owned", "111-111-191-111-1");

		mvc.perform(delete("/api/v1/book/{id}", book.getId())).andExpect(status().isOk());
	}
	
	@Test
	public void givenBookId_whenDeleteFails_thenStatus404() throws Exception {

		mvc.perform(delete("/book/{id}", 50L)).andExpect(status().isNotFound());
	}

	private Book createBook(String title, String author, String edition, String status, String isbn) {
		Book book = new Book(title, author, edition, status, isbn);
		return bookRepository.save(book);
	}

}
