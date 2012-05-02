package web.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import net.sourceforge.stripes.tag.HtmlTagSupport;

import com.hk.constants.catalog.image.EnumImageSize;
import com.hk.util.ImageManager;

public class ProductImageTag extends HtmlTagSupport {
  public int doStartTag() throws JspException {
    return SKIP_BODY;
  }

  public int doEndTag() throws JspException {

    JspWriter out = getPageContext().getOut();
    set("src", ImageManager.getS3ImageUrl(size, imageId));
    writeSingletonTag(out, "img");

    return EVAL_PAGE;
  }

  Long imageId;
  EnumImageSize size;

  public void setImageId(Long id) {
    this.imageId = id;
  }

  public void setSize(EnumImageSize enumImageSize) {
    this.size = enumImageSize;
  }

}
