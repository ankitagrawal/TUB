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
<div id="login-header"><h2 style='color:#2186C6;font-weight:normal'>Use one of your saved Addresses</h2></div>
  <div id="address-block">
   <div style='padding:10px'>
   <div style='border:1px solid #234;margin-bottom:10px;padding:5px'>
   <h2>Address 1</h2>
   Plot No:233<br>
   Sector 14, Gurgaon<br>
   Pin-123231, Haryana
   <div><a href='#'>Delete</a></div>
   </div>
   <div style='border:1px solid #234;margin-bottom:10px;padding:5px'>
   <h2>Address 2</h2>
   Plot No:707<br>
   Sector 21-B, Gurgaon<br>
   Pin-123231, Haryana
   <div><a href='#'>Delete</a></div>
   </div>
   </div>
  </div> 
</div>
<!-- end right pannel -->
<div id='registarViewContent' class='viewContent hide'>
<div id="login-header"><h2 style='color:#2186C6;font-weight:normal'>Create account</h2>
 <!-- block -->
<div id="signin-block">
 <form autocomplete="on" id='registration' action="/webservices/users/registration"> 
   <p style="padding:10px 0 10px 0"> 
   <label for=usernamesignup>Name <span>*</span></label>
  <input type="text" placeholder="Enter your name"  name="username" id="usernamesignup" class='ui-corner-all' >
   </p>
 <p style="padding:10px 0 10px 0"> 
  <label class="youmail" for="emailsignup"> Address Line 1 <span>*</span></label>
  <input type="email" placeholder="Enter your Email address"  name="emailaddress" id="emailsignup"> 
                                </p>
	<p style="padding:10px 0 10px 0"> 
  <label class="youmail" for="emailsignup"> Address Line 2 <span></span></label>
  <input type="email" placeholder="Enter your Email address"  name="emailaddress" id="emailsignup"> 
                                </p>
                                <p style="padding:10px 0 10px 0">  
  <label for="passwordsignup">City <span>*</span></label>
   <input type="text" placeholder="Enter your City"  name="password" id="passwordsignup">
                                </p>
                                 <p style="padding:10px 0 10px 0">  
  <label for="passwordsignup">State <span>*</span></label>
   <input type="text" placeholder=""  name="password" id="passwordsignup">
                                </p>
								 <p style="padding:10px 0 10px 0">  
  <label for="passwordsignup">PIN Code <span>*</span></label>
   <input type="text" placeholder=""  name="password" id="passwordsignup">
                                </p>
								 <p style="padding:10px 0 10px 0">  
  <label for="passwordsignup">Phone/Mobile <span>*</span></label>
   <input type="text" placeholder=""  name="password" id="passwordsignup">
                                </p>
                                <p class="signin button"> 
									<br>
									<!--input type="submit" value="Use this & Continue"-->
<a href='orderSummary.jsp' data-role=button>Use this & Continue</a>									
								</p>
                                <br>
								<br>
                            </form>
</div> 

<!-- block end -->
</div>
<div style="clear:both"></div>
</div>

</div>

	
<script>
$('#address').bind('pageshow',function(){
		var AddressModel = Backbone.Model.extend({
			initialize: function(){
				this.render();
			},
			userDefinedElement: '#address-block',
			render: function(){
				var adVi = new AddressView({model:this});
				$(this.userDefinedElement).append(adVi.render.el);
			}
		});
		var AddressCollection = Backbone.Collection.extend({
			model: AddressModel,
			initialize: function(){
				this.clearView();
				this.on('reset',this.clearView(),this);
			}
			clearView: function(){
				$(this.userDefinedElement).html();
			}
		});
		var AddressView = Backbone.View.extend({
			tagName: 'div',
			className: 'savedAddressView',
			initialize: function(){
				
			},
			template: _.template($('#address-view-template')),
			render: function(){
				$(this.el).empty();
				$(this.el).(this.template(this.model.toJSON()));
			}
			
		});
		$("#usernamesignup").validate({
                    expression: "if (VAL=='') return false; else return true;",
                    message: "Please enter name.",
		});
		
		$("#emailsignup").validate({
                    expression: "if (VAL.match(/^[^\\W][a-zA-Z0-9\\_\\-\\.]+([a-zA-Z0-9\\_\\-\\.]+)*\\@[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*\\.[a-zA-Z]{2,4}$/)) return true; else return false;",
                    message: "Please enter valid Email id",
                });
                

		$("#passwordsignup").validate({
                    expression: "if (VAL==''||VAL.length<6) return false; else return true;",
                    message: "Please enter Valid Password. Minimum size 6.",
		});
		
		var kpass = $("#passwordsignup").val();
		
		$("#passwordsignup_confirm").validate({
                   expression: "if ((VAL == jQuery('#passwordsignup').val()) && VAL) return true; else return false;",
                     message: "Confirm password field doesn't match the password field",
		});
		
		$('#accept').validate({
			expression: "if ( $('#loginkeeping').is(':checked')) return true; else return false;",
                     message: "Please Read & Accept Terms and Condition",
		});
		
		
	/*	$('#registration').validated(function(){
			
		var path=$('#registration').attr('action');
		var redirectPath=$('#authenticate').attr('action');
		var info=$('#registration').serialize();
		$.ajax({
		url: wSURL + path,
		data: info+"&protocol=REST",
		dataType: 'json',
		type: 'POST',
		success: function(data)
		{
			
			
		},
		error : function(e) {
			
			
		}
		});
		});
*/
	
		
		
	$('.gNav').click( function(e){
		
		var ele = e.currentTarget;
		var eleId = $(ele).attr('id');
		$('.viewContent').hide();
		$('#'+eleId+'Content').show();
		
	});	

});

</script>
</div>
</body>
</html>
