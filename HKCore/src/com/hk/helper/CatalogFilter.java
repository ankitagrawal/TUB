package com.hk.helper;

import com.hk.dto.ProductOptionDto;
import com.hk.pact.service.catalog.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private Map<String, List<ProductOptionDto>> filterMap = new HashMap<String, List<ProductOptionDto>>();
	private List<ProductOptionDto> filterOptionDtoList;

	public Map<String, List<ProductOptionDto>> getFilterOptions(String category) {
		List<ProductOptionDto> optionDtoList = categoryService.getFilterOptions(category);
		for (ProductOptionDto productOptionDto : optionDtoList) {
			String option = productOptionDto.getName().toUpperCase();
			if (filterMap.containsKey(option)) {
				filterOptionDtoList = filterMap.get(option);
			} else {
				filterOptionDtoList = new ArrayList<ProductOptionDto>();
			}
			filterOptionDtoList.add(productOptionDto);
			filterMap.put(option, filterOptionDtoList);
		}
		return filterMap;
	}

	public Map<String, List<ProductOptionDto>> getFilterMap() {
		return filterMap;
	}
}
