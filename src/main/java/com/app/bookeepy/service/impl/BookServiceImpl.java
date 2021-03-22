package com.app.bookeepy.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.app.bookeepy.dto.BookView;
import com.app.bookeepy.entity.Book;
import com.app.bookeepy.entity.Image;
import com.app.bookeepy.exceptions.BookAlreadyExistsException;
import com.app.bookeepy.exceptions.BookException;
import com.app.bookeepy.exceptions.BookNotFoundException;
import com.app.bookeepy.exceptions.InvalidBookStatusException;
import com.app.bookeepy.repository.BookRepository;
import com.app.bookeepy.repository.ImageRepository;
import com.app.bookeepy.service.BookService;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private EntityManager em;

	@Override
	public Book storeBook(Book book) {
		// Validates ISBN
		isbnExists(book.getIsbn());
		
		validateCover(book);

		return bookRepository.save(book);
	}

	@Override
	public Book deleteBook(Long id) {
		Book book = findBookById(id);

		imageRepository.deleteImages(id);

		bookRepository.delete(book);
		
		return book;
	}

	@Override
	public Book findBookById(Long id) {

		// SELECT b FROM books b LEFT JOIN FETCH b.images WHERE b.id = :id
		final String sqlQuery = "SELECT b FROM books b LEFT JOIN FETCH b.images WHERE b.id = :id";

		try {
			TypedQuery<Book> query = em.createQuery(sqlQuery, Book.class);
			query.setParameter("id", id);
			return query.getSingleResult();
		} catch (NoResultException e) {
			throw new BookNotFoundException("Error: Could not find a book with the id of " + id);
		}

	}

	/*
	 * @Override public List<Book> findAllBooks() {
	 * 
	 * /* return em.createQuery( "select new " +
	 * "   com.app.BookDatabase.entity.Book(" +
	 * "       b.id, b.title, b.author, b.edition, b.status, b.isbn" + "   ) " +
	 * "from books b", Book.class) .getResultList(); }
	 */

	@Override
	public List<BookView> findBooksByStatus(String status) {

		if (status.equalsIgnoreCase("wished") || status.equalsIgnoreCase("owned")) {
			return findAllBooks().parallelStream().filter(book -> book.getStatus().equalsIgnoreCase(status))
					.collect(Collectors.toList());
		} else {
			throw new InvalidBookStatusException();
		}

	}

	@Override
	public List<BookView> findAllBooks() {
		return bookRepository.findAllBooks();
	}

	@Override
	public Book updateBook(Book book) {

		// Validate the ISBN
		isbnExists(book.getId(), book.getIsbn());

		return bookRepository.save(book);
	}

	private void isbnExists(String isbn) {
		final String isbnQuery = "SELECT b FROM books b WHERE b.isbn = :isbn";

		try {
			TypedQuery<Book> typedQuery = em.createQuery(isbnQuery, Book.class);
			typedQuery.setParameter("isbn", isbn);
			Book book = typedQuery.getSingleResult();

			if (book != null)
				throw new BookAlreadyExistsException("Error: There's already book with this ISBN: " + isbn);

		} catch (NoResultException ex) {

		}

	}

	private void isbnExists(Long id, String isbn) {

		final String isbnQuery = "SELECT b FROM books b WHERE b.isbn = :isbn and b.id <> :id";

		try {
			TypedQuery<Book> typedQuery = em.createQuery(isbnQuery, Book.class);
			typedQuery.setParameter("isbn", isbn);
			typedQuery.setParameter("id", id);
			Book book = typedQuery.getSingleResult();

			if (book != null)
				throw new BookAlreadyExistsException("Error: There's already book with this ISBN: " + isbn);

		} catch (NoResultException ex) {

		}

	}

	@Override
	public Map<String, Object> findBooks(int page) {

		Map<String, Object> data = new LinkedHashMap<String, Object>();
		Page<BookView> results = bookRepository.findBooks(PageRequest.of(page, 30));
		
		
		data.put("page", results.getNumber());;
		data.put("totalRecords", results.getTotalElements());
		data.put("numPages", results.getTotalPages());
		data.put("books", results.getContent());
		
		
		return data;
	}
	
	 /**
	  * Validates if the book has a cover
	  * Validates if the book has more than one cover
	  * @param book
	  */
	private void validateCover(Book book) {
		
		// Counts the number of covers a book has
		Long count = book.getImages().stream().filter(image -> image.getIsCover().equals(Boolean.TRUE)).count();
		
		// If the book as 2 covers or more throws this exception
		if (count >= 2L) throw new BookException("Error: A book cannot have two covers!");
		
		// If the book has no cover, than assign a default cover
		if (count.equals(0L)) book.getImages().add(new Image("Default Cover", "Default Location", Boolean.TRUE));
		
		book.getImages().forEach(image -> image.setBook(book));
	}
		
}
