<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.user.MyAccountAction" var="maa"/>
<s:layout-render name="/layouts/default.jsp">
    <s:layout-component name="heading">Your Account</s:layout-component>
    <s:layout-component name="lhsContent">
        <jsp:include page="myaccount-nav.jsp"/>
    </s:layout-component>

    <s:layout-component name="rhsContent">
        <s:form beanclass="com.hk.web.action.core.user.MyAccountAction" var="accountAction">
            <s:errors/>
            <div>
                <h4 class="strikeline"> Email Subscriptions</h4>
                <div style="margin-top: 15px"></div>
                <c:choose>
                <c:when test="${!accountAction.getUser().isSubscribedForNotify()}">
                <div style="float: right; font-size: 0.7em;">
                    <s:submit name="subscribeForNotifications" value="Update" disabled="true" class="button_orange"/>
                </div>
                </c:when>
                <c:otherwise>
                    <div style="float: right; font-size: 0.7em;">
                        <s:submit name="" value="Update" class="button_orange"/>
                    </div>
                </c:otherwise>
                </c:choose>
            </div>
        </s:form>
    </s:layout-component>
</s:layout-render>

<script type="text/javascript">
    window.onload = function() {
        document.getElementById('myAccountLink').style.fontWeight = "bold";
        document.getElementById('birthDate').labelify({labelledClass: 'input_tip'});
    };

    $(document).ready(function() {
        $('.error').hide();
        $('#userName').change(_validateUserName);

        $(document).click(function() {
            $('.error').fadeOut();
        });

        function _validateUserName() {
            if ($('#userName').val().length > 80) {
                $('.error').fadeIn();
                //        var position = $('#userName').position();
                //        var width = $('#userName').innerWidth();
                //        var height = $('#userName').innerHeight();
                //        $('.errorMessage .line1').html("Name cannot be greater than 80 characters!");
                //        $('.errorMessage').css("top", position.top);
                //        $('.errorMessage').css("height",height);
                //        $('.errorMessage').css("left", position.left + width);
                //        $('.errorMessage').css("left", position.left + width);
                //        $('.errorMessage').animate({
                //          opacity: 1
                //        }, 500);
            }
        }
    });
</script>
<style type="text/css">
    .row {
        margin-top: 0;
        float: left;
        margin-left: 0;
        padding-top: 2px;
        padding-left: 26px;
    }

    .rowLabel {
        float: left;
        padding-right: 5px;
        padding-left: 5px;
        width: 100px;
        height: 24px;
        margin-top: 5px;
        font-weight: bold;
    }

    .rowText {
        float: left;
        border-width: 0;
        padding-top: 0;
        padding-bottom: 0;
        margin-left: 20px;
        font: inherit;
    }

    .error {
        float: left;
        color: black;
        width: 150px;
        font-size: 0.8em;
        margin: 5px 5px 5px 10px;
    }

    .date_input {
        width: 100px;
        float: left;
        border-width: 0;
        padding-top: 0;
        padding-bottom: 0;
        margin-left: 20px;
    }
</style>