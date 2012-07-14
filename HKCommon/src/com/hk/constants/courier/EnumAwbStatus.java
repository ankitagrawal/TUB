package com.hk.constants.courier;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jul 13, 2012
 * Time: 9:09:38 AM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumAwbStatus {

    Unused(0L,"Unused"),
    Attach(1L,"Attach"),
    Used(2L,"Used");

  private Long id;
    private String status;

   EnumAwbStatus(Long id , String status){
    this.id=id;
       this.status=status;

   }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
}
