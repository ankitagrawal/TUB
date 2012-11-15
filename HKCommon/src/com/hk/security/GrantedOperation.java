package com.hk.security;

import java.io.Serializable;


/**
 * 
 * @author vaibhav.adlakha
 *
 */
public interface GrantedOperation extends Serializable{
    
   
    String getOperation();
    

}
