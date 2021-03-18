package com.app.bookeepy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.bookeepy.entity.Image;

@Service
public interface ImageService {

	 List<Image> addImages(Long bookId, List<Image> images);

	 Image addImage(Long bookId, Image image);

	 void removeImage(Long id);
	 
	 void deleteSome(Integer[] ids);

	 Image findImageById(Long id);

	 Image setCoverPicture(Long oldImageId, Long newImageId);

}
