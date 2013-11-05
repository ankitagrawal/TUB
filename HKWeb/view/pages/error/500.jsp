<%@include file="/includes/_taglibInclude.jsp" %>
<%@ page isErrorPage="true" %>
<s:layout-render name="/layouts/errorTemplate.jsp" pageTitle="Error 500 | Healthkart" errorCode="500">
    <s:layout-component name="content">
        <div class="container clearfix">
            <div class="err-500-cntnr mrgn-bt-70">
                <div class="left-500-msg">
                    <div class="msg1">error</div>
                    <div class="msg2">500</div>
                </div>
                <div class="span7 offset1 right-500-msg mrgn-t-30">
                    <p>Something is Broken.</p>
                    <p>Sorry :(</p>
                </div>
            </div>
        </div>
    </s:layout-component>
</s:layout-render>