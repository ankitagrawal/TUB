package com.hk.web.tag;

import java.util.Collection;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import net.sourceforge.stripes.tag.InputOptionsCollectionTag;
import net.sourceforge.stripes.util.bean.BeanUtil;
import net.sourceforge.stripes.util.bean.EvaluationException;
import net.sourceforge.stripes.util.bean.ParseException;

import com.hk.service.ServiceLocatorFactory;


@SuppressWarnings("unchecked")
public class MasterDataOptionsTag extends InputOptionsCollectionTag implements Tag {

    private Class service;
    private String serviceProperty;


  public Class getService() {
    return service;
  }

  public void setService(Class service) {
    this.service = service;
  }

  public String getServiceProperty() {
    return serviceProperty;
  }

  public void setServiceProperty(String serviceProperty) {
    this.serviceProperty = serviceProperty;
  }

    /**
     * Iterates through the collection and generates the list of Entry objects that can then
     * be sorted and rendered into options. It is assumed that each element in the collection
     * has non-null values for the properties specified for generating the label and value.
     *
     * @return SKIP_BODY in all cases
     * @throws javax.servlet.jsp.JspException if either the label or value attributes specify properties that are
     *         not present on the beans in the collection
     */
    @SuppressWarnings("unchecked")
    @Override
    public int doStartTag() throws JspException {
      // get the collection from service and serviceProperty
      Collection someCollection = null;
      Object serviceObject = null;
      try {
	      
        serviceObject = ServiceLocatorFactory.getService(service);
      } catch (Exception e) {
        throw new IllegalArgumentException("Could not inject object of class "+service.getName());
      }
      try {
        someCollection = (Collection) BeanUtil.getPropertyValue(serviceProperty, serviceObject);
      } catch (ParseException e) {
        throw new IllegalArgumentException("Could not obtain a collection from object of "+service.getName()+" with property "+serviceProperty);
      } catch (EvaluationException e) {
        throw new IllegalArgumentException("Could not obtain a collection from object of "+service.getName()+" with property "+serviceProperty);
      } catch (ClassCastException e) {
        throw new IllegalArgumentException("Could not obtain a collection from object of "+service.getName()+" with property "+serviceProperty);
      }
      setCollection(someCollection);

      return super.doStartTag();
    }

}
