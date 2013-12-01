<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultG.jsp">
  <s:layout-component name="heading">Writers' Payment Calculator</s:layout-component>

  <s:layout-component name="htmlHead">
    <script type="text/javascript">
      $(document).ready(function() {

        $('#calculate').click(function() {
          var words = $('#words').val();

          if (!isNumber(words)) {
            $('#salary').html('<span style="color:red;">Please enter a valid number (no comma or space)</span>');
            return false;
          }

          var salary = words <= 125000 ? (0.45*words-words*words/1000000) : (40625+(words-125000)*.20);
          $('#salary').html(salary);
        });

      });

      function isNumber(n) {
        return !isNaN(parseFloat(n)) && isFinite(n);
      }
    </script>
  </s:layout-component>

  <s:layout-component name="rhsContent">
    <p align="center">
      Please enter number of words<br/>
      <input type="text" value="1000" id="words"/>
    </p>

    <p align="center">
      <button id="calculate">Calculate Payment</button>
    </p>

    <p align="center" style="font-size: 2em;" id="salary">

    </p>
  </s:layout-component>
</s:layout-render>
