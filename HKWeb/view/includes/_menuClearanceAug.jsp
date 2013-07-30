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

<div class="banner"> <a href="#"><img src="${pageContext.request.contextPath}/images/clearance/cs-banner-blue.jpg" alt="Clearance Sale - upto 70% off" /></a></div>


<ul class="menu">
  <li><a href="#">Health<br />
    Nutrition</a></li>
  <li><a href="#">Health<br />
    Devices</a></li>
  <li><a href="#">Diabetes</a></li>
  <li><a href="#">Personal<br />
    Care</a></li>
  <li><a href="#">sports &amp;<br />
    fitness</a></li>
  <li><a href="#">Parenting</a></li>
  <li><a href="#">Beauty</a></li>
  <li><a href="#">Eye</a></li>
</ul>


<div class="cl"></div>

