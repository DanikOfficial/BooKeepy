package com.app.bookeepy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.bookeepy.dto.BookView;
import com.app.bookeepy.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "select b.id as id, b.title as title, b.author as author, b.status as status," +
            "(select i.image_url from images i where i.book_id = b.id and i.is_cover = TRUE) " +
            "as image_url from books b", nativeQuery = true)
    public List<BookView> findAllBooks();
    

}
