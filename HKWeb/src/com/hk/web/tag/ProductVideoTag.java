package com.hk.web.tag;

import net.sourceforge.stripes.tag.HtmlTagSupport;
import net.sourceforge.stripes.util.ssl.SslUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;

/**
 * @author vaibhav.adlakha
 */
public class ProductVideoTag extends HtmlTagSupport {
    public int doStartTag() throws JspException {
        return SKIP_BODY;
    }

    public int doEndTag() throws JspException {

        JspWriter out = getPageContext().getOut();

        if(SslUtil.isSecure()){
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
