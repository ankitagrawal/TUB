package com.hk.helper;

import com.hk.dto.ProductOptionDto;
import com.hk.pact.service.catalog.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ajeet
 * Date: 13 Aug, 2012
 * Time: 11:39:19 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class CatalogFilter {

	@Autowired
	private CategoryService categoryService;

	private Map<String, Set<ProductOptionDto>> filterMap;
	private Set<ProductOptionDto> filterOptionDtoSet;

	public Map<String, Set<ProductOptionDto>> getFilterOptions(String category) {
		filterMap = new HashMap<String, Set<ProductOptionDto>>();
		List<ProductOptionDto> optionDtoList = categoryService.getFilterOptions(category);
		for (ProductOptionDto productOptionDto : optionDtoList) {
			String option = productOptionDto.getName().toUpperCase();
			if (filterMap.containsKey(option)) {
				filterOptionDtoSet = filterMap.get(option);
			} else {
				filterOptionDtoSet = new HashSet<ProductOptionDto>(0);
			}
			filterOptionDtoSet.add(productOptionDto);
			filterMap.put(option, filterOptionDtoSet);
		}
		return filterMap;
	}

	public Map<String, Set<ProductOptionDto>> getFilterMap() {
		return filterMap;
	}
	
}
