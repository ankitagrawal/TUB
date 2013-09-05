package com.hk.edge.response.variant;

import org.codehaus.jackson.annotate.JsonProperty;

import com.hk.edge.constants.DtoJsonConstants;

public class VariantImage {

    @JsonProperty(DtoJsonConstants.ALT_TEXT)
    private String altText;
    @JsonProperty(DtoJsonConstants.CAPTION)
    private String caption;
    @JsonProperty(DtoJsonConstants.IMG_S_LINK)
    private String slink;
    @JsonProperty(DtoJsonConstants.IMG_M_LINK)
    private String mlink;
    @JsonProperty(DtoJsonConstants.IMG_L_LINK)
    private String llink;

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getSlink() {
        return slink;
    }

    public void setSlink(String slink) {
        this.slink = slink;
    }

    public String getMlink() {
        return mlink;
    }

    public void setMlink(String mlink) {
        this.mlink = mlink;
    }

    public String getLlink() {
        return llink;
    }

    public void setLlink(String llink) {
        this.llink = llink;
    }

}
