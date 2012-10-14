<!DOCTYPE html>
<html>
<head>
		<%@ include file='header.jsp' %>	
	
</head>
<body>
<div data-role="page" id=address class="type-home">
<div data-role=header>
	<%@ include file='menuHeader.jsp'%>
	<%@ include file='menuNavBtn.jsp'%>
</div>
	<div data-role="content" style='height:auto'>	
			<div style='background-color:#319aff;padding:10px;margin-bottom:10px'>
				<div data-role="navbar" >
					<ul>
					  <li style=''><a href="#" class="ui-corner-tl ui-corner-bl ui-btn-active gNav" data-theme='' style='border-color:transparent;background-color:transparent;border-width:0;border-right-width:1px;border-right-color:#0f3d6f' id='defaultView' >Existing Addresses</a></li>
					  <li><a href="#"   style='border-top-color:transparent;border-width:0;' id='registarView' class='gNav ui-corner-tr ui-corner-br'>New Address</a></li>
					 
					 </ul>
				</div>
			</div>
<!-- end left -->
<!-- start right pannel -->
<div id='defaultViewContent' class=viewContent>
<div id="address-header" style='clear:both;margin-bottom:2px'><h2 style='color:#2186C6;font-weight:normal'>Use one of your saved Addresses</h2></div>
  <div  style='margin-top:14px'  id="address-block">
  
  <!-- <div style='padding:10px'>
   <div class=arr-r style='border-bottom:none;border-right:none;margin-bottom:15px;padding:5px;box-shadow:2px 2px 8px #CBE5FF,-2px -2px 8px #CBE5FF;-webkit-box-shadow:2px 2px 8px #CBE5FF,-2px -2px 8px #CBE5FF'>
   <h2>Address 1</h2>
   Plot No:233<br>
   Sector 14, Gurgaon<br>
   Pin-123231, Haryana
   <div><a href='#'><img src="images/remove.png"></a></div>
   </div>
   <div class=arr-r style='margin-bottom:10px;padding:5px;box-shadow:2px 2px 8px #CBE5FF,-2px -2px 8px #CBE5FF;-webkit-box-shadow:2px 2px 8px #CBE5FF,-2px -2px 8px #CBE5FF;'>
   <h2>Address 2</h2>
   Plot No:707<br>
   Sector 21-B, Gurgaon<br>
   Pin-123231, Haryana
   <div><a href='#'><img src="images/remove.png"></a></div>
   </div>
   
   </div>-->
  </div> 
</div>
<br/>
<br/>
<!-- end right pannel -->
<div id='registarViewContent' class='viewContent hide'>
<div id="newaddress-header"><h2 style='color:#2186C6;font-weight:normal'>Add new address</h2>
 <!-- block -->
<div id="newaddress-block" style='margin:0px 10px'>
 <form autocomplete="on" id='address_registration' action="mAddress/addAddress"> 
   <p style="padding:10px 0 10px 0"> 
   <!--label for=usernamesignup>Name <span>*</span></label-->
  <input type="text" placeholder="Enter your name"  name="name" id="address_name" class='ui-corner-all' >
   </p>
                               <p style="padding:10px 0 10px 0">  
  <!--label for="passwordsignup">City <span>*</span></label-->
   <input type="text" placeholder="Enter line1"  name="line1" id="address_line1">
                                </p>
                                 <p style="padding:10px 0 10px 0">  
  <!--label for="passwordsignup">State <span>*</span></label-->
   <input type="text" placeholder="Enter line2"  name="line2" id="address_line2">
                                </p>
								 <p style="padding:10px 0 10px 0">  
  <!--label for="passwordsignup">PIN Code <span>*</span></label-->
   <input type="text" placeholder="Enter city"  name="city" id="address_city">
                                </p>
								 <p style="padding:10px 0 10px 0">  
  <!--label for="passwordsignup">Phone/Mobile <span>*</span></label-->
   <input type="text" placeholder="Enter state"  name="state" id="address_state">
                                </p>
								
<p style="padding:10px 0 10px 0">  
  
   <input type="text" placeholder="Enter pincode"  name="pin" id="address_pin">
                                </p>
<p style="padding:10px 0 10px 0">  
  
   <input type="text" placeholder="Enter phone number"  name="phone" id="address_phone">
                                </p>
                                <p class="signin button"> 
									<br>
									<!--input type="submit" value="Use this & Continue"-->
                                    <input type='submit' data-role=button id="use_address_button" value="Use this & Continue"/>
								</p>
                                <br>
								<br>
                            </form>
</div> 

<!-- block end -->
</div>
<div style="clear:both"></div>
</div>
		<%@ include file='menuFooter.jsp' %>	
