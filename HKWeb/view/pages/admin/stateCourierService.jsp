<%@ page import="com.hk.constants.courier.StateList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.courier.StateCourierServiceAction" var="scsaBean" event="pre"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="State Courier Service">
    <s:layout-component name="htmlHead">
        <script type="text/javascript">
            var nextIndex = 0;
            $(document).ready(function() {
                $('.addRowButton').click(function() {
                    var lastIndex = $('.lastRow').attr('count');
                    if (!lastIndex) {
                        lastIndex = -1;
                    }
                    var limitIndex = eval(lastIndex + "+1");

                    $('.lastRow').removeClass('lastRow');
                    var innerRowHtml = '<tr count = "' + limitIndex + 'class="lastRow">' +
                                       '<td>' +
                                       limitIndex +
                                       '</td>' +
                                       '<td>' +
                                       '<input text="text" name ="'+${scsaBean.stateCourierService.courier.name}+'" />' +
                                       '<td>' +
                                       '<td>' +
                                       '<input text="text" name ="'+${scsaBean.stateCourierService.preference}+'" />' +
                                       '</td>'
                            ;
                    $('#featureTable').append(innerRowHtml);
                    nextIndex = eval(nextIndex + "+1");

                });


            });

        </script>
    </s:layout-component>


    <s:layout-component name="heading">State Courier Service</s:layout-component>
    <s:layout-component name="content">
        <div style="height:100px;">
            <table>
                <tr>
                    <th>State</th>
                    <br/>

                </tr>
                <tr>
                    <s:form beanclass="com.hk.web.action.admin.courier.StateCourierServiceAction">
                        <td>
                            <s:select name="state">
                                <s:options-collection collection="${scsaBean.stateList}"/>
                            </s:select>
                        </td>
                        <td>
                            <s:submit name="search" value="Save"/>
                        </td>
                    </s:form>
                </tr>
            </table>
        </div>

        <div class="clear" style="height:10px;"></div>

        <s:form beanclass="com.hk.web.action.admin.courier.StateCourierServiceAction">
            <s:hidden name="state"  value="${scsaBean.state}"/>
            <table id="featureTable">
                <%--<c:if test="${scsaBean.state != null}">--%>
                <tr>
                    <th>Selected State</th>
                </tr>
                <tr> ${scsaBean.state} </tr>

                <tr>
                    <th>S.No.</th>
                    <th>Courier Name</th>
                    <th>Preference</th>

                </tr>
                <div>
                    <c:forEach items="${scsaBean.stateCourierServiceList}" var="stateCourierService" varStatus="count">
                        <s:hidden name="stateCourierService.id"/>
                       <tr count="${count.index}" class="${count.last ? 'lastRow':''}">
                        <td> ${count.index+1}</td>
                        <td>
                                ${stateCourierService.courier.name}
                        </td>
                        <td>
                                ${stateCourierService.preference}
                        </td>

                    </tr>

                    </c:forEach>

                    <%--</c:if>--%>
            </table>
            <s:submit name="save" value="SAVE"/>
            </div>

            <div class="clear" style="height:50px;"></div>

            <div style="font-weight:bold; font-size:150%;">
                <a href="#" class="addRowButton">Add New Row</a>
            </div>

        </s:form>

    </s:layout-component>
</s:layout-render>
