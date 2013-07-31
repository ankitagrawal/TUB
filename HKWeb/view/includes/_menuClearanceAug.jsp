<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>

<script>
  var box1 = $('#box1');

  var highlights = new TimelineMax({
    paused: true
  });

  highlights.insert(TweenLite.set(box1, {
    css: { className: '+=glow' },
    immediateRender: false
  }), 1);
  highlights.insert(TweenLite.set(box1, {
    css: { className: '-=glow' },
    immediateRender: false
  }), 2);

  highlights.play();</script>

<div class="banner"> <a href="${pageContext.request.contextPath}/pages/campaign/clearance/august/clearance-aug.jsp">
  <img src="${pageContext.request.contextPath}/images/clearance/cs-banner-blue.jpg" alt="Clearance Sale - upto 70% off" /></a></div>


<ul class="menu">
  <li><a href="${pageContext.request.contextPath}/pages/campaign/clearance/august/hnCS.jsp">Health<br />
    Nutrition</a></li>
  <li><a href="${pageContext.request.contextPath}/pages/campaign/clearance/august/devicesCS.jsp">Health<br />
    Devices</a></li>
  <li><a href="${pageContext.request.contextPath}/pages/campaign/clearance/august/diabetesCS.jsp">Diabetes</a></li>
  <li><a href="${pageContext.request.contextPath}/pages/campaign/clearance/august/careCS.jsp">Personal<br />
    Care</a></li>
  <li><a href="${pageContext.request.contextPath}/pages/campaign/clearance/august/fitnessCS.jsp">sports &amp;<br />
    fitness</a></li>
  <li><a href="${pageContext.request.contextPath}/pages/campaign/clearance/august/parentingCS.jsp">Parenting</a></li>
  <li><a href="${pageContext.request.contextPath}/pages/campaign/clearance/august/beautyCS.jsp">Beauty</a></li>
  <li><a href="${pageContext.request.contextPath}/pages/campaign/clearance/august/eyeCS.jsp">Eye</a></li>
</ul>


<div class="cl"></div>

