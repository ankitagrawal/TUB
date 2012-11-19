var coMaPaVi;
$('#couponManage').bind('pagebeforeshow',function(){
console.log('sd');
	var CouponManagerModel = Backbone.Model.extend({
		initialize: function(){
			var coMaVi = new CouponManagerView({
				model:this
				});
			$('#couponManagerContainer').append(coMaVi.render().el);
		}
	});
	
	var CouponManagerCollection = Backbone.Collection.extend({
		model : CouponManagerModel,
		//url : wSURL + __hkG.urls.getOffers,
		// parse : function(response){
			// if(hasErr(response))
			// {
				// popUpMob.show(response.messgae);
			// }
			// else
			// {
				// return response.data
			// }
		// },
		initialize: function () {
            this.on('reset', this.clearView, this);
            this.on('add', this.updateUI, this);
            this.clearView();
			var col =this;
			getOffers();
        },
        clearView: function () {
            $('#couponManagerContainer').html('');
            //$('#couponManagerContainer').css('height', 'auto');
        },
        updateUI: function () {
            $('#couponManagerContainer').listview();
            $('#couponManagerContainer').listview('refresh');
        }
	});
	
	coMaCo = new CouponManagerCollection();
	
	var CouponManagerView = Backbone.View.extend({
		tagName : 'tr',
		template : _.template($('#coupon-manage-template').html()),
		initialize : function(){
			_.bindAll(this,'render');
		},
		render : function(){
			$(this.el).empty();
            $(this.el).html(this.template(this.model.toJSON()));
            return this;
		}
	});
	
	function getOffers(){
		$.ajax({
				url: wSURL + __hkG.urls.getOffers,
				dataType: 'json',
				success: function (response) {
					if (hasErr(response)) {
						loadingPop('h');
						popUpMob.show(getErr(response.message));
					} else {
						if (response.data.length == 0 || response.data == null) {
							//$('#cartList').html('<h3 style="padding-left:20px;padding-top:20px">No Product in Cart</h3>');
						} else {
							coMaCo.add(response.data);
						}
						loadingPop('h');
					}
				},
				error: function () {
					popUpMob.show(__hkG.errs.requestFail);
					loadingPop('h');
				}

		});
	}
	
	var CouponManageParentView = Backbone.View.extend({
		el : '#couponManage',
		initialize: function(){
			
		},
		events : {
			'click #couponRemove' : 'removeCoupon',
			'click #couponApply' : 'applyOffer'
		},
		removeCoupon : function(){
			loadingPop('s','');
			$.ajax({
					url: wSURL + __hkG.urls.removeOffer,
					dataType: 'json',
					type: 'post',
					success: function (response) {
						if (hasErr(response)) {
							loadingPop('h');
							popUpMob.show(getErr(response.message));
						} else {
							coMaCo.reset();
							getOffers();
							popUpMob.show(response.message);
							loadingPop('h');
						}
					},
					error: function () {
						popUpMob.show(__hkG.errs.requestFail);
						loadingPop('h');
					}

			});
		},
		applyOffer : function(){
			loadingPop('s','');
			var x = $('#couponManage input[name=couponId]:checked').val();
			if(String(x)=='undefined')
			{
				popUpMob.show('Select a Coupon');
			}
			else
				{
		    	$.ajax({
		    				url: wSURL + __hkG.urls.applyOffer,
		    				dataType: 'json',
		    				type: 'post',
		    				data: 'offer='+x,
		    				success: function (response) {
		    					if (hasErr(response)) {
		    						loadingPop('h');
		    						popUpMob.show(getErr(response.message));
		    					} else {
		    						coMaCo.reset();
		    						getOffers();
		    						popUpMob.show(response.message);
		    						loadingPop('h');
		    					}
		    				},
		    				error: function () {
		    					popUpMob.show(__hkG.errs.requestFail);
		    					loadingPop('h');
		    				}
		    
		    			});
				}
		}
	});
	coMaPaVi = new CouponManageParentView();
});
$('#couponManage').bind('pagebeforehide',function(){
	coMaPaVi.undelegateEvents();
})
