package com.app.bookeepy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.bookeepy.dto.BookView;
import com.app.bookeepy.entity.Book;
import com.app.bookeepy.exceptions.BookAlreadyExistsException;
import com.app.bookeepy.exceptions.BookNotFoundException;
import com.app.bookeepy.exceptions.InvalidBookStatusException;
import com.app.bookeepy.repository.BookRepository;
import com.app.bookeepy.repository.ImageRepository;
import com.app.bookeepy.service.BookService;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

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

		return bookRepository.save(book);
	}

	@Override
	public void deleteBook(Long id) {
		Book book = findBookById(id);

		imageRepository.deleteImages(id);

		bookRepository.delete(book);
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

}
