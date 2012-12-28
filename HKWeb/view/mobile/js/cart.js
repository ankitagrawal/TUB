var caPaVi;
$('#cart').bind('pagebeforeshow', function () {

    jQuery.support.cors = true;
    _.templateSettings = {
        evaluate: /\{\{(.+?)\}\}/g
    };
    Backbone.emulateJSON = true;
    
    var x = $.mobile.path.parseUrl(location.href);
    var queryString = x.search;
    
    /*****Cart total Backbone**S**********/
    var CartTotalModel = Backbone.Model.extend({
    	initialize : function() {
			this.render();
		},
		render : function() {
			var caToVi = new CartTotalView({model:this});
			$('#cartTotal').html(caToVi.render().el);
		},
		unRender : function(){
			this.destroy();
		}
    })
    
    var CartTotalCollection = Backbone.Collection.extend({
    	model : CartTotalModel,
    	initialize: function () {
            this.on('reset', this.clearView, this);
            this.on('add', this.updateUI, this);
            this.clearView();
        },
        clearView: function () {
            $('#cartTotal').html('');
        },
        updateUI: function () {
            $('#cartTotal').listview();
            $('#cartTotal').listview('refresh');
        }
    })
    
    var CartTotalView = Backbone.View.extend({
    	className : 'cart-total-container',
    	initialize : function(){
    		_.bindAll(this,'render','unRender');
    		  this.on('unRender', this.model.unRender, this);
    	},
    	template : _.template($('#cart-total-template').html()),
    	render : function(){
    		$(this.el).empty();
    		$(this.el).html(this.template(this.model.toJSON()));
    		return this
    	},
    	unRender: function () {
            this.remove();
        }
    })
    
    var caToCo = new CartTotalCollection();
    /*****Cart totalBackbone**E**********/
    
    
    /*****Cart Items Backnbone**S**********/
    var CartModel = Backbone.Model.extend({
        initialize: function () {
            this.render();
        },
        render: function () {
            var caVi = new CartView({
                model: this
            });
            $('#cartList').append(caVi.render().el);
        },
        unRender: function () {

            this.destroy();
        }
    });
    
    var CartCollection = Backbone.Collection.extend({
        model: CartModel,
        initialize: function () {
            this.on('reset', this.clearView, this);
            this.on('add', this.updateUI, this);
            this.clearView();
        },
        clearView: function () {
            $('#cartList').html('');
            $('#cartList').css('height', 'auto');
        },
        updateUI: function () {
            $('#cartList').listview();
            $('#cartList').listview('refresh');
        }
    });
    
    var caCo = new CartCollection();
    
    var CartView = Backbone.View.extend({
        tagName: 'li',
        template: _.template($('#cart-template').html()),
        initialize: function () {
            _.bindAll(this, 'render');
            this.on('unRender', this.model.unRender, this);
        },
        render: function () {
            $(this.el).empty();
            $(this.el).html(this.template(this.model.toJSON()));
            return this;
        },
        unRender: function () {
            this.remove();
        },
        events: {
            'click .rm2Crt': 'removeItem',
            'click .su2Crt': 'removeItem',
            'click .pl2Crt': 'removeItem',
        },
        removeItem: function (e) {
            var ele = e.currentTarget;
            var query = $(ele).attr('data-url');
            var viewObj = this;
			loadingPop('s','');
            $.ajax({
                url: wSURL + __hkG.urls.removeFromCart,
                data: query,
                type: 'get',
                success: function (data) {
                    if (hasErr(data)) {
                        popUpMob.show(data.message);
						loadingPop('h');
                    } else {                    	
                        if (caCo.reset()&&caToCo.reset()) {
                            if ($(ele).hasClass('pl2Crt')) popUpMob.show(__hkG.msgs.successAdd);
                            else popUpMob.show(__hkG.msgs.successRemove);

                            caCo.add(data.data.cartItemsList);
                            caToCo.add(data.data.pricingDto);
							loadingPop('h');
                            }                        
                    }
                }
				
            });
			
        }
    });
	
    var CartParentView = Backbone.View.extend({
		el : '#cart',
		initialize: function(){			
			$('#couponContainer').addClass('hide');
			
		},
		events: {
			'click #submitCoupon' : 'applyCoupon'
		},
		applyCoupon : function(){
			//var e = ele.currentTarget();
			loadingPop('s','');			
			$.ajax({
				url: wSURL + __hkG.urls.applyCoupon,
				timeout: __hkG.timeOut.medium,
				type: 'post',
				data: 'coupon='+$('#couponText').val(),
				dataType: 'json',
				success : function(response){
					if(hasErr(response))
					{
						popUpMob.show(response.message);
					}
					else
					{
						popUpMob.show(response.message);
					}
				},
				async : false
			});
			loadingPop('h');
		}
	});
    /*****Cart Items Backbone**E**********/
    

    caPaVi = new CartParentView();
    loadingPop('s', '');

    $.ajax({
        url: wSURL + __hkG.urls.viewCart,
        dataType: 'json',
        success: function (response) {
            if (hasErr(response)) {
                loadingPop('h');
                popUpMob.show(getErr(response.message));
                $('#btnChkOut').hide();
            } else {
                if (response.data.cartItemsList.length == 0 || response.data.cartItemsList == null) {
                    $('#cartList').html('<h3 style="padding-left:20px;padding-top:20px">No Product in Cart</h3>');
                } else {
                    if (caCo.add(response.data.cartItemsList) && caToCo.add(response.data.pricingDto)) {
                        if ($('#cartList').listview()) {
                            setTimeout(function () {
                                $('#cartList').css('height', $('#cartList').height());
								$('#couponContainer').removeClass('hide');
                            }, 400);
                        }
                    }
                }
                loadingPop('h');
            }
        },
        error: function () {
            popUpMob.show(_hkG.errs.requestFail);
            loadingPop('h');
        }

    });
});
$('#cart').bind('pageshow',function(){
		loadingPop('s','');
		
	});
$('#cart').bind('pagebeforehide',function(){
	caPaVi.undelegateEvents();
});
