<%@ page import="org.joda.time.DateTime" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.hk.taglibs.Functions" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<style type="text/css">
    #sendCouponLink {
        text-decoration: none;
        border-bottom: 0;
    }

    #sendCouponLink :hover {
        text-decoration: none;
        border-bottom: 0;
    }
</style>

<div style="margin-left: auto; margin-right: auto; width:960px;">
<%--<s:link beanclass="com.hk.web.action.core.user.RequestCallbackAction" id="sendCouponLink">--%>
    <%--<img src="${pageContext.request.contextPath}/images/banners/sports_strip_banner_fitness.jpg"--%>
         <%--alt="Beauty Offers">--%>
    <%--</s:link>--%>

    <a class="fitnessOrder" href="#" style="cursor: pointer;">
        <img src="${pageContext.request.contextPath}/images/banners/sports_strip_banner_fitness.jpg"
             alt="Beauty Offers">
    </a>

    <script type="text/javascript">
        var params = {};
        params['srcUrl'] = document.location.href;
        params['topLevelCategory'] = $('#topCategoryContainer').html();
        $('#sendCouponLink').attr('href', $('#sendCouponLink').attr('href') + '?' + $.param(params));

        $('#discountCouponModal').jqm({trigger: '#sendCouponLink', ajax: '@href'});

    </script>

    <script type="text/javascript">
        $(".fitnessOrder").live('click', function () {
            $('html, body').animate({scrollTop: $("#bulkOrderModal").offset().top - 50}, 1000);
            $('#bulkOrderModal').jqm({trigger: '.fitnessOrder'});
            $("#bulkOrderModal").append($('<iframe id="raj_frame" class="bulkModalFrame" src=" https://docs.google.com/spreadsheet/viewform?formkey=dFFBeGVKUzR6dl9NTkJsVUNlQWRYZnc6MQ" width="760" height="760" frameborder="0" marginheight="0" marginwidth="0">Loading...</iframe>'));
        });
    </script>
</div>
