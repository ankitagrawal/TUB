<!DOCTYPE html>
<html>
<head>
	<title>Health Kart | Online health Store</title>
	<%@ include file='header.jsp' %>	
</head>
<body>
<div data-role="page" id=offers class="type-home">
<div data-role=header>
	<%@ include file='menuHeader.jsp'%>
	<%@ include file='menuNavBtnSrch.jsp'%>
</div>
	<div data-role="content" style='background-color:white'>
	
		<!--div style='clear:both;padding-top:6px;background-color:#ddd;font-weight:bold;font-size:0.9em'>
		Top Products
		</div>
		<div id='product-scroll'>
		</div-->
		
		<div style='clear:both;padding:1px 0px'></div>
		<div id='productTitle'style='clear:both;padding:4px;background-color:#ddd;text-align:center;margin-bottom:6px;font-weight:bold'>
			Offers
		</div>
		<div style='padding-top:4px' id='offerList'>
		</div>
		<br>
		<br>
		
		
		<%@ include file='menuFooter.jsp' %>	



	</div>

		<script type='text/template' id='offers-list-template'>
		
		
			{{for(var i =0;i<data.length;i++){ }}
		                         <br/>	
			
					<p align=center>	<img width="90%" alt='No Image Available' src='{{print(data[i])}}'/></p>
			{{ } }}
		
		
		</script>
		
<script>
$('#offers').bind('pageshow',function(){
		var urlEval = new URLEval();
		//alert(urlEval.getURLFromHash(location.href));
var x = $.mobile.path.parseUrl(urlEval.getURLFromHash(location.href));
		var queryString = x.search;
		
		jQuery.support.cors = true;
		
	_.templateSettings = {
			evaluate : /\{\{(.+?)\}\}/g
		};
		
	Backbone.emulateJSON = true;
			
		
		/**Backbone code for product list*S*/
		var ProductModel = Backbone.Model.extend({
			initialize : function(){
				this.render();
			},
			render : function(){
				var prVi = new ProductView({model : this});
				$('#offerList').append(prVi.render().el);
			}
		});
		
		var ProductCollection = Backbone.Collection.extend({
			model : ProductModel,
			initialize : function(){
				this.on('reset',this.clearView,this);
				this.clearView();
			},
			clearView : function(){
				//$('#productTitle').html(getURLParameterValue(queryString,'secondaryCategory'));
				$('#offerList').html('');
			}
		});
		
		var ProductView = Backbone.View.extend({
			tagName : 'ul',
			initialize : function(){
				_.bindAll(this,'render');
				
			},
			template : _.template($('#offers-list-template').html()),
			render : function(){
				$(this.el).empty();
				$(this.el).html(this.template(this.model.toJSON()));
				return this;
			}
		});
		jQuery.support.cors = true;
		loadingPop('s','');
		var prCo = new ProductCollection();
		prCo.reset();
		var temp = queryString;
		var URL;
		temp = temp.replace('?','');
		if(temp.indexOf('query')==0)
		{
			URL = wSURL+'mSearch/search'+queryString;
		}
		else
		{
			URL = wSURL + 'mCatalog/catalog'+queryString;
			//  URL = 'c.json'+queryString;
		}
		$.ajax({
			url : wSURL+'mOffers/offers',
		dataType: 'json',
		success : function(response){
			if(hasErr(response))
			{
				loadingPop('h');
				alert(getErr(response.message));
			}
			else
			{
				
				if(prCo.add(response))
				{
				  $('#offerList ul').listview();
				}
				
				loadingPop('h');
			}
		},
		error: function(){
			alert('Request failed');
			loadingPop('h');
		}
		
		});
		/**Backbone code for product list*E*/
});

</script>


	
</div>

</body>
</html>
