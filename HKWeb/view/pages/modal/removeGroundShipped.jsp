<%--
  Created by IntelliJ IDEA.
  User: Ankit
  Date: Aug 24, 2012
  Time: 3:19:37 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/modal.jsp">
  <s:layout-component name="heading">
    Remove GroundShipped Item
  </s:layout-component>
    
    <s:layout-component name="content">
   <div>
       <s:form beanclass="" id= "">
          <div style="text-align: center; padding: 5px 0 5px 0; font-size: 1em;">
          This product does not have Shipping Facility, Please remove the item.
          <s:text id="forgotEmail" name="email"/>
          <br/> <br/>
          <span class="special" style="font-size: 12px;">(your password will be emailed to you at this address)</span>
        </div>

       </s:form>
       
   </div>

    </s:layout-component>

  </s:layout-render>