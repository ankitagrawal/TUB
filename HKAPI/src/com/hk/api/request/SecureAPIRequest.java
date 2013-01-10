package com.hk.api.request;

public abstract class SecureAPIRequest implements APIRequest{
    
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
    
    
    

}
