package com.app.bookeepy.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.bookeepy.entity.Book;
import com.app.bookeepy.entity.Image;
import com.app.bookeepy.exceptions.ImageNotFoundException;
import com.app.bookeepy.repository.ImageRepository;
import com.app.bookeepy.service.BookService;
import com.app.bookeepy.service.ImageService;

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
		book.addImages(images);
		/*images.forEach(image -> image.setBook(book));
		return imageRepository.saveAll(images);*/
		return book.getImages();
	}

	@Override
	@Transactional
	public Image addImage(Long bookId, Image image) {
		Book book = bookService.findBookById(bookId);
		book.addImage(image);
		
		int index = book.getImages().indexOf(image);

		return book.getImages().get(index);
	}

	@Override
	public void removeImage(Long id) {
		Image image = findImageById(id);
		imageRepository.deleteById(id);
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

		Image image = findImageById(imageId);

		image.setIsCover(Boolean.TRUE);

		return imageRepository.save(image);
	}

	@Override
	public void deleteSome(Integer[] ids) {
		imageRepository.deleteSome(ids);
	}

}
