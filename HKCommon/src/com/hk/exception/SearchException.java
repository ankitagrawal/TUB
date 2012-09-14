package com.hk.exception;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 7/13/12
 * Time: 9:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class SearchException extends Exception{

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String source;
    private String message;
    public SearchException(String source, String message, Throwable cause){
        super(cause);
        this.source = source;
        String finalMsg = String.format("%s not working. Please check the service. %s", source, message);
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String getMessage(){
        return this.message;
    }
}
