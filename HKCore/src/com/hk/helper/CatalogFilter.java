package com.hk.helper;

import com.hk.dto.ProductOptionDto;
import com.hk.dto.PriceRangeDto;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.pact.dao.BaseDao;
import com.hk.domain.catalog.product.ProductOption;
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

	@Autowired
	private BaseDao baseDao;

	private Map<String, List<ProductOptionDto>> filterMap;
	private List<ProductOptionDto> filterOptionDtoSet;

	public Map<String, List<ProductOptionDto>> getFilterOptions(String primaryCategory, List<String> categoryNames, List<Long> filterOptions, Double minPrice, Double maxPrice) {
		filterMap = new HashMap<String, List<ProductOptionDto>>();
		List<ProductOptionDto> allOptionDtoList = categoryService.getFilterOptions(primaryCategory, categoryNames, null, minPrice, maxPrice);
		List<ProductOptionDto> filteredOptionDtoList = categoryService.getFilterOptions(primaryCategory, categoryNames, filterOptions, minPrice, maxPrice);
		List<String> productOptionHeads = getFilteredProductOptionHeads(filterOptions);
		if (filteredOptionDtoList != null) {
			for (ProductOptionDto productOptionDto : allOptionDtoList) {
				String option = productOptionDto.getName().toUpperCase();
				if (filterMap.containsKey(option)) {
					filterOptionDtoSet = filterMap.get(option);
				} else {
					filterOptionDtoSet = new ArrayList<ProductOptionDto>(0);
				}
				ProductOptionDto filteredOption = getFilteredProductOptionDto(filteredOptionDtoList, productOptionDto.getId());
				if (filteredOption != null) {
					productOptionDto.setApplicable(true);
					productOptionDto.setQty(filteredOption.getQty());
				} else {
					if (productOptionHeads.contains(option)) {
						productOptionDto.setApplicable(true);
						productOptionDto.setQty(null);
					} else {
						productOptionDto.setApplicable(false);
						productOptionDto.setQty(0L);
					}
				}

				filterOptionDtoSet.add(productOptionDto);
				filterMap.put(option, filterOptionDtoSet);
			}
		}
		return new TreeMap(filterMap);
	}

	public ProductOptionDto getFilteredProductOptionDto(List<ProductOptionDto> filteredOptionDtoList, Long id) {
		for (ProductOptionDto optionDto : filteredOptionDtoList) {
			if (optionDto.getId().equals(id)) {
				return optionDto;
			}
		}
		return null;
	}

	public List<String> getFilteredProductOptionHeads(List<Long> filterOptions) {
		List<String> filterOptionHeads = new ArrayList<String>();
		for (Long filterOption : filterOptions) {
			ProductOption productOption = baseDao.get(ProductOption.class, filterOption);
			if (productOption != null) filterOptionHeads.add(productOption.getName().toUpperCase());
		}
		return filterOptionHeads;
	}

	public PriceRangeDto getPriceRange(String primaryCategory, List<String> categoryNames, List<Long> filterOptions) {
		PriceRangeDto priceRange = categoryService.getPriceRange(primaryCategory, categoryNames, filterOptions);
		return priceRange;
	}

	public Map<String, List<ProductOptionDto>> getFilterMap() {
		return filterMap;
	}
}
