<!DOCTYPE html>
<html>
<head>
	<title>Health Kart | Online health Store</title>
	<%@ include file='header.jsp' %>	
</head>
<body>
<div data-role="page" id=cart class="type-home">
<div data-role=header>
	<%@ include file='menuHeader.jsp'%>
	<%@ include file='menuNavBtnSrch.jsp'%>
</div>
	<div data-role="content" style='background-color:white'>
	
		
	<div style='clear:both;padding:4px;background-color:#ddd;text-align:center;margin-bottom:6px;font-weight:bold'>
		Product in Cart
		</div>
		<ul data-role=listview style='padding-top:4px;padding-bottom:20px' id='cartList'>
		
		</ul>
		<a href='javascript:void(0)' id='checkout' data-role=button>CheckOut</a>
		
<%@ include file='menuFooter.jsp' %>	
</div>

<script type='text/template' id='cart-template'>

			
				<table width='100%'>
					<tr>
					<td class='image-container'>
						<img src='{print(imageUrl)}}'/>
					</td>
					<td class='text-container'>
						<h3 style='white-space:normal'>{{print(name)}}</h3>
						<p>
							<strike>Rs {{print(markedPrice)}}</strike>
							<span class='ofrPrc'>Our Price Rs {{print(hkPrice)}}</span>
							<span class='svPrcnt'>Rs {{print(discountOnHkPrice)}} off</span>
							
						</p>
						<!--span class='ad2Crt'>Add To cart</span-->
					</td>
					</tr>
				</table>
				<table style='width:90%;margin:0px auto'>
				<tr>
					<td>
						<table style='margin:0px auto'><tr>
						<td class='su2Crt' data-url='cartLineItemId={{print(cartLineItemId)}}&qty={{print(qty - 1)}}'><img src='${httpPath}images/sub.png' alt='Minus'/></td><td ><input type='text' value='{{print(qty)}}'  style='text-align:center;width:30px;margin:0px auto; border:none' readonly /></td>
						<td class='pl2Crt' data-url='cartLineItemId={{print(cartLineItemId)}}&qty={{print(qty + 1)}}'><img src='${httpPath}images/plus.png' alt='Plus'/></td>
						</tr></table>
					</td>
					<td>
						<a href='javascript:void(0)' class='rm2Crt' data-url='cartLineItemId={{print(cartLineItemId)}}'><img src='${httpPath}images/remove.png'/>
					</td>
				</tr>
				</table>
					
			
</script>
<script>

$('#cart').bind('pageshow',function(){
		jQuery.support.cors = true;
		
	_.templateSettings = {
			evaluate : /\{\{(.+?)\}\}/g
		};
		
	Backbone.emulateJSON = true;
var x = $.mobile.path.parseUrl(location.href);
var queryString = x.search;
/*****Variant Backnbone**S**********/
	var ProductVariantModel = Backbone.Model.extend({
		initialize : function(){
			this.render();
		},
		render : function(){
			var prVaVi = new ProductVariantView({model:this});
			$('#cartList').append(prVaVi.render().el);
		},
		unRender : function(){
		console.log('done');
			this.destroy();
		}
	});
	var ProductVariantCollection = Backbone.Collection.extend({
		model : ProductVariantModel,
		initialize : function(){
			this.on('reset',this.clearView,this);
			this.on('add',this.updateUI,this);
			this.clearView();
		},
		clearView : function(){
			$('#cartList').html('');
		},
		updateUI : function(){
			$('#cartList').listview();
			$('#cartList').listview('refresh');
		}
	});
		var prVaCo = new ProductVariantCollection();
	var ProductVariantView = Backbone.View.extend({
		tagName : 'li',
		template : _.template($('#cart-template').html()),
		initialize : function(){
			_.bindAll(this,'render');
			this.on('unRender',this.model.unRender,this);
	//		this.collection = prVaCo;
		},
		render : function(){
			$(this.el).empty();
			$(this.el).html(this.template(this.model.toJSON()));
			return this;
		},
		unRender : function(){
			this.remove();
		},
		events : {
			'click .rm2Crt' : 'removeItem',
			'click .su2Crt' : 'removeItem',
			'click .pl2Crt' : 'removeItem',
		},
		removeItem : function(e){
			var ele = e.currentTarget;
			var query=$(ele).attr('data-url');
			//console.log(this.model.get('id'));
			var viewObj = this;
			$.ajax({
				url : wSURL + 'mRemoveItem/removeItemfromCart',
				data : query,
				type : 'get',
				success: function(data){
					if(hasErr(data))
					{
						alert('Request Failed');
					}
					else
					{
						if(prVaCo.reset()){
						alert('Successfully Removed');
						
						prVaCo.add(data.data);
						//viewObj.unRender();
						}
					}
				}
			});
		}
	});
/*****Variant Backnbone**E**********/
loadingPop('s','');

	
	$.ajax({
			url : wSURL+'mCart/viewCart',
			//url: 'c.json',
		dataType: 'json',
		success : function(response){
			if(hasErr(response))
			{
				loadingPop('h');
				alert(getErr(response.message));
			}
			else
			{
	
				if(prVaCo.add(response.data))
				{
					$('#cartList').listview();
				}
				
				loadingPop('h');
			}
		},
		error: function(){
			alert('Request failed');
			loadingPop('h');
		}
		
		});
		
		$('#checkout').on('click',function(e){
	//e.preventDefault();

			//var requestURL = $(this).attr('data-url');
			
			$.ajax({
				url: 'c.json',
			//	data: requestURL,
				dataType: 'json',
				//type: 'POST',
				success: function(response){
					if(hasErr(response))
					{
						loadingPop('h');
						alert(getErr(response.message));
					}
					else
					{
						if(response.data.type == null)
						{
							$.mobile.changePage('login-signup.jsp?target=address');
						}
						else if(response.data.type == 'Logged In')
						{
							$.mobile.changePage('address.jsp');
						}
					}
				},
				error: function(e)
				{
					alert(e);
				}
			});
		

		});
});

</script>
</div>

</body>
</html>
