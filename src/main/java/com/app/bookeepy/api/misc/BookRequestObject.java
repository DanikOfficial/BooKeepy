package com.app.bookeepy.api.misc;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public interface BookRequestObject {

	@NotBlank(message = "Error: The book title is mandatory!")
	public String getTitle();

	@NotBlank(message = "Error: The book author is mandatory!")
	public String getAuthor();

	@NotBlank(message = "Error: The book edition is mandatory!")
	public String getEdition();

	@NotBlank(message = "Error: The specified book must be Owned or Wished!")
	public String getStatus();

	@Pattern(regexp = "(ISBN[-]*(1[03])*[ ]*(: ){0,1})*(([0-9Xx][- ]*){13}|([0-9Xx][- ]*){10})", message = "Error: The specified ISBN is invalid!")
	public String getIsbn();
}
