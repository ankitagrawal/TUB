package com.hk.web.action.test;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;

@Component
public class DoAdditionAction extends BaseAction {
  @Validate(required = true)
  double number1;
  @Validate(required = true)
  double number2;
  double result;

  public double getNumber1() {
    return number1;
  }

  public double getNumber2() {
    return number2;
  }

  public double getResult() {
    return result;
  }

  public void setNumber1(double number1) {
    this.number1 = number1;
  }

  public void setNumber2(double number2) {
    this.number2 = number2;
  }

  public Resolution addition() {
    result = number1 + number2;
    return new ForwardResolution("/pages/test/addition.jsp");
  }

  public Resolution subtraction() {
    result = number1 - number2;
    return new ForwardResolution("/pages/test/addition.jsp");
  }
}

