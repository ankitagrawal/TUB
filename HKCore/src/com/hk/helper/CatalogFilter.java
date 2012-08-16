package com.hk.helper;

import com.hk.dto.ProductOptionDto;
import com.hk.dto.PriceRangeDto;
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

	private Map<String, List<ProductOptionDto>> filterMap;
	private List<ProductOptionDto> filterOptionDtoSet;

	public Map<String, List<ProductOptionDto>> getFilterOptions(List<String> categoryNames) {
		filterMap = new HashMap<String, List<ProductOptionDto>>();
		List<ProductOptionDto> optionDtoList = categoryService.getFilterOptions(categoryNames);
		if (optionDtoList != null) {
			for (ProductOptionDto productOptionDto : optionDtoList) {
				String option = productOptionDto.getName().toUpperCase();
				if (filterMap.containsKey(option)) {
					filterOptionDtoSet = filterMap.get(option);
				} else {
					filterOptionDtoSet = new ArrayList<ProductOptionDto>(0);
				}
				filterOptionDtoSet.add(productOptionDto);
				filterMap.put(option, filterOptionDtoSet);
			}
		}
		return filterMap;
	}

	public PriceRangeDto getPriceRange(List<String> categoryNames) {
		PriceRangeDto priceRange = categoryService.getPriceRange(categoryNames);
		return priceRange;
	}

	public Map<String, List<ProductOptionDto>> getFilterMap() {
		return filterMap;
	}
}
