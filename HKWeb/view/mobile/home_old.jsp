<!DOCTYPE html>
<html>
<head>
	<title>Health Kart | Online health Store</title>
	<%@ include file='header.jsp' %>	
</head>
<body>
<div data-role="page" id=home class="type-home">
<div data-role=header data-position=fixed>
	<%@ include file='menuHeader.jsp'%>
</div>
	<div data-role="content" style='background-color:white'>
		
		<div class='ui-grid-a grid' data-theme=b>
			<div class=ui-block-a><a href='category.jsp'><div class=center><img src='images/shop.png'/><div>Shop</div></div></a></div>
			<div class=ui-block-b><a href='offer.html'><div class=center><img src='images/offer.png'/><div>Offers</div></div></a></div>

			<div class=ui-block-a><a href='trackOrder.html'><div class=center><img src='images/orderTracking.png'/><div>Track Order</div></div></a></div>
			<div class=ui-block-b><a href='login-signup.html'><div class=center><img src='images/login.png'/><div>Login</div></div></a></div>

			

		</div>
		
		
		
		



	</div>
<script>
$(window).bind('orientationchange',function(){
	var activePage = $.mobile.activePage.attr('id');
	console.log(activePage);
	if(activePage=='home')
	{
		setTimeout(function(){
			var hei = window.innerHeight -120;
			if(hei>280){
				var bloHei = eval(hei)/2 +'px';
				$('#home .grid').css({'height':hei,'width':'98%','margin':'0px auto'});
				$('#home .grid .ui-block-a,#home .grid .ui-block-b').css({'height':bloHei,'lineHeight':bloHei}).find('div.center').css({'verticalAlign':'middle','lineHeight':'14px'});
			}
			else
			{
				var hei = 'auto';
				var bloHei = '120px';
				$('#home .grid').css({'height':hei,'width':'98%','margin':'0px auto'});
				$('#home .grid .ui-block-a,#home .grid .ui-block-b').css({'height':bloHei,'lineHeight':bloHei}).find('div.center').css({'verticalAlign':'middle','lineHeight':'14px'});
			}
		},400);
	}
		
	
});
$('#home').bind('pageshow',function(){
	$(window).trigger('orientationchange');
});
</script>
	<style>
	#home .center{
		border:1px solid white;
		padding:5px;
		background:#eee;
		width:110px;
		height:100px;
		
		border-radius:5px;
		-webkit-border-radius:5px;
	}
	</style>
	
</div>
</body>
</html>
