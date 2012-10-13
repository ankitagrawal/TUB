<!DOCTYPE html>
<html>
<head>
	<title>Health Kart | Online health Store</title>
	<%@ include file='header.jsp' %>	
	
	
</head>
<body>
<div data-role="page" id=trackOrder class="type-home">
<div data-role=header >
<%@ include file='menuHeader.jsp'%>
	<%@ include file='menuNavBtn.jsp'%>
</div>
	<div data-role="content" style='height:auto'>
	
		<div style='text-align:center'>
		Tracking Console
		</div>
		<form id=trackOrderForm action='contact.html'>
		<div style='margin:10px;padding:10px;border:1px solid white;box-shadow:4px 4px 8px #888;-webkit-box-shadow:4px 4px 8px #888'>
		<label for='login'>Tracking Number</label>
		<input type=text  id=trackOrderId style='background-color:white'placeholder='Enter Here'/>
			<center><input type=submit data-inline=true value='Track Now'/></center>
			</div>
		</form>
		<div id='trackOrderResult'>
		</div>
	
	
		<%@ include file='menuFooter.jsp' %>	
	



	

</div>

<script>

$('#trackOrder').bind('pageshow',function(){
$('#trackOrderForm').bind('submit',function(e){
	e.preventDefault();
	$.ajax({
		url: wSURL + 'mOrder/' + $('#trackOrderId').val() +'/track/',
		dataType: 'json',
		success : function(data){
			if(hasErr(data))
			{
					alert('Request failed');
			}
			else
			{
				$('#trackOrderResult').html('<h3>'+data.data+'</h3>');
			}
		}
	});
	
	return false;
});
});
</script>
</div>
</body>
</html>
