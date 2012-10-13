<!DOCTYPE html>
<html>
<head>
	<title>Health Kart | Online health Store</title>
	<%@ include file='header.jsp' %>	
</head>
<body>
<div data-role="page" id=productDetails class="type-home">
<div data-role=header>
	<%@ include file='menuHeader.jsp'%>
	<%@ include file='menuNavBtnSrch.jsp'%>
</div>
	<div data-role="content" style='background-color:white'>
	
		<div style='clear:both;padding-top:6px;background-color:#ddd;font-weight:bold;text-align:center'>
		Product Detail
		</div>
		
		<table width=100% style='font-size:12px'id='productDetailMain' class='variantList'>
					
		</table>
		
		
	
	
	<div style='clear:both;padding:4px;background-color:#ddd;text-align:center;margin-bottom:6px;font-weight:bold'>
		Variants
		</div>
		<ul data-role=listview style='padding-top:4px;padding-bottom:20px' id='variantList'>
		
		</ul>
		
<%@ include file='menuFooter.jsp' %>	
</div>

<script type='text/template' id='product-detail-main-template'>

					<td class='image-container'>
						<img src='{{print(imageUrl)}}'/>
					</td>
					<td class='text-container' style='padding:20px'>
						<h3 class='ui-li-heading' style='margin:0px'>{{print(name)}}</h3>
						<p style='margin:0px'>
							
				
							<span class='svPrcnt'>{{print(brand)}}
							<br>Delivered in: {{print(minDays)}} - {{print(maxDays)}} days</span>
							
						</p>
						<!--span class='ad2Crt'>Add To cart</span-->
					</td>
					
</script>	
<script type='text/template' id='product-variant-template'>
<table class='variantList'width=100%>
					<tr>
					{{ if(productOptions.length>0) { }}
					<td class='variant-description'>
						{{for(i=0;i<productOptions.length;i++) {
							print("<span class='svPrcnt'>"+productOptions[i].name+" : "+productOptions[i].value+"</span>");
						}
						}}
						
					</td>				
					{{ } }}
					<td class='text-container'>
						
						<p style='padding-top:3px'>
							<strike>Rs {{print(markedPrice)}}</strike>
							<span class='ofrPrc'> Rs {{print(hkPrice)}}</span>
							<span class='svPrcnt'>{{print(discountPercent)}}% off</span>
							
						</p>
						<!--a class='ad2Crt' href='#' data-url="addtoCart=Place Order&productVariantList[0]={{print(id)}}&productVariantList[0].qty=1&productVariantList[0].selected=true">Place Order</a-->
						<a class='ad2Crt' href='#' data-url="productVariantId={{print(id)}}&productId={{print(id.substring(0,id.indexOf('-')))}}">Place Order</a>
					</td>
					</tr>
				</table>
</script>
<script>

$('#productDetails').bind('pageshow',function(){
		jQuery.support.cors = true;
		
	_.templateSettings = {
			evaluate : /\{\{(.+?)\}\}/g
		};
		
	Backbone.emulateJSON = true;
var urlEval = new URLEval();
var x = $.mobile.path.parseUrl(urlEval.getURLFromHash(location.href));
var queryString = x.search;
/*****Variant Backnbone**S**********/
	var ProductVariantModel = Backbone.Model.extend({
		initialize : function(){
			this.render();
		},
		render : function(){
			var prVaVi = new ProductVariantView({model:this});
			$('#variantList').append(prVaVi.render().el);
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
			$('#variantList').html('');
		},
		updateUI : function(){
			$('#variantList').listview();
			$('#variantList').listview('refresh');
		}
	});
	var ProductVariantView = Backbone.View.extend({
		tagName : 'li',
		template : _.template($('#product-variant-template').html()),
		initialize : function(){
			_.bindAll(this,'render');
		},
		render : function(){
			$(this.el).empty();
			$(this.el).html(this.template(this.model.toJSON()));
			return this;
		}
	});
/*****Variant Backnbone**E**********/

/*****Product Backnbone**S**********/
	var ProductDetailModel = Backbone.Model.extend({
		initialize : function(){
			this.render();
		},
		render : function(){
			var prDeVi = new ProductDetailView({model:this});
			$('#productDetailMain').append(prDeVi.render().el);
		}
	});
	var ProductDetailCollection = Backbone.Collection.extend({
		model : ProductDetailModel,
		initialize : function(){
			this.on('reset',this.clearView,this);
			this.on('add',this.updateUI,this);
			this.clearView();
		},
		clearView : function(){
			$('#productDetailMain').html('');
		},
		updateUI : function(){
			
		}
	});
	var ProductDetailView = Backbone.View.extend({
		tagName : 'tr',
		template : _.template($('#product-detail-main-template').html()),
		initialize : function(){
			_.bindAll(this,'render');
		},
		render : function(){
			$(this.el).empty();
			$(this.el).html(this.template(this.model.toJSON()));
			return this;
		}
	});
/*****Variant Backnbone**E**********/
loadingPop('s','');
	var prVaCo = new ProductVariantCollection();
	var prDeCo = new ProductDetailCollection();
	$.ajax({
			url : wSURL+'mProduct/productDetail/'+queryString,
		dataType: 'json',
		success : function(response){
			if(hasErr(response))
			{
				loadingPop('h');
				alert(getErr(response.message));
			}
			else
			{
				prDeCo.add(response.data);
				if(prVaCo.add(response.data.productVariants))
				{
					$('#variantList').listview();
				}
				
				loadingPop('h');
			}
		},
		error: function(){
			alert('Request failed');
			loadingPop('h');
		}
		
		});
		$('#productDetails').on('click','.ad2Crt',function(e){
	//e.preventDefault();

			var requestURL = $(this).attr('data-url');
			
			$.ajax({
				url: wSURL+'mAddtoCart/addtoCart',
				data: requestURL,
				dataType: 'json',
				type: 'GET',
				success: function(response){
					if(hasErr(response))
					{
						loadingPop('h');
						alert(getErr(response.message));
					}
					else
					{
						alert(response.message);
					}
				}
			});
			console.log(requestURL);

		});
});

</script>
</div>

</body>
</html>
