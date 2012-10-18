<!DOCTYPE html>
<html>
<head>
		<%@ include file='header.jsp' %>

</head>
<body>
<div data-role="page" id=orderSummary class="type-home">
<div data-role=header>
	<%@ include file='menuHeader.jsp'%>
	<%@ include file='menuNavBtn.jsp'%>
</div>
	<div data-role="content" style='height:auto'>

				<div style='background-color:#319aff;padding:10px;margin-bottom:10px;color:white;font-size:16px'>
				<center>Order Summary</center>
				</div>
			</div>
<!-- end left -->


<!-- start right pannel -->
<div id='defaultViewContent' class=viewContent>

<script id="summary-view-template" type='text/template'>
  <div style='padding:10px'>
   <div style='border-bottom:none;border-right:none;margin-bottom:15px;padding:5px;box-shadow:2px 2px 8px #888,-2px -2px 8px #888;-webkit-box-shadow:2px 2px 8px #888,-2px -2px 8px #888'>
   {{ if(data.addressSelected!=null){ }}
   <h2>Delivery Address</h2>
    <div class='addressText'><b>{{print(data.addressSelected.name)}}</b><br>
	{{print(data.addressSelected.line1)}}<br>
	{{print(data.addressSelected.line2)}}<br>
	{{print(data.addressSelected.city)}}, {{print(data.addressSelected.state)}}<br>
	PIN Code: {{print(data.addressSelected.pin)}}<br>
	Ph: {{print(data.addressSelected.phone)}}<br></div>
   {{ } }}
   </div>
<div style='border-bottom:none;border-right:none;margin-bottom:15px;padding:5px;box-shadow:2px 2px 8px #888,-2px -2px 8px #888;-webkit-box-shadow:2px 2px 8px #888,-2px -2px 8px #888'>
   <h2>You selected</h2>
   <table style='width:100%;text-align:center;font-size:1.2em'>
   <tr style='background:url(images/navbar_bg.png);color:white;webkit-text-shadow:none;text-shadow:none;'><th>Product</th><th>Qty</th><th>Amt(Rs.)</th></tr>
   {{ for(var i=0;i<data.cartItems.length;i++) { }}
   <tr style='background-color:#eee'><td>{{print(data.cartItems[i].name)}}</td><td>{{print(data.cartItems[i].qty)}}</td><td>{{print(data.cartItems[i].hkPrice)}}</td></tr>
    {{ } }}
	</table>
	{{ if(data.pricingDto!=null){ }}
	   <h2 style='color:#888'>Shipping: Rs.{{print(data.pricingDto.shippingTotal)}}</h2>
	   <h3 style='color:#888'>You Saved: Rs.{{print(data.pricingDto.productsMrpSubTotal- data.pricingDto.productsHkSubTotal)}}</h3>
	   <h2 style='color:#F87500'>Total: Rs.{{print(data.pricingDto.productsHkSubTotal)}}</h2>

	{{ } }}

   </ul>

   </div>


</script>
  <div id="summary-address-block">
  </div>
   <form id="order_makeapayment" >
   <div style='border-bottom:none;border-right:none;padding:10px;;margin:10px;box-shadow:2px 2px 8px #888,-2px -2px 8px #888;-webkit-box-shadow:2px 2px 8px #888,-2px -2px 8px #888'>
   <h2>Instructions(If any)</h2>
   <textarea name="comment" id="orderSummary_comments"></textarea>
   <h2>COD Contact Name</h2>
   <textarea name="codContactName" id="orderSummary_name"></textarea>
   <h2>COD Contact Number</h2>
   <textarea name="codContactPhone" id="orderSummary_phone"></textarea>

   </div>
     <a href='javascript:void(0)' id="orderSummary_makepayment" style='width:95%;margin:0px auto;margin-bottom:10px' data-role=button>Place Order</a>
   </form>
  </div>
</div>
<!-- end right pannel -->
		<%@ include file='menuFooter.jsp' %>
</div>


<script>
$('#orderSummary').bind('pageshow',function(){

			 jQuery.support.cors = true;

	_.templateSettings = {
			evaluate : /\{\{(.+?)\}\}/g
		};

    	Backbone.emulateJSON = true;

		var OrderSummary = Backbone.Model.extend({
			initialize: function(){
				this.render();
			},
			render: function(){
				var adVi = new OrderSummaryView({model:this});
				$("#summary-address-block").append(adVi.render().el);

			}
		});
		var OrderSummaryCollection = Backbone.Collection.extend({
			model: OrderSummary,
			initialize: function(){
				this.clearView();
				this.on('reset',this.clearView(),this);
			},
			clearView: function(){
				$("#summary-address-block").html('');
			}
		});
		var OrderSummaryView = Backbone.View.extend({
			tagName: 'ul',
			template: _.template($('#summary-view-template').html()),
			//className: 'savedAddressView',
			initialize: function(){
				_.bindAll(this,'render');
			},

			render: function(){
				$(this.el).empty();
				$(this.el).html(this.template(this.model.toJSON()));
				return this;
			}

		});

		var addrCol = new OrderSummaryCollection();


		$.ajax({
		url : wSURL+'mOrderSummary/orderSummary',
		dataType: 'json',
		async:false,
		success : function(response){
			if(hasErr(response))
			{
				loadingPop('h');
				popUpMob.show(getErr(response.message));
			}
			else
			{
		    	if(addrCol.add(response))
				{
					$('#summary-address-block').listview();
				}

				loadingPop('h');
			}
		},
		error: function(){
			popUpMob.show('Request failed');
			loadingPop('h');
		}

		});



   $('#orderSummary_makepayment').click(function(){
         var txt = $("#orderSummary_comments").val();
		$.ajax({
		url: wSURL+'mPayment/payment/'+"?comment="+txt,
		type: 'post',
		dataType: 'json',
		data:$("#order_makeapayment").serialize(),
		async:false,
		success: function(data)
		{
			if(hasErr(data))
			{
				popUpMob.show(data.message);
				if((data.message).toLowerCase()=='cod is not available for this location'){
                                setTimeout(function(){location.href="${httpPath}/address.jsp"},800);
								}

			}
			else
			{
			    setTimeout(function(){location.href="${httpPath}/orderSuccess"+'.jsp?orderId='+data.data.gatewayOrderId},500);
			}
		},

		error : function(e) {
			popUpMob.show("Request Failed");
		}
		});

		return false;
	});



});

</script>
</div>
</body>
</html>