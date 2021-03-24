package com.app.bookeepy.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.app.bookeepy.dto.BookView;
import com.app.bookeepy.entity.Book;

@Service
public interface BookService {

    Book storeBook(Book book);

    BookView updateBook(Book book);

    Book deleteBook(Long id);

    Book findBookById(Long id);

    List<BookView> findAllBooks();
    
    Map<String, Object> findBooks(int page);

    List<BookView> findBooksByStatus(String status);

}
