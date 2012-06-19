package net.sourceforge.stripes.controller;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.util.CryptoUtil;
import net.sourceforge.stripes.util.Log;
import net.sourceforge.stripes.validation.ScopedLocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.TypeConverterFactory;
import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.ValidationMetadata;

/**
 * This class overrides the default stripes binder ({@link DefaultActionBeanPropertyBinder})
 *
 * The only additional functionality it provides is that it checks post
 * type-conversion (through stripes type converters) that the converted value is not null if
 * required is true in validation meta data.
 */
public class SafeActionBeanPropertyBinder extends DefaultActionBeanPropertyBinder {
  private static final Log log = Log.getInstance(SafeActionBeanPropertyBinder.class);

  /**
   * Looks up and caches in a useful form the metadata necessary to perform validations as
   * properties are bound to the bean.
   */
  public void init(Configuration configuration) throws Exception {
      super.init(configuration);
  }

  /**
   * <p>
   * Converts the String[] of values for a given parameter in the HttpServletRequest into the
   * desired type of Object. If a converter is declared using an annotation for the property (or
   * getter/setter) then that converter will be used - if it does not convert to the right type an
   * exception will be logged and values will not be converted. If no Converter was specified then
   * a default converter will be looked up based on the target type of the property. If there is
   * no default converter, then a Constructor will be looked for on the target type which takes a
   * single String parameter. If such a Constructor exists it will be invoked.
   * </p>
   *
   * <p>
   * Only parameter values that are non-null and do not equal the empty String will be converted
   * and returned. So an input array with one entry equaling the empty string, [""], will result
   * in an <b>empty</b> List being returned. Similarly, if a length three array is passed in with
   * one item equaling the empty String, a List of length two will be returned.
   * </p>
   *
   * @param bean the ActionBean on which the property to convert exists
   * @param propertyName the name of the property being converted
   * @param values a String array of values to attempt conversion of
   * @param declaredType the declared type of the ActionBean property
   * @param scalarType if the declaredType is a collection, map or array then this will
   *           be the type contained within the collection/map value/array, otherwise
   *           the same as declaredType
   * @param validationInfo the validation metadata for the property if defined
   * @param errors a List into which ValidationError objects will be populated for any errors
   *            discovered during conversion.
   * @return List<Object> a List of objects containing only objects of the desired type. It is
   *         not guaranteed to be the same length as the values array passed in.
   */
  @SuppressWarnings("unchecked")
  protected List<Object> convert(ActionBean bean, ParameterName propertyName, String[] values,
                                 Class<?> declaredType, Class<?> scalarType,
                                 ValidationMetadata validationInfo, List<ValidationError> errors)
          throws Exception {

      List<Object> returns = new ArrayList<Object>();
      Class returnType = null;

      // Dig up the type converter.  This gets a bit tricky because we need to handle
      // the following cases:
      // 1. We need to simply find a converter for the declared type of a simple property
      // 2. We need to find a converter for the element type in a list/array/map
      // 3. We have a domain model object that implements List/Map and has a converter itself!
      TypeConverterFactory factory = this.getConfiguration().getTypeConverterFactory();
      TypeConverter<?> converter = null;
      Locale locale = bean.getContext().getRequest().getLocale();

      converter = factory.getTypeConverter(declaredType, locale);
      if (validationInfo != null && validationInfo.converter() != null) {
          // If a specific converter was requested and it's the same type as one we'd use
          // for the declared type, set the return type appropriately
          if (converter != null && validationInfo.converter().isAssignableFrom(converter.getClass())) {
              returnType = declaredType;
          }
          // Otherwise assume that it's a converter for the scalar type inside a collection
          else {
              returnType = scalarType;
          }
          converter = factory.getInstance(validationInfo.converter(), locale);
      }
      // Else, if we got a converter for the declared type (e.g. Foo implementes List<Bar>)
      // then convert for the declared type
      else if (converter != null) {
          returnType = declaredType;
      }
      // Else look for a converter for the scalar type (Bar in List<Bar>)
      else {
          converter  = factory.getTypeConverter(scalarType, locale);
          returnType = scalarType;
      }

      log.debug("Converting ", values.length, " value(s) using ", (converter != null ?
          "converter " + converter.getClass().getName()
        : "Constructor(String) if available"));

      for (String value : values) {
          if (validationInfo != null && validationInfo.encrypted()) {
              value = CryptoUtil.decrypt(value);
          }

          if (!"".equals(value)) {
              try {
                  Object retval = null;
                  if (converter != null) {
                      retval = converter.convert(value, returnType, errors);
                  }
                  else {
                      Constructor<?> constructor = returnType.getConstructor(String.class);
                      if (constructor != null) {
                          retval = constructor.newInstance(value);
                      }
                      else {
                          log.debug("Could not find a way to convert the parameter ", propertyName.getName(),
                                    " to a ", returnType.getSimpleName(), ". No TypeConverter could be ",
                                    "found and the class does not ", "have a constructor that takes a ",
                                    "single String parameter.");
                      }
                  }


                /**
                 * KANI patch : check that if retval is null, and the field is required = true
                 * then add a validation error
                 */

               if (retval == null && validationInfo !=null && validationInfo.required() && validationInfo.on().contains(bean.getContext().getEventName())) {
                 ValidationError error = new ScopedLocalizableError("validation.required",
                         "valueNotPresent");
                 error.setFieldValue(null);
                 errors.add(error);
               }


                  // If we managed to get a non-null converted value, add it to the return set
                  if (retval != null) {
                      returns.add(retval);
                  }

                  // Set the field name and value on the error
                  for (ValidationError error : errors) {
                      error.setFieldName(propertyName.getStrippedName());
                      error.setFieldValue(value);
                  }
              }
              catch (Exception e) {
                  log.warn(e, "Looks like type converter ", converter, " threw an exception.");
              }
          }
      }

      return returns;
  }

}
