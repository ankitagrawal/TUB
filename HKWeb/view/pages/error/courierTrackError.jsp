<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/default.jsp" pageTitle="Courier Status not found">
    <s:layout-component name="heading">Courier status not found.</s:layout-component>
    <s:layout-component name="htmlHead">

        <style type="text/css">

            .lhsContent {
                float: left;
                width: 100%;
                background: #FAFCFE;
            }
        </style>
    </s:layout-component>
    <s:layout-component name="lhsContent">
        <div>
            <p class="imp_note">Sorry,some problem occurred in finding the status.</p>

            <p>Please call our customer support <b>0124-4551616</b> for assistance.</p>
        </div>
    </s:layout-component>

</s:layout-render>
