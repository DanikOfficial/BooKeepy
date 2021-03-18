package com.app.bookeepy.service;

import org.springframework.stereotype.Service;

import com.app.bookeepy.dto.BookView;
import com.app.bookeepy.entity.Book;

import java.util.List;

@Service
public interface BookService {

    Book storeBook(Book book);

    Book updateBook(Book book);

    void deleteBook(Long id);

    Book findBookById(Long id);

    List<BookView> findAllBooks();

    List<BookView> findBooksByStatus(String status);

}
