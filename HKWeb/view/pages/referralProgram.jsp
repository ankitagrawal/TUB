<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.referral.ReferralManagerAction" var="couponBean"/>
<s:layout-render name="/layouts/default.jsp" pageTitle="Referral Program">

  <s:layout-component name="htmlHead">
    <script type="text/javascript" src="http://www.plaxo.com/css/m/js/util.js"></script>
    <script type="text/javascript" src="http://www.plaxo.com/css/m/js/basic.js"></script>
    <script type="text/javascript" src="http://www.plaxo.com/css/m/js/abc_launcher.js"></script>
    <script type="text/javascript">
      function onABCommComplete(data) {
        var emails = [];
        if (data) {
          for (var i = 0; i < data.length; i++) {
            emails.push(data[i][1]);
          }
        }
        var oldValue = document.getElementById('emails').value;
        while (oldValue.charAt(oldValue.length - 1) == ' ') {
          oldValue = oldValue.substring(0, oldValue.length - 1);
        }
        if (oldValue != "" && oldValue.charAt(oldValue.length - 1) != ',') {
          oldValue += ", ";
        }
        document.getElementById('emails').value = oldValue + emails.join(', ');
      }

      $(document).ready(function() {
        $('#referralTerms').jqm({trigger: '.referralTerms', ajax: '@href'});
      });
    </script>
    <style type="text/css">
      ul {
        list-style: none;
      }
    </style>
  </s:layout-component>
  <s:layout-component name="heading">Referral Program!</s:layout-component>
  <s:layout-component name="lhsContent">
    <jsp:include page="myaccount-nav.jsp"/>
  </s:layout-component>
  <s:layout-component name="rhsContent">
    <s:layout-component name="modal">
      <div class="jqmWindow" id="referralTerms"></div>
    </s:layout-component>

    <p style="text-align:left;">
      Referral Program |
      <s:link beanclass="com.hk.web.action.core.referral.ReferralManagerAction">My Referrals</s:link> |
      
      <s:link beanclass="com.hk.web.action.core.referral.ReferralTermsAction" class="referralTerms">T&C</s:link>
    </p>
    <br/>

    <h4>How it works?</h4>

    <div class="">
      <p class="lrg">
        <strong>Simple!!
          <br></strong> When you refer, <strong>you get store credit of Rs. 100</strong>* <i>plus</i> <strong>your
        friend gets an additional 5% off </strong>** on the first purchase
      </p>
    </div>

    <br>


    <h4>How to refer?</h4>
    <ul class="instr">
      <li>
        <strong>1. Facebook : </strong> Publish your referral link on Facebook wall.
          <br/>
        <table cellpadding="0" cellspacing="0">

          <tr>
            <td></td>
            <td><!-- AddThis Button BEGIN -->
              <div class="addthis_toolbox addthis_default_style addthis_32x32_style"
                   addThis:url="${pageContext.request.contextPath}/action/invite/${couponBean.principal.userHash}"
                   addTHis:title="HealthKart referral program"
                   addTHis:description="Get Rs. 100 and Gift Additional 5% off">
                <a class="addthis_button_preferred_1">
                  &nbsp;&nbsp;${pageContext.request.contextPath}/action/invite/${couponBean.principal.userHash}</a>
              </div>
              <script type="text/javascript" src="http://s7.addthis.com/js/250/addthis_widget.js#pubid=xa-4df7053a23664e11"></script>
              <!-- AddThis Button END --></td>
          </tr>

        </table>
      </li>

      <li>
          <br/>
        <strong>2. Email : </strong>
        <table cellpadding="0" cellspacing="0">
          <tr>
            <td><%--<img src="${pageContext.request.contextPath}/images/email.jpg" width="" alt="">--%></td>
            <td>
              <s:form beanclass="com.hk.web.action.core.referral.ReferralProgramAction">
                <fieldset class="top_label" style="border:0px;">
                  <ul style="margin-bottom:0; padding-left:10px">
                    <s:hidden name="senderName"/>
                    <s:hidden name="senderEmail"/>
                    <li>
                      <label><strong>Your friends' email addresses</strong></label><br />
                        <span >(Separate emails by a comma. Maximum 50 allowed at a time.)</span>

                      <div class="text_input_grouped">
                        <s:textarea rows="" cols="50" name="recipientEmails" id="emails"></s:textarea>
                        <a href="referralProgram.jsp#" onclick="showPlaxoABChooser('recipient_list', '${pageContext.request.contextPath}/pages/plaxoAddressSelector.jsp'); return false;"><img src="http://www.plaxo.com/images/abc/buttons/add_button.gif" alt="Add from my address book"/></a>
                      </div>

                       <span style="display:none;">
                        <textarea rows="" cols="" name="notImportant" id="recipient_list"></textarea>
                      </span>

                    </li>

                  </ul>
                </fieldset>
                <div style="padding-left:100px">
                  <div class="buttons">
                    <s:submit name="send" value="Send Invites"/>
                  </div>
                </div>
                <br>
                <br>
              </s:form></td>
          </tr>
        </table>
      </li>

      <c:if test="${hk:isNotBlank(couponBean.coupon.code)}">
      <li>
        <strong>3. Unique Referral Code : </strong> Share your unique referral code with friends through Email/SMS.
        <table cellpadding="0" cellspacing="0">
          <tr>
            <td>&nbsp;</td>
            <td>
              <div class="box_alert" style="font-family: monospace;">
                  ${couponBean.coupon.code}
              </div>
            </td>
          </tr>
        </table>
      </li>
      </c:if>
    </ul>

    <div>
      <p class="dim">
        * A referral is only counted when your friend places an order using your referral coupon code<br/>
        ** On purchases of Rs 500 or more
      </p>
    </div>
  </s:layout-component>

</s:layout-render>

<script type="text/javascript">
  window.onload = function() {
    document.getElementById('rpLink').style.fontWeight = "bold";
  };
</script>