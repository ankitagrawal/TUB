<%@ include file="/includes/_taglibInclude.jsp" %>
<s:layout-definition>
  <s:useActionBean beanclass="com.hk.web.action.nwb.NwbMenuAction" var="menuAction" event="pre"/>
  <div class="container clearfix">
      <div class="strip-404 mrgn-bt-10"></div>
      <div class="row pad-tb-30 menu-404">
          <span class="fnt-sz10"><a href="/">HOME PAGE</a></span>
          <c:forEach items="${menuAction.menuNodes}" var="menuNode">
                <span class="mrgn-l-10 fnt-sz10">
                    <a href="${menuNode.url}">${menuNode.name}</a>
                </span>
          </c:forEach>
      </div>
      <div class="strip-404 mrgn-bt-10"></div>
  </div>
</s:layout-definition>