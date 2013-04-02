
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.courier.ShippingOrderStatusChangeAction" var="changeSOStatus"/>


<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Change Shipping Order Status">
<s:layout-component name="htmlHead">

    <script type="text/javascript">
            $(document).ready(function(){
              $('#search').click(function(){
                     var gateWayOrderId = $('#gatewayOrderId').val();
                      if(gateWayOrderId == "" || gateWayOrderId == null){
                          alert("Gateway Order can't be Empty");
                          return false;
                      }
                  });
                $('#saveStatus').click(function(){
                   var updateStatus=$('#updateStatus').val();
                    if(updateStatus=="" || updateStatus==null){
                        alert("Please select required status..!!!");
                        return false;
                    }
                });
            });
    </script>

</s:layout-component>
<s:layout-component name="heading">Change Shipping Order Status </s:layout-component>
<s:layout-component name="content">
  <s:form beanclass="com.hk.web.action.admin.courier.ShippingOrderStatusChangeAction" method="get" autocomplete="false">

      <fieldset class="top_label">
                <h3></h3><label>Gateway Order ID</label></h3>
                    <s:text name="gatewayOrderId" id="gatewayOrderId"/>
                    <br>
                    <s:submit name="search" value="Search" id="search"/>
                    <div class="clear"></div>
          </fieldset>
          <c:if test="${changeSOStatus.shippingOrder!=null}">
              <fieldset>
              <h2>Change Shipping Order Status</h2>
              <br>
              Current Shipping Order Status : <span style="color:blue;"> ${changeSOStatus.shippingOrder.shippingOrderStatus.name}</span>
              <br> Shipping Order Status Name<s:select name="enumSoUpdatedStatusId" id="updateStatus">
              <s:option value="">--Select--</s:option>
                                <c:forEach items="${changeSOStatus.SOMapping}" var="soStatus">
                                  <s:option value="${soStatus}">${soStatus.name}</s:option>
                                </c:forEach>
                            </s:select>
              <br>
              <input type="hidden" name="shippingOrder" value="${changeSOStatus.shippingOrder.id}" />
              <s:submit name="saveStatus" value="Save" id="saveStatus"/>
          </fieldset>
           </c:if>
          </s:form>
</s:layout-component>
</s:layout-render>
