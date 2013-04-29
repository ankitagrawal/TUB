<%@ page import="com.hk.constants.core.HealthkartConstants" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.user.RequestCallbackAction" var="sdcActionBean" event="pre"/>
<s:layout-render name="/layouts/modal.jsp">
    <s:layout-component name="modal">
        <script type="text/javascript">
            $(document).ready(function () {
                $('.notifyMeValidate').click(function () {
                    var emailRegEx = /^[a-z0-9_\+-]+(\.[a-z0-9_\+-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*\.([a-z]{2,4})$/;
                    var nameRegEx = /^[a-zA-Z'.\s]{3,45}$/;
                    //var mobileRegEx=/^((\+)?(\d{2}[-]))?(\d{10}){1}?$/;
                    var mobileRegEx = /^([+]?(\d)+[-]?(\d)+)$/;
                    var notifyMeEmail = $('.emailId').val();
                    var notifyMeName = $('.userName').val();
                    var notifyMeMobile = $('.phoneNo').val();
                    if (!emailRegEx.test(notifyMeEmail)) {
                        alert("Please enter correct email address.");
                        return false;
                    }
                    if (!nameRegEx.test(notifyMeName)) {
                        alert("Please enter correct User Name.");
                        return false;
                    }
                    if (notifyMeMobile != '') {
                        if (notifyMeMobile.length > 15 || !mobileRegEx.test(notifyMeMobile)) {
                            alert("Please enter correct Mobile No.");
                            return false;
                        }
                    }
                });
            });
        </script>
    </s:layout-component>
    <s:layout-component name="heading">
        Notify Me
    </s:layout-component>
    <s:layout-component name="content">

        <div id class="msg red" style="text-align: center; padding: 2px 0 2px 0; font-size: 1em;"></div>

        <div class="notifyForm">
            <s:form beanclass="com.hk.web.action.core.user.NotifyMeAction" id="notifyMeForm">
                <div style="text-align: left; padding: 5px 0 5px 0; font-size: 1em;">
                    <table>
                        <tr>
                            <td style="text-align: right;">
                                Enter your name:<span class='aster' title="this field is required">*</span>
                            </td>
                            <td>
                                <s:text class="userName" id="notifyMeName" name="notifyMe.name"/> <br/><br/>
                            </td>
                        </tr>
                        <tr>
                            <td style="text-align: right;">
                                Enter your email address:<span class='aster' title="this field is required">*</span>
                            </td>
                            <td>
                                <s:text class="emailId" id="notifyMeEmail" name="notifyMe.email"/> <br/><br/>
                            </td>
                        </tr>
                        <tr>
                            <td style="text-align: right;">
                                Enter your phone / mobile number:
                            </td>
                            <td>
                                <s:text class="phoneNo" id="notifyMePhone" name="notifyMe.phone"/> <br/><br/>
                            </td>
                        </tr>
                    </table>

                    <s:hidden id="notifyMeProductVariant" name="notifyMe.productVariant"/>
                </div>
                <s:submit class="button_orange notifyMeValidate" name="notifyMe" value="Notify"/>
            </s:form>

        </div>
        <script type="text/javascript">
            function _registerNotifyMe(res) {
                if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                    $('#notifyMeWindow .msg').html(res.message);
                    $('#notifyMeWindow .notifyForm').hide();
                } else if (res.code == '<%=HealthkartResponse.STATUS_ERROR%>') {
                    $('#notifyMeWindow .msg').html(res.message);
                }
                else if (res.code == '<%=HealthkartResponse.STATUS_ACCESS_DENIED%>') {
                    $('#notifyMeWindow .msg').html(res.message);
                    $('#notifyMeWindow .notifyForm').hide();
                }

            }
            $('#notifyMeForm').ajaxForm({dataType:'json', success:_registerNotifyMe});
        </script>
    </s:layout-component>

</s:layout-render>
