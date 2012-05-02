package net.sourceforge.stripes.validation;

import java.util.Map;

/**
 * Author: Kani
 * Date: Mar 18, 2009
 */
public class ReloadableValidationMetadataProvider extends DefaultValidationMetadataProvider {

  @Override
  public Map<String, ValidationMetadata> getValidationMetadata(Class<?> beanType) {
      return loadForClass(beanType);
  }

}
