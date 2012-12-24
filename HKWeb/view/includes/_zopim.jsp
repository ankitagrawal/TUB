<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!-- Start of Zopim Live Chat Script -->
<script type="text/javascript">
window.$zopim||(function(d,s){var z=$zopim=function(c){z._.push(c)},$=
z.s=d.createElement(s),e=d.getElementsByTagName(s)[0];z.set=function(o
){z.set._.push(o)};$.setAttribute('charset','utf-8');$.async=!0;z.set.
_=[];$.src=('https:'==d.location.protocol?'https://ssl':'http://cdn')+
'.zopim.com/?ZbNl3gqrfxK7LUlA8ojWSwHbfHfSmeg9';$.type='text/java'+s;z.
t=+new Date;z._=[];e.parentNode.insertBefore($,e)})(document,'script')
</script>
<!-- End of Zopim Live Chat Script -->
<div class="firstName" style="display:none"><shiro:principal property="firstName"/></div>
<shiro:hasRole name="<%=RoleConstants.TEMP_USER%>">
  <div class="email" style="display:none">Please enter your email</div>
</shiro:hasRole>
<shiro:lacksRole name="<%=RoleConstants.TEMP_USER%>">
  <div class="email" style="display:none"><shiro:principal property="email"/></div>
</shiro:lacksRole>

<div class="id" style="display:none"><shiro:principal property="id"/></div>

<script>
  $zopim(function() {
    $zopim.livechat.setName("");
    $zopim.livechat.setName($('.firstName').html() + " [" + $('.id').html() + "]");
    $zopim.livechat.setEmail("");
    $zopim.livechat.setEmail($('.email').html());
  });
</script>

<script id="_webengage_script_tag" type="text/javascript">
  window.webengageWidgetInit = window.webengageWidgetInit || function(){
    webengage.init({
      licenseCode:"~2024c219"
    }).onReady(function(){
      webengage.render();
    });
  };

  (function(d){
    var _we = d.createElement('script');
    _we.type = 'text/javascript';
    _we.async = true;
    _we.src = (d.location.protocol == 'https:' ? "//ssl.widgets.webengage.com" : "//cdn.widgets.webengage.com") + "/js/widget/webengage-min-v-3.0.js";
    var _sNode = d.getElementById('_webengage_script_tag');
    _sNode.parentNode.insertBefore(_we, _sNode);
  })(document);
</script>
