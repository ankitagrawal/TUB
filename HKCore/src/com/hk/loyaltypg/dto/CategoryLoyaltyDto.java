/**
 * 
 */
package com.hk.loyaltypg.dto;

import com.hk.domain.catalog.category.Category;

/**
 * @author Ankit Chhabra
 *
 */
public class CategoryLoyaltyDto {
	
	private String              name;
    private String              displayName;
    private int 				count;
    
    CategoryLoyaltyDto () {}
	
    
    /**
     * This method converts a CategoryLoyaltyDto object into a category object.
     * @param dto
     * @return
     */
    public static Category toCategory(CategoryLoyaltyDto dto) {
    	Category cat = new Category();
    	cat.setName(dto.getName());
    	cat.setDisplayName(dto.getDisplayName());
    	
    	return cat;
    }
    
   
    /**
     * This method does as name suggests.
     * @param dto
     * @return
     */
  /*  public static Category toCategoryDto(List<Category> listCat) {
    	
    	List<CategoryLoyaltyDto> dtoList = new 
    	for (Category cat: listCat) {
    		cat.setName(dto.getName());
        	cat.setDisplayName(dto.getDisplayName());
        		
    	}
    	
    	return cat;
    }
  */ 
    
    /**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @param name the name to set
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
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	/**
	 * @return the count
	 */
	public int getCount() {
		return this.count;
	}
	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}
    

}
