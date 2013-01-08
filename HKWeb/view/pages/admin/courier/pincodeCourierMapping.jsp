<%--
  Created by IntelliJ IDEA.
  User: Shrey
  Date: Jan 8, 2013
  Time: 12:45:21 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.courier.PincodeCourierMappingAction" var="pcma"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Pincode Courier Mapping">
<s:layout-component name="htmlHead">
    <script type="text/javascript">
     $(document).ready(function(){
        $('.check').click(function(){
           var text = $(this).val();
           if(text == "" || text == null){
               alert("Pincode can't be left Empty");
               return false;
           }
            else if(isNaN(text)){
             alert("Pincode must contain numbers only");
               return false;
           }
        });
         $('.check').click(function(){
             if ($("#courierContainer").css("display","none")) {
                 $("#courierContainer").css("display","block");
                    $("#courierContainer").slideDown("slow");
                      } else {
                    $("#courierContainer").css("display","none");
                   }
         });
     });
    </script>
</s:layout-component>
<s:layout-component name="content">
<s:form beanclass="com.hk.web.action.admin.courier.PincodeCourierMappingAction">
    <s:text name="pin"/>
    <s:button name="search" value="Basic Search" class="check"/>
    <s:button name="detailedAnalysis" value="Detailed Analysis" class="check"/>

<div id="courierContainer" class="pincodeCourier" style="display:none;">
    <table class="zebra_vert">
          <thead>
          <tr>
            <th>Courier Name</th>
            <th>Active</th>
            <th>Prepaid Air</th>
            <th>Prepaid Ground</th>
            <th>COD Air</th>
            <th>COD Ground</th>
          </tr>
          </thead>
        <c:choose>
          <c:when test="${pcma.applicableShipmentServices!=null and fn:length(pcma.applicableShipmentServices)>0}">
             <c:forEach items="${pcma.applicableShipmentServices}" var="aShipServices">

             </c:forEach>
          </c:when>
            <c:otherwise>
                <c:when test="${pcma.pincodeCourierMappings!=null and fn:length(pcma.pincodeCourierMappings)>0}">
                 <c:forEach items="${pcma.pincodeCourierMappings}" var="pCourierMap">

                 </c:forEach>
                </c:when>
            </c:otherwise>
        </c:choose>
    </table>
</div>
</s:form>
  </s:layout-component>
  </s:layout-render>