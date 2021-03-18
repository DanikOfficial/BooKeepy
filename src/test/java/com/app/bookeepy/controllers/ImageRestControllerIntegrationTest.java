package com.app.bookeepy.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import com.app.bookeepy.entity.Image;
import com.app.bookeepy.repository.BookRepository;
import com.app.bookeepy.repository.ImageRepository;
import com.app.bookeepy.service.ImageService;
import com.fasterxml.jackson.databind.ObjectMapper;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = BooKeepy.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class ImageRestControllerIntegrationTest {
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired 
	private ObjectMapper objectMapper;
	
	@Autowired
	private ImageRepository imageRepository;
	
	@Autowired
	private ImageService imageService;

	@Autowired
	private BookRepository bookRepository;
	
	@AfterEach
	public void deleteAll() {
		imageRepository.deleteAll();
		bookRepository.deleteAll();
	}
	
	@Test
	public void givenImage_whenPersistSucceeds_thenStatus200() throws Exception {
		Book book = createBook("Title 1 exists", "Author 1", "Edition 1", "Owned", "111-111-191-111-1"); 
		Image image = new Image("url1", "location1", Boolean.TRUE);
				
		mvc.perform(post("/api/v1/image/{id}", book.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(image)))
				.andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("image_url", is("url1")))
				.andExpect(jsonPath("image_location", is("location1")))
				.andExpect(jsonPath("isCover", is(image.getIsCover())));
		
	}
	
	
	@Test
	public void givenImage_whenPersistFails_thenStatus404() throws Exception {
		Image image = new Image("url1", "location2", Boolean.TRUE);
		
		mvc.perform(post("/api/v1/image/{id}", 5L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(image)))
				.andExpect(status().isNotFound())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void givenImages_whenParentExists_thenStatus200() throws Exception {
		Book book = createBook("Title 1 exists", "Author 1", "Edition 1", "Owned", "111-111-191-111-1");
		
		Image image1 = new Image("url1", "location1", Boolean.TRUE);
		Image image2 = new Image("url2", "location2", Boolean.FALSE);
		Image image3 = new Image("url3", "location3", Boolean.FALSE);

		Image[] images = {image1, image2, image3};
		
		mvc.perform(post("/api/v1/images/{id}", book.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(images)))
				.andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].image_url", is("url1")))
				.andExpect(jsonPath("$[1].image_url", is("url2")))
				.andExpect(jsonPath("$[2].image_url", is("url3")));
	}
	
	@Test
	public void givenImages_whenNonExistentParent_thenStatus404() throws Exception {
		Image image1 = new Image("url1", "location1", Boolean.TRUE);
		Image image2 = new Image("url2", "location2", Boolean.FALSE);
		Image image3 = new Image("url3", "location3", Boolean.FALSE);

		Image[] images = {image1, image2, image3};
		
		
		mvc.perform(post("/api/v1/images/{id}", 10L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(images)))
				.andExpect(status().isNotFound())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void givenImage_whenSetCoverSucceeds_thenStatus200() throws Exception {
		Book book = createBook("Title 1 exists", "Author 1", "Edition 1", "Owned", "111-111-191-111-1");
		
		createImage("url1", "location1", Boolean.TRUE, book.getId());
		createImage("url2", "location2", Boolean.FALSE, book.getId());
		createImage("url3", "location3", Boolean.FALSE, book.getId());
			
		mvc.perform(put("/api/v1/image/{bookId}/cover/{imageId}", 1L, 3L))
		.andExpect(status().isOk())
		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("image_url", is("url2")));
		
	}
	
	@Test
	public void givenInvalidImageId_thenStatus404() throws Exception {
		Book book = createBook("Title 1 exists", "Author 1", "Edition 1", "Owned", "111-111-191-111-1");
		
		createImage("url1", "location1", Boolean.TRUE, book.getId());
		createImage("url2", "location2", Boolean.FALSE, book.getId());
		createImage("url3", "location3", Boolean.FALSE, book.getId());
			
		mvc.perform(put("/api/v1/image/{bookId}/cover/{imageId}", 1L, 9L))
		.andExpect(status().isNotFound())
		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void givenValidImageId_whenDeleteSuceeds_thenStatus200() throws Exception {
		Book book = createBook("Title 1 exists", "Author 1", "Edition 1", "Owned", "111-111-191-111-1");
		
		Image image = createImage("url1", "location1", Boolean.TRUE, book.getId());
		createImage("url2", "location2", Boolean.FALSE, book.getId());
		createImage("url3", "location3", Boolean.FALSE, book.getId());
		
		mvc.perform(delete("/api/v1/image/{id}",image.getId())).andExpect(status().isOk());
	}
	
	@Test
	public void whenGivenInvalidIdToDelete_thenStatus404() throws Exception {
		Book book = createBook("Title 1 exists", "Author 1", "Edition 1", "Owned", "111-111-191-111-1");
		
		 createImage("url1", "location1", Boolean.TRUE, book.getId());
		
		mvc.perform(delete("/api/v1/image/{id}",5L)).andExpect(status().isNotFound());
	}
	
	@Test
	private Book createBook(String title, String author, String edition, String status, String isbn) {
		Book book = new Book(title, author, edition, status, isbn);
		return bookRepository.save(book);
	}

	private Image createImage(String image_url, String image_location, Boolean isCover, Long bookId) {
		return imageService.addImage(bookId, new Image(image_url, image_location, isCover));
		
	}

}
