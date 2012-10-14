<!DOCTYPE html>
<html>
<head>
	<title>Health Kart | Online health Store</title>
	<%@ include file='header.jsp' %>	
</head>
<body>
<div data-role="page" id='sub-category'>
<div data-role=header>
	<%@ include file='menuHeader.jsp'%>
	<%@ include file='menuNavBtnSrch.jsp'%>
	
	
</div>
	<div data-role="content" style='background-color:white'>

		
		
				
		<div id='subCategoryContainer' style='clear:both;padding:1px 0px'>
			<ul data-inset=true data-role=listview>
			</ul>
		</div>
		<!--div style='clear:both;padding:4px'>
		Top Products
		</div-->
		<!--div id='sub-category-scroll'>
		</div-->
		<%@ include file='menuFooter.jsp' %>	
	</div>
	
	<script type='text/template' id='product-fav-template'>
		<tr>
		
			{{for(var i =0;i<favItem.length;i++){ }}
			<td class=scrollBlock>
			
				<div class=scrollBlockContent>
					<img src='images/{{print(favItem[i].img)}}'/>
					<div>
						{{print(favItem[i].label)}}
					</div>
				</div>
			</td>
			{{ } }}
		
		</tr>
		</script>
		<script type='text/template' id='sub-category-template'>
		
		{{
		var urlEval = new URLEval();
		var pC = ($.mobile.path.parseUrl(urlEval.getURLFromHash(location.href))).search;pC = pC.replace('?','');}}
			<a href='product.jsp?{{print(pC)}}&secondaryCategory={{print(encodeURIComponent(name))}}'>{{print(name)}}</a>		
		</script>
		<script type='text/javascript'>
		
