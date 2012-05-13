<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory" %>
<%@ page import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/default.jsp">
  <s:layout-component name="htmlHead">
    <script type="text/javascript" src="http://www.plaxo.com/css/m/js/util.js"></script>
    <script type="text/javascript" src="http://www.plaxo.com/css/m/js/basic.js"></script>
    <script type="text/javascript" src="http://www.plaxo.com/css/m/js/abc_launcher.js"></script>
    <script type="text/javascript">

      function onABCommComplete(data) {
        var emails = [];
        if (data) {for (var i = 0; i < data.length; i++) {emails.push(data[i][1]);}}
        var oldValue = document.getElementById('emails').value;
        while (oldValue.charAt(oldValue.length - 1) == ' ') {oldValue = oldValue.substring(0, oldValue.length - 1);}
        if (oldValue != "" && oldValue.charAt(oldValue.length - 1) != ',') {oldValue += ", ";}
        document.getElementById('emails').value = oldValue + emails.join(', ');
      }

      $(document).ready(function() {
        $('#previewLink').click(function() {
          $('#senderNamePreview').val($('#senderName').val());
          $(this).parents('form').submit();
          return false;
        });
      });
    </script>
  </s:layout-component>
  
  <s:layout-component name="heading">Referral Email</s:layout-component>

  <s:layout-component name="lhsContent">
    <p class="lrg em">
      You can recommend HealthKart to your friends by sending this referral email. You can add a custom message to this
      email.<br/>
      <s:form beanclass="com.hk.web.action.core.email.ReferralEmailPreviewAction" target="_blank">
        <s:hidden name="senderName" id="senderNamePreview"/>
        <a href="#" id="previewLink">Click here</a> to preview how the email will look.
      </s:form>
    </p>

    <s:form beanclass="com.hk.web.action.core.email.ReferralEmailAction">

      <fieldset class="top_label">
        <ul>
          <li><label>Your name</label>
            <s:text name="senderName" id="senderName"/> <span class="gry">(required)</span>
            <s:hidden name="senderEmail"/>
          </li>
          <li>
            <label>Your friends' email addresses <span class="gry">(comma separated like friend1@yahoo.com, friend2@gmail.com):</span>
              (max 50 at a time)</label>

            <div class="text_input_grouped">
              <s:textarea rows="" cols="" name="recipientEmails" id="emails"></s:textarea>
              <a href="referralEmail.jsp#" onclick="showPlaxoABChooser('recipient_list', '${pageContext.request.contextPath}/pages/plaxoAddressSelector.jsp'); return false;"><img src="http://www.plaxo.com/images/abc/buttons/add_button.gif" alt="Add from my address book"/></a>
            </div>
            <span style="display:none;">
              <textarea rows="" cols="" name="notImportant" id="recipient_list"></textarea>
            </span>
          </li>
          <li><label>Your message (optional)</label><s:textarea name="customText"/></li>
        </ul>
      </fieldset>

      <div class="">
        <s:submit name="send" value="Send Invites"/>
      </div>
    </s:form>

    <p class="gry"><strong>Note</strong>: We do not spam. These addresses will only be used to send invites to your
      friends.</p>

  </s:layout-component>

</s:layout-render>
