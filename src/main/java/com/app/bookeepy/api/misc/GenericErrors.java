package com.app.bookeepy.api.misc;

import java.time.LocalDateTime;

public interface GenericErrors {
	
	public LocalDateTime getTimeStamp();
	
	public String getStatus();
	
	public String getMessage();

}