</div>
<script type="text/template" id="address-view-template2">
	<h2>Address 1</h2>
   Plot No:233<br>
   Sector 14, Gurgaon<br>
   Pin-123231, Haryana
   <div><a href='#'>Delete</a></div>
  </script>
 <script type='text/template' id='address-view-template'>		
 
			{{for(var i =0;i<data.length;i++){ }}
			
			<li  class='shadow-4-address arr-r'>
			<a class='addAddressToOrder' href='javascript:void(0)' addrId="{{print(data[i].id)}}">
				<table width='100%'>
					<tr>
					<td class='text-container'>
						<h3 style='white-space:normal;padding-left:10px;margin-top:0px'>{{print(data[i].name)}}</h3>
						<p style='margin:0px;padding-left:10px'>
							<span class='addressText'>{{print(data[i].line1)}}</span><br>
							<span class='addressText'>{{print(data[i].line2)}}</span><br>
							<span class='addressText'>{{print(data[i].city)}}</span>,
							<span class='addressText'>{{print(data[i].state)}}</span><br>
							<span class='addressText'>PIN Code:{{print(data[i].pin)}}</span><br>
							<span class='addressText'>Ph: {{print(data[i].phone)}}</span>
						</p>
						<!--span class='ad2Crt'>Add To cart</span-->
					</td>
					</tr>
				</table>
					</a>
			</li>
			{{ } }}
		
		
</script>
<style>

</style>
<script>
$('#address').bind('pageshow',function(){
       
	 jQuery.support.cors = true;
	$("a.addAddressToOrder").live("click",(function(e){
		authenticateAddress2("addressId="+$(this).attr("addrId"));
		
	}));	
	_.templateSettings = {
			evaluate : /\{\{(.+?)\}\}/g
		};
		
    	Backbone.emulateJSON = true;
		
		var AddressModel = Backbone.Model.extend({
			initialize: function(){
				this.render();
			},
	//		userDefinedElement: '#address-block',
			render: function(){
				var adVi = new AddressView({model:this});		
				$("#address-block").append(adVi.render().el);
							
			}
		});
		var AddressCollection = Backbone.Collection.extend({
			model: AddressModel,
			initialize: function(){
				this.clearView();
				this.on('reset',this.clearView(),this);
			},
			clearView: function(){
				$("#address-block").html('');
			}
		});
		var AddressView = Backbone.View.extend({
			tagName: 'ul',
			template: _.template($('#address-view-template').html()),
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
		
		var addrCol = new AddressCollection();
		//addrCol.reset();
		$.ajax({
		url : wSURL+'mAddress/addresses',
		dataType: 'json',
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
					//$('#address-block ul').listview();
				}
				
				loadingPop('h');
			}
		},
		error: function(){
			popUpMob.show('Request failed');
			loadingPop('h');
		}
		
		});

		
		
	$('.gNav').click(function(e){
		
		var ele = e.currentTarget;
		
		var eleId = $(ele).attr('id');
		$('.viewContent').hide();
		$('#'+eleId+'Content').show();
		
		
		
	});	
	
		$("#address_name").validate({
                    expression: "if (VAL=='') return false; else return true;",
                    message: "Please enter name.",
		});
	

		$("#address_city").validate({
                    expression: "if (VAL=='') return false; else return true;",
                    message: "Please city.",
		});
	

		$("#address_state").validate({
                    expression: "if (VAL=='') return false; else return true;",
                    message: "Please enter state.",
		});
	

		$("#address_line1").validate({
                    expression: "if (VAL=='') return false; else return true;",
                    message: "Please enter name.",
		});
		
		$("#address_line2").validate({
                    expression: "if (VAL=='') return false; else return true;",
                    message: "Please enter name.",
		});
		$("#address_phone").validate({
                    expression: "if (VAL=='') return false; else return true;",
                    message: "Please enter name.",
		});
		
   $('#address_registration').validated(function(){
		$('.loaderContainer').show();
	    authenticateAddress2(null);
		return false;
	});
	
	function addToOrder(addrId){
	        authenticateAddress2("addressId="+addrId);
	}
	function authenticateAddress2(addrId){
	    var path=$('#address_registration').attr('action');
		var data=$('#address_registration').serialize();
		 if(addrId!=null){
		   data = addrId;
		  }
	   $.ajax({
		url: wSURL+path+"?"+data,
		type: 'get',
		dataType: 'json',
		async:false,
		success: function(data)
		{
			if(hasErr(data))
			{	
				popUpMob.show(data.message);
			}
			else
			{
			    setTimeout(function(){location.href="${httpPath}orderSummary"+'.jsp'},500);
				
			}
		},
		
		error : function(e) {
			popUpMob.show("Request Failed");			
		}
		});
	  
	}

});

</script>
</div>
</body>
</html>
