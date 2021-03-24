package com.app.bookeepy.service.impl;

import com.app.bookeepy.entity.Book;
import com.app.bookeepy.entity.Image;
import com.app.bookeepy.exceptions.ImageException;
import com.app.bookeepy.exceptions.ImageNotFoundException;
import com.app.bookeepy.repository.ImageRepository;
import com.app.bookeepy.service.BookService;
import com.app.bookeepy.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    BookService bookService;

    @Autowired
    ImageRepository imageRepository;

    @PersistenceContext
    EntityManager em;

    @Override
    @Transactional
    public List<Image> addImages(Long bookId, List<Image> images) {
        Book book = bookService.findBookById(bookId);

        // Checks wether the new images has a cover
        Long count = images.stream().filter(image -> image.getIsCover().equals(Boolean.TRUE)).count();

        if (count >= 2L) throw new ImageException("Error: A book cannot have two covers!");

        // If the new images has a cover, then unset the old cover and deletes the Default Location if is there
        if (count.equals(1L)) {
            final String removeSQL = "UPDATE images SET is_cover = FALSE WHERE book_id = :id AND is_cover = TRUE";
            Query query = em.createNativeQuery(removeSQL);
            query.setParameter("id", bookId);
            query.executeUpdate();

            final String removeDefaultCoverSQL = "DELETE FROM images WHERE image_location = :location AND book_id = :id";
            Query deleteDefault = em.createNativeQuery(removeDefaultCoverSQL);
            deleteDefault.setParameter("id", bookId);
            deleteDefault.setParameter("location", "Default Location");
            deleteDefault.executeUpdate();
        }

        images.forEach(image -> image.setBook(book));
        return imageRepository.saveAll(images);
    }

    @Override
    @Transactional
    public Image addImage(Long bookId, Image image) {
        Book book = bookService.findBookById(bookId);

        // If The new image is cover, then remove old cover and default cover if exists
        if (image.getIsCover()) {
            final String removeSQL = "UPDATE images SET is_cover = FALSE WHERE book_id = :id AND is_cover = TRUE";
            Query query = em.createNativeQuery(removeSQL);
            query.setParameter("id", bookId);
            query.executeUpdate();

            final String removeDefaultCoverSQL = "DELETE FROM images WHERE image_location = :location AND book_id = :id";
            Query deleteDefault = em.createNativeQuery(removeDefaultCoverSQL);
            deleteDefault.setParameter("id", bookId);
            deleteDefault.setParameter("location", "Default Location");
            deleteDefault.executeUpdate();
        }

        book.addImage(image);

        int index = book.getImages().indexOf(image);

        return book.getImages().get(index);
    }

    @Override
    public void removeImage(Long id) {
        Image image = findImageById(id);
        imageRepository.delete(image);
    }

    @Override
    public Image findImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException("Error: Could not find an image with this id: " + id));
    }

    @Override
    @Transactional
    public Image setCoverPicture(Long bookId, Long imageId) {

        Book book = bookService.findBookById(bookId);

        final String removeSQL = "UPDATE images SET is_cover = FALSE WHERE book_id = :id AND is_cover = TRUE";
        Query query = em.createNativeQuery(removeSQL);
        query.setParameter("id", bookId);
        query.executeUpdate();

        final String removeDefaultCoverSQL = "DELETE FROM images WHERE image_location = 'Default Location' AND book_id = :id";
        query = em.createNativeQuery(removeDefaultCoverSQL);
        query.setParameter("id", bookId);
        query.executeUpdate();


        Image image = findImageById(imageId);

        image.setIsCover(Boolean.TRUE);

        return imageRepository.save(image);
    }

    @Override
    public void deleteSome(Integer[] ids) {
        imageRepository.deleteSome(ids);
    }

}
