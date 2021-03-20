package com.app.bookeepy;

import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.stereotype.Component;

@Component
public class PagingConfig implements PageableHandlerMethodArgumentResolverCustomizer {

	@Override
	public void customize(PageableHandlerMethodArgumentResolver pr) {
		pr.setOneIndexedParameters(true);
	}
} 