<!DOCTYPE html>
<html>
<head>
	<title>Health Kart | Online health Store</title>
	<%@ include file='header.jsp' %>
</head>
<body>
<div data-role="page" id=trackOrder class="type-home">
		<div data-role=header>
			<%@ include file='menuHeader.jsp'%>
			<%@ include file='menuNavBtn.jsp'%>
		</div>
		<div data-role="content" style='height: auto'>

			<form id=trackOrderForm>
				<div
					style='margin: 10px; padding: 10px; border: 1px solid white; box-shadow: 4px 4px 8px #888; -webkit-box-shadow: 4px 4px 8px #888'>
					<label for='login'>Tracking Number</label> <input type=text
						id=trackOrderId style='background-color: white'
						placeholder='Enter Here' />
					<center>
						<input type=submit data-inline=true value='Track Now' />
					</center>
				</div>
			</form>
			<div id='trackOrderResult'></div>
			<%@ include file='menuFooter.jsp'%>
		</div>

		<script>
		$('#trackOrder').bind('pageshow',function(){
			$('#trackOrderForm').bind('submit',function(e){
				e.preventDefault();
				$.ajax({
					url: wSURL + 'mOrder/' + $('#trackOrderId').val() +'/track/',
					dataType: 'json',
					async:false,
					success : function(data){
						if(hasErr(data))
						{
				   		    popUpMob.show(data.message);
						}
						else
						{
						    var resultHtml= '</h3> Status of your order '+ $('#trackOrderId').val()+' created on '+data.data.date+' is: '+data.data.orderstatus+'</h3><br/>';
							popUpMob.showWithTitle("Order status",resultHtml);
			
						}
					},
					error: function(){
						popUpMob.showWithTitle("Status",'Invalid Order Id');
					}
				});
			
				return false;
			});
		});
		</script>
</div>
</body>
</html>