$("#sub-category").bind("pageshow", function(event,ui) {
//var wSURL = 'http://healthtest:9090/healthkart/rest/api/';
var urlEval = new URLEval();
var x = $.mobile.path.parseUrl(urlEval.getURLFromHash(location.href));
		var queryString = x.search;
		//alert(queryString);
jQuery.support.cors = true;
_.templateSettings = {
	evaluate : /\{\{(.+?)\}\}/g
		};
	Backbone.emulateJSON = true;
	/*Backbone.sync = function(method, model, options) {};*/
	var ProductScrollModel = Backbone.Model.extend({
			initialize: function(){
				_.bindAll(this,'render');
				this.render();
			},
			render: function(){
				var prScVi = new ProductScrollView({model:this});
				$('#sub-category-scroll').append(prScVi.render().el);
			}
		});
		
		var ProductScrollCollection = Backbone.Collection.extend({
			model: ProductScrollModel,
			initialize : function(){
				this.on('reset',this.clearView,this);
				this.clearView();
			},
			clearView : function(){
				$('#sub-category-scroll').html('');
			}
		});
		
		var ProductScrollView = Backbone.View.extend({
			tagName: 'table',
			className: 'scrollWindow',
			initialize: function(){
				_.bindAll(this,'render');
			},
			template: _.template($('#product-fav-template').html()),
			render: function(){
				$(this.el).empty();
				$(this.el).html(this.template(this.model.toJSON()));
				return this;
			}
		});
		
		/*****Sub Category listing *S****/
		var SubCategoryModel = Backbone.Model.extend({
			initialize: function(){
				_.bindAll(this,'render');
				this.render();
			},
			render: function(){
				var suCaVi = new SubCategoryView({model:this});
				$('#subCategoryContainer ul').append(suCaVi.render().el);
			}
		});
		
		var SubCategoryCollection = Backbone.Collection.extend({
			model: SubCategoryModel,
			initialize: function(){
				_.bindAll(this,'clearView');
				this.clearView();
				this.on('reset',this.clearView,this);
			},
			clearView : function(){
			//console.log('in');
				$('#subCategoryContainer ul').html('<li class=heading data-role="list-divider">CATEGORY</li>');
			},
			updateUI : function(){
				$('#subCategoryContainer ul').listview();
				$('#subCategoryContainer ul').listview('refresh');
			}
			
		});
		
		var SubCategoryView = Backbone.View.extend({
			tagName: 'li',
			className: '',
			initialize: function(){
				_.bindAll(this,'render');
			},
			template: _.template($('#sub-category-template').html()),
			render: function(){
				$(this.el).empty();
				$(this.el).html(this.template(this.model.toJSON()));
				$(this.el).attr('data-icon','arr')
				return this;
			}
		});
		/*****Sub Category listing *E****/
		
		var prScCo = new ProductScrollCollection();
		if(prScCo.add({"favItem":[{"id":1,"label":"Nutrition","img":"1.jpg"},{"id":2,"label":"Sports & Fitness","img":"2.jpg"},
		{"id":3,"label":"Diabetes","img":"3.jpg"},{"id":4,"label":"Home Devices","img":"4.jpg"},
		{"id":5,"label":"Eye","img":"5.jpg"},{"id":6,"label":"Personal Care","img":"6.jpg"},
		{"id":7,"label":"Beauty","img":"7.jpg"},{"id":8,"label":"Parenting","img":"8.jpg"},
		{"id":9,"label":"Services","img":"9.jpg"}]}))
		{
			$('.scrollWindow').scrollWindow({scrollFactor:35});   
		}
		var suCaCo = new SubCategoryCollection();
		suCaCo.reset();
		loadingPop('s','');
		
		$.ajax({
			url : wSURL+'mShop/secondaryCategory'+queryString,
		success : function(response){
			if(hasErr(response))
			{
				loadingPop('h');
				alert(getErr(response.message));
			}
			else
			{
				
				if(suCaCo.add(response.data))
				{
					suCaCo.updateUI();
				}
				
				loadingPop('h');
			}
		},
		error: function(){
			alert('Request failed');
			loadingPop('h');
		}
		
		});
		/*if(suCaCo.add({"subItem":[{"id":1,"label":"Nutrition","img":"1.jpg"},{"id":2,"label":"Sports & Fitness","img":"2.jpg"},
		{"id":3,"label":"Diabetes","img":"3.jpg"},{"id":4,"label":"Home Devices","img":"4.jpg"},
		{"id":5,"label":"Eye","img":"5.jpg"},{"id":6,"label":"Personal Care","img":"6.jpg"},
		{"id":7,"label":"Beauty","img":"7.jpg"},{"id":8,"label":"Parenting","img":"8.jpg"},
		{"id":9,"label":"Services","img":"9.jpg"}]}))
		{
			$('#subCategoryContainer ul').listview();   
		}*/
		
				
		
		
//var wSURL = 'http://www.healthkartplus.com/'    
	//	jQuery.support.cors = true;

		//var wSURL = 'http://healthkartplus.com/'; 
/*		$("#productSearch").autocomplete(
				{
					source : function(request, response) {
					
					$('#menuBar').addClass('hide');
					$("#medicineSearch").autocomplete('close');
						$('#status').html('');
						var queryStr = '';
						if ($('#pSCFlag').is(':checked')) {
							//queryStr = '&' + $('input[name=maId]').serialize();
						}
						$.ajax({
							url : wSURL + "webservices/search/both?name="+ encodeURIComponent(request.term)	+ queryStr,
							//url: 'http://localhost:8080/m/c.json?'+request.term,
							dataType : "json",
	
							success : function(data) {
								
								$('.ui-autocomplete').removeClass('ui-corner-all');
								$('.ui-autocomplete-loading').removeClass(
										"ui-autocomplete-loading");
								if (data.result == null
										|| data.result.length == 0) {
try{
										 if(data.suggestions.length)
										{
										
                                            $(".ui-autocomplete").prepend("<li class='ui-menu-item arr-r' role='menuitem'><a style='color:#2384C6' class=ui-corner-all tabindex='-1'>Did You mean</a>");    
                                                            
											  var dd = [];
											  var kk = {};
											  var y=-1;
											  response(
											  
													 $.map(data.suggestions, function(item) {
													 
													 y++;
														return {
																	label : item ,
																	value : {
												"id" : 0,
												"type" : '',
												"name" : item,
												"mfg": 'me',
												"counter": y
											}
                                                                               
                                                                                }
                                                                            
                                                                            }
                                                                      ));
                                                                               
									   }  
									else
									{
										
											response(
											  
													 $.map([$('#medicineSearch').val()], function(item) {
													 
													 //y++;
														return {
																	label : item ,
																	value : {
												"id" : 0,
												"type" : '',
												"name" : item,
												"mfg": 'me',
												"counter": -1
											}
											}
											}));
									}
}
catch(e){console.log(e)}
						//			$("#medicineSearch").autocomplete('close');
								}   else {
                                                                
                                     response(
	                                                           
										$.map(data.result, function(item) {
										var temp = item.packSize;
										var lbl;
										if(temp)
										{
											lbl= item.name + ' - ' + temp;
										}
										else
										{
											lbl = item.name;
										}
										return {
											label : lbl,
											value : {
												"id" : item.id,
												"type" : item.type,
												"name" : item.name,
												"mfg": item.manufacturer
											}
			
										}
                                                                              
									})); 
                                   }
							},
							error : function(result) {
								
								$('.ui-autocomplete-loading').removeClass(
										"ui-autocomplete-loading");
								
							}
						});
					},
					minLength : 2,
					delay: 500,
                  select : function(event, ui) {
				  
				 
				  
							if(ui.item.value.id==0)
							{
								$('#medicineSearch').val(ui.item.label);		
								$('#medicineSearch').autocomplete('search');
							}
							else if(ui.item.value.id==-1)
							{
								
								location.href='/m/suggestions?name='+encodeURIComponent(ui.item.label);
								$('#medicineSearch').val('');
							}
							else
							{
								$('#type').val(ui.item.value.type);
								$("#id").val(ui.item.value.id);
								$("#name").val((ui.item.value.name).replace(/[/\\]/g,''));
								//$("#medSet").submit();
							var url= $("#medSet").attr('action');
							url+='/'+encodeURIComponent($('#type').val());
							url+='/'+encodeURIComponent($('#id').val());
							url+='/'+encodeURIComponent($('#name').val());
							$('#medicineSearch').autocomplete('close');
							$.mobile.changePage('product.html');
							$('#medicineSearch').val('');
							return false;
							}
							
							return false;
						},
					
					open : function(data) {
					
						$('.ui-autocomplete').css({'left':'0px','top':'75px','width':'100%'});
						
					},
					close : function() {
						$(this).removeClass("ui-corner-top").addClass(
								"ui-corner-all");
					},
					focus : function(event, ui) {
					$(event.currentTarget).removeClass('ui-state-hover');
					try{
						$('#medicineSearch').val(ui.item.value.name);
						}
						catch(e){}

						return false;
					}
				}).data("autocomplete")._renderItem = function(ul, item) {
                                        
			if (item.value.type == 'drugs') {
				return $("<li style='padding:3px 0px' class='arr-r'></li>").data("item.autocomplete", item).append(
						"<a style='color:#2384C6'>" + item.label +"<div style='color:black;font-size:11px'>"+item.value.mfg+ "</div></a>")
						.appendTo(ul);
			} else if (item.value.type == 'generics') {
                         return $("<li style='padding:8px 0px' class=arr-r></li>").data("item.autocomplete", item).append(
						"<a style='color:black'>" + item.label + "</a>").appendTo(ul);
				}
				else{
					if(item.value.counter==0||item.value.counter==-1)
					{
						
						
						$("<li style='background-color:gray'></li>").data("item.autocomplete", item).append("<div style='color:white'>Add to HealthKart+ database</div>").appendTo(ul);
						
						var temp =$.extend(true,{},item);
						temp.value.id= -1;
						temp.label=$("#medicineSearch").val();
						temp.value.name=$("#medicineSearch").val();

						$("<li style='padding:5px 0px' class=arr-r></li>").data("item.autocomplete", temp).append("<a>"+$("#medicineSearch").val()+"</a>").appendTo(ul);
						if(item.value.counter!=-1)
						{
							$("<li style='background-color:gray' class=arr-r></li>").data("item.autocomplete", item).append("<div style='color:white'>Did you mean ?</div>").appendTo(ul);
							return $("<li style='padding:8px 0px' class=arr-r></li>").data("item.autocomplete", item).append(
							"<a style='color:black'>" + item.label + "</a>").appendTo(ul);
						}
					}
					else
					{
						 return $("<li style='padding:8px 0px' class=arr-r></li>").data("item.autocomplete", item).append(
						"<a style='color:black'>" + item.label + "</a>").appendTo(ul);
					}
				}
				
                               // $(".ui-autocomplete").first().append("<li class='ui-menu-item' role='menuitem'><a style='color:#2384C6' class=ui-corner-all tabindex='-1'>Did You mean<img style='width:20px;height:20px;float:right;padding-top:5px' src='/m/images/arr.png'></a>");
	
					};
				
*/				
				
				
     
       
	

    
    

    
    });

</script>   
</div>

</body>
</html>
