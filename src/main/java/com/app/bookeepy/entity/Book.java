package com.app.bookeepy.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity(name = "books")
@Table(name = "books")
public class Book {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String author;

    private String edition;

    private String status;

    @Column(unique = true)
    private String isbn;

    @Valid
    @Size(min = 1, message = "Error: The book must have at least one image.")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "book", fetch = FetchType.LAZY)
    private List<Image> images = new ArrayList<>();

    public Book() {
    }

    public Book(String title, String author, String edition, String status, String isbn) {
        super();
        this.title = title;
        this.author = author;
        this.edition = edition;
        this.status = status;
        this.isbn = isbn;
    }

    public Book(Long id, String title, String author, String edition, String status, String isbn) {
        super();
        this.id = id;
        this.title = title;
        this.author = author;
        this.edition = edition;
        this.status = status;
        this.isbn = isbn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotBlank(message = "Error: The book title is mandatory!")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NotBlank(message = "Error: The book author is mandatory!")
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @NotBlank(message = "Error: The book edition is mandatory!")
    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    @NotBlank(message = "Error: The specified book must be Owned or Wished!")
    public String getStatus() {
        return status;
    }


    @Pattern(regexp = "(ISBN[-]*(1[03])*[ ]*(: ){0,1})*(([0-9Xx][- ]*){13}|([0-9Xx][- ]*){10})", message = "Error: The specified ISBN is invalid!")
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public void addImage(Image image) {
        this.images.add(image);
        image.setBook(this);
    }

    public void addImages(List<Image> imgs) {
        imgs.forEach(image -> image.setBook(this));
        this.images.addAll(imgs);

    }

    public void removeImage(Image image) {
        this.images.remove(image);
        image.setBook(null);
    }

    public void removeImages() {
        images.forEach(image -> removeImage(image));
    }

    @Override
    public String toString() {
        return "Book [id=" + id + ", title=" + title + ", author=" + author + ", edition=" + edition + ", status="
                + status + "]";
    }

}
