package com.hk.web.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import net.sourceforge.stripes.tag.HtmlTagSupport;

/**
 * @author vaibhav.adlakha
 */
public class ProductVideoTag extends HtmlTagSupport {
    public int doStartTag() throws JspException {
        return SKIP_BODY;
    }

    public int doEndTag() throws JspException {

        JspWriter out = getPageContext().getOut();
        boolean isSecure = pageContext.getRequest().isSecure();
        
        if(isSecure){
            videoCode = videoCode.replace("http", "https");
        }
        
        try {
            out.write(videoCode);
        } catch (IOException e) {
            // should not happen
        }
        
        //set("src", HKImageUtils.getS3ImageUrl(size, imageId, isSecure));
        //writeSingletonTag(out, "img");

        return EVAL_PAGE;
    }

    String videoCode;

    public void setVideoCode(String videoCode) {
        this.videoCode = videoCode;
    }

}
