package com.app.bookeepy.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.bookeepy.dto.BookView;
import com.app.bookeepy.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, PagingAndSortingRepository<Book, Long> {

	@Query(value = "SELECT b.id as id, b.title as title, b.author as author, b.status as status, i.image_url as image_url FROM books b INNER JOIN "
			+ "images i ON b.id = i.book_id WHERE i.is_cover = TRUE order by id desc", nativeQuery = true)
	 List<BookView> findAllBooks();

	@Query(value = "SELECT b.id as id, b.title as title, b.author as author, b.status as status, i.image_url as image_url FROM books b INNER JOIN "
			+ "images i ON b.id = i.book_id WHERE i.is_cover = TRUE order by id desc", nativeQuery = true)
	Page<BookView> findBooks(Pageable pageable);

	@Query(value = "SELECT b.id as id, b.title as title, b.author as author, b.status as status, i.image_url as image_url FROM books b INNER JOIN "
			+ "images i ON b.id = i.book_id WHERE i.is_cover = TRUE AND b.id = :id order by id desc", nativeQuery = true)
	BookView getBookAfterUpdate(@Param("id") Long id);
}
