/**
 * 
 */
package com.hk.store;

import com.hk.domain.catalog.category.Category;

/**
 * @author Ankit Chhabra
 * 
 */
public class CategoryDto {

	private String name;
	private String displayName;

	private int prodCount;

	public CategoryDto() {
	}

	public CategoryDto(String name, String displayName, int prodCount) {
		this.name = name;
		this.displayName = displayName;
		this.prodCount = prodCount;
	}

	/**
	 * This method converts a CategoryLoyaltyDto object into a category object.
	 * 
	 * @param dto
	 * @return
	 */
	public static Category toCategory(CategoryDto dto) {
		Category cat = new Category();
		cat.setName(dto.getName());
		cat.setDisplayName(dto.getDisplayName());

		return cat;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return this.displayName;
	}

	/**
	 * @param displayName
	 *            the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the count
	 */
	public int getProdCount() {
		return this.prodCount;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setProdCount(int prodCount) {
		this.prodCount = prodCount;
	}

}
