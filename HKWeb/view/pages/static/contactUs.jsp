<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory" %>
<%@ page import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ page import="com.hk.constants.core.HealthkartConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/default.jsp" pageTitle="Contact Us for Customer Support or Business Queries">

  <s:layout-component name="heading">Contact Us</s:layout-component>

  <s:layout-component name="htmlHead">
    <meta name="Keywords" content="Customer care HealthKart.com"/>
    <meta name="Description" content="Call: 0124-4616444, Email: info@healthkart.com - Available 7 days a week from 9am to 12am midnight"/>
  </s:layout-component>

  <s:layout-component name="left_col">
    <div style="width: 45%; float: left; background: #fcfcfc; padding: 15px; ">
      <s:form beanclass="com.hk.web.action.pages.ContactAction">
        <div class="left">
          <h2>Use the contact form below &darr;</h2>

          <div class='label'>Name <span class="aster">*</span></div>
          <s:text name="name"/>
          <div class='label'>Email <span class="aster">*</span></div>
          <s:text name="email"/>
          <div class='label'>Phone</div>
          <s:text name="phone"/>

          <div class='label'>Subject <span class="aster">*</span></div>
          <s:select name="msgSubject">
            <s:option value="General feedback/support">General feedback/support</s:option>
            <s:option value="Order Status">Order Status</s:option>
            <s:option value="Payment related">Payment related</s:option>
            <s:option value="Feature Request">Feature Request</s:option>
            <s:option value="Product Request">Product Request</s:option>
            <s:option value="Other">Other</s:option>
          </s:select>
          <div class='label'>Your Message <span class="aster">*</span></div>
          <s:textarea name="msgText" rows="15" cols="50"/>
          <div class='label'>Tell us you are human <span class="aster">*</span></div>
          <%
            ReCaptcha captcha = ReCaptchaFactory.newReCaptcha(HealthkartConstants.recaptchaPublicKey, HealthkartConstants.recaptchaPrivateKey, false);
            out.print(captcha.createRecaptchaHtml(request.getParameter("error"), null));
          %>
          <div style="float: right; font-size: 0.9em;  margin-top: 10px; width: 50%;">
            <s:submit name="sendMessage" value="Send Message" class="button"/>
          </div>
        </div>
      </s:form>
    </div>

    <div style="padding: 0 20px; width: 40%; float: left;">

      <h2>Email Us</h2>

      <p>For customer support, feedback and any other issues write to <a href="mailto:info@healthkart.com">info@healthkart.com</a>
      </p>

      <br/>

      <h2>Call Us</h2>

      <p style="font-size: 1.2em;">0124-4616444 <br/> <span style="font-size: 10px;">9 AM to 12 AM (midnight)</span></p>

      <%--<div
          style="border-top: 2px solid #ff9999; border-bottom: 2px solid #ff6666; height: 25px; padding-top: 3px; font-size: 11px;">
        Our customer care phone lines will be down for maintenance from 5:30pm to 9:00pm on 29th June 2013. Sincere apologies for the inconvenience caused.
      </div>--%>

      <br/>

      <%--<p style="color: gray">If you are not satisfied with the response, please write to us at <a href="mailto:ceo@healthkart.com">ceo@healthkart.com</a></p>--%>

      <h2>Headquarters</h2>

      <p>
	      Aquamarine HealthCare Pvt. Ltd.<br/>
        4th Floor,
        Parshavnath Arcadia<br/>
        1, MG Road<br/>
        (Opposite to Motorola)<br/>
        Sector-14<br/>
        Gurgaon - 122001<br/>
      </p>

    </div>
  </s:layout-component>

</s:layout-render>


