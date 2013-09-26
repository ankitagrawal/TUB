<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.shippingOrder.DropShipChangeAction" var="dropShip"/>

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
                    });
        </script>

        <s:layout-component name="heading">Change Shipping Order Status </s:layout-component>
        <s:layout-component name="content">
            <s:form beanclass="com.hk.web.action.admin.courier.ShippingOrderStatusChangeAction" method="get"
                    autocomplete="false">
                <fieldset class="top_label">
                    <h3></h3><label>Gateway Order ID</label></h3>
                    <s:text name="gatewayOrderId" id="gatewayOrderId"/>
                    <br>
                    <s:submit name="search" value="Search" id="search"/>
                    <div class="clear"></div>
                </fieldset>
                <c:if test="${changeSOStatus.shippingOrder!=null}">
                    <fieldset>
                        <h2>Change SO DropShip Status</h2>
                        <br>
                        Current DropShip Status : <span
                            style="color:blue;"> ${dropShip.shippingOrder.dropShipping}</span>
                        <c:choose>
                            <c:when test="${dropShip.shippingOrder.dropShipping==true}">
                                <s:submit name="removeDropShip" value="RemoveDropShip" id="saveStatus"/>
                            </c:when>
                            <c:otherwise>
                                <s:submit name="addDropShip" value="AddDropShip" id="saveStatus"/>
                            </c:otherwise>

                        </c:choose>
                    </fieldset>
                </c:if>
            </s:form>
        </s:layout-component>
    </s:layout-component>
</s:layout-render>


