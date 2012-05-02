package com.hk.web;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;

import com.akube.framework.util.HtmlUtils;

/**
 * Note : This TypeConverter is not to be auto discovered, and should only be used manually
 *        So it is kept outside the validation package (which is auto discovered by Stripes by default)
 * Author: Kani
 * Date: Jun 30, 2009
 */

public class SafeHtmlTypeConverter implements TypeConverter<String> {
  public void setLocale(Locale locale) {
  }

   AntiSamy antiSamy;

  public String convert(String str, Class<? extends String> aClass, Collection<ValidationError> validationErrors) {
    try {
      CleanResults cr;
      String newLineReplacementExpression = "---new!!!!line---";
      String strNew = str.replaceAll("\n", newLineReplacementExpression);
      cr = antiSamy.scan(strNew);
      String safeHtml = cr.getCleanHTML().replaceAll(newLineReplacementExpression, "\n");

      return HtmlUtils.unescapeHTML(safeHtml);
    } catch (ScanException e) {
      validationErrors.add(new LocalizableError("converter.antisamy.error"));
    } catch (PolicyException e) {
      validationErrors.add(new LocalizableError("converter.antisamy.policy"));
    }
    return "";
  }
}
