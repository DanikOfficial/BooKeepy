package com.app.bookeepy.controllers;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.bookeepy.api.misc.GenericErrors;
import com.app.bookeepy.api.misc.ImageRequestObject;
import com.app.bookeepy.api.misc.ValidationErrors;
import com.app.bookeepy.entity.Image;
import com.app.bookeepy.service.ImageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Image", description = "REST Endpoint responsible for handling images management.")
public class ImageController {

	@Autowired
	ImageService imageService;

	@Operation(summary = "Adds an image.", description = "Adds an image associated with a book!", method = "POST",
			parameters = @Parameter(allowEmptyValue = false, name = "id", description = "id of the book."),
			requestBody  = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json"
			, schema = @Schema(implementation = ImageRequestObject.class))))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Successfully added the image.",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = Image.class))),
			@ApiResponse(responseCode = "404", description = "The specified Book doesn't exist.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = GenericErrors.class))),
			@ApiResponse(responseCode = "400", description = "Constraints violated.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrors.class)))
	})
	@PostMapping(value = "/image/{id}", produces = "application/json", consumes = "application/json")
	public ResponseEntity<Image> addImage(@RequestBody Image image, @PathVariable("id") Long id) {

		Image img = imageService.addImage(id, image);

		return new ResponseEntity<>(img, HttpStatus.CREATED);
	}

	@Operation(summary = "Adds images.", description = "Adds images associated with a book!", method = "POST",
			parameters = @Parameter(allowEmptyValue = false, name = "id", description = "id of the book."),
			requestBody  = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json"
			, schema = @Schema(implementation = ImageRequestObject.class))))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Successfully added images.",
					content = @Content(mediaType = "application/json", 
					array = @ArraySchema(arraySchema = @Schema(implementation = Image.class)))),
			@ApiResponse(responseCode = "404", description = "The specified Book doesn't exist.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = GenericErrors.class))),
			@ApiResponse(responseCode = "400", description = "Constraints violated.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrors.class)))
	})
	@PostMapping(value = "/images/{id}", produces = "application/json", consumes = "application/json")
	public ResponseEntity<List<Image>> addImages(@RequestBody Image[] images, @PathVariable("id") Long id) {

		List<Image> imgs = Arrays.asList(images);

		List<Image> storedImgs = imageService.addImages(id, imgs);

		return new ResponseEntity<>(storedImgs, HttpStatus.CREATED);
	}

	@Operation(summary = "Removes an image.", description = "Removes an image from the Database!", method = "DELETE",
			parameters = @Parameter(allowEmptyValue = false, name = "id", description = "id of the image that will be removed."))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully removed the image."),
			@ApiResponse(responseCode = "404", description = "The specified Image doesn't exist.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = GenericErrors.class)))
	})
	@DeleteMapping("/image/{id}")
	public ResponseEntity<Void> deleteImage(@PathVariable("id") Long id) {
		imageService.removeImage(id);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Operation(summary = "Removes images.", description = "Removes images from the Database!", method = "DELETE",
			parameters = @Parameter(allowEmptyValue = false, name = "ids",
			description = "array of id of the images that will be removed."))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully removed the images."),
	})
	@DeleteMapping("/images/{ids}")
	public ResponseEntity<Void> deleteSome(@PathVariable("ids") Integer[] ids) {

		imageService.deleteSome(ids);

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@Operation(summary = "Sets an image as cover.", description = "Sets an image as book cover!", method = "PUT",
			parameters = {@Parameter(allowEmptyValue = false, name = "bookId", description = "id of the book."),
						@Parameter(allowEmptyValue = false, name = "imageId", description = "id of the image that will be set as cover.")})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully removed the book."),
			@ApiResponse(responseCode = "404", description = "Either the book or image doesn't exist.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = GenericErrors.class)))
	})
	@PutMapping(value = "/image/{bookId}/cover/{imageId}", produces = "application/json")
	public ResponseEntity<Image> setCoverPicture(@PathVariable("bookId") Long bookId,
			@PathVariable("imageId") Long imageId) {
		Image image = imageService.setCoverPicture(bookId, imageId);

		return new ResponseEntity<Image>(image, HttpStatus.OK);
	}

}
