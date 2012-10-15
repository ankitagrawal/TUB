<!DOCTYPE html>
<html>
<head>
	<title>Health Kart | Online health Store</title>
	<%@ include file='header.jsp' %>	
</head>
<body>
<div data-role="page" id=primaryCategory class="type-home">
<div data-role=header>
	<%@ include file='menuHeader.jsp'%>
	<%@ include file='menuNavBtnSrch.jsp'%>
</div>
	<div data-role="content" style='background-color:white;margin-top:10px'>
		<div id='categoryContainer'>
			<ul data-inset=true>
			</ul>
		</div>
<%@ include file='menuFooter.jsp' %>	
		
	</div>

	<!--div data-role="footer" class="footer-docs" data-theme="c">
			<p>&copy; 2012 jQuery Foundation and other contributors</p>
	</div-->
<script type='text/template' id='categoryTemplate'>

	{{//var totalElements=data.length;
	
		 //for( var i=0;i<totalElements ;i++ ) { 
			
					print("<a href='${httpPath}/sub-category.jsp?primaryCategory="+encodeURIComponent(name)+"'>"+name+"</a>");
			//	}
		  }}
	</script>
	<script type='text/javascript'>
	
	$('#primaryCategory').bind('pageshow',function(){
	jQuery.support.cors = true;
	_.templateSettings = {
	evaluate : /\{\{(.+?)\}\}/g
		};
	Backbone.emulateJSON = true;
	/*Backbone.sync = function(method, model, options) {};*/
	
		var CategoryModel = Backbone.Model.extend({
			initialize: function(){
			_.bindAll(this,'render');
				this.render();
				
			},
			render: function(){
				var catView = new CategoryView({model:this});
				
				$('#categoryContainer ul').append(catView.render().el);
			}
		});
		
		var CategoryCollection = Backbone.Collection.extend({
		model : CategoryModel,
		initialize: function(){
			_.bindAll(this,'clearView');
			this.clearView();
			this.on('reset',this.clearView,this);
			
		},
		clearView : function(){
			//console.log('in');
			$('#categoryContainer ul').html('<li class=heading data-role="list-divider">CATEGORY</li>');
		},
		updateUI : function(){
			$('#categoryContainer ul').listview();
			$('#categoryContainer ul').listview('refresh');
		}
		/*url : wSURL+'mShop/primaryCategory',
		parse : function(response){
			if(hasErr(response))
			{
				alert(getErr(response.message));
			}
			else
			{
				return response.data;
			}
		}*/
		});
		
		
		var CategoryView = Backbone.View.extend({
			tagName : 'li',
			className: '',
			initialize: function(){
				
				_.bindAll(this,'render');
				
				
			},
			template: _.template($('#categoryTemplate').html()),
			render: function(){
				$(this.el).empty();
				$(this.el).html(this.template(this.model.toJSON()));
				$(this.el).attr('data-icon','arr')
				return this;
			}
		});
		
		var catCol = new CategoryCollection();
		//catCol.reset();
		/*catCol.fetch();*/
		loadingPop('s','');
		$.ajax({
			url : wSURL+'mShop/primaryCategory',
		success : function(response){
			if(hasErr(response))
			{
				loadingPop('h');
				alert(getErr(response.message));
			}
			else
			{
				if(response.data.length == 0 ||response.data == null)
				{
					$('#categoryContainer ul').append('<h3 style="padding-left:20px">No Data Found!</h3>');
				}
				else
				{
					if(catCol.add(response.data))
					{
						catCol.updateUI();
						console.log(response.data);
					}
				}
				loadingPop('h');
			}
		},
		error: function(){
			alert('Request failed');
			loadingPop('h');
		}
		
		});
		/*catCol.add({"category":[{"id":1,"name":"Nutrition","img":"1.jpg"},{"id":2,"name":"Sports & Fitness","img":"2.jpg"},
		{"id":3,"name":"Diabetes","img":"2.jpg"},{"id":4,"name":"Home Devices","img":"2.jpg"},
		{"id":5,"name":"Eye","img":"2.jpg"},{"id":6,"name":"Personal Care","img":"2.jpg"},
		{"id":7,"name":"Beauty","img":"2.jpg"},{"id":8,"name":"Parenting","img":"8.jpg"},
		{"id":9,"name":"Services","img":"9.jpg"}]});*/
		
		
		
		});
	</script>
		
		
	
</div>
</body>
</html>
