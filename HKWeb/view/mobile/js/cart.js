$('#cart').bind('pagebeforeshow', function () {
    jQuery.support.cors = true;
    _.templateSettings = {
        evaluate: /\{\{(.+?)\}\}/g
    };
    Backbone.emulateJSON = true;
    
    var x = $.mobile.path.parseUrl(location.href);
    var queryString = x.search;
    
    /*****Variant Backnbone**S**********/
    var ProductVariantModel = Backbone.Model.extend({
        initialize: function () {
            this.render();
        },
        render: function () {
            var prVaVi = new ProductVariantView({
                model: this
            });
            $('#cartList').append(prVaVi.render().el);
        },
        unRender: function () {

            this.destroy();
        }
    });
    
    var ProductVariantCollection = Backbone.Collection.extend({
        model: ProductVariantModel,
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
    
    var prVaCo = new ProductVariantCollection();
    
    var ProductVariantView = Backbone.View.extend({
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
                    	
                        if (prVaCo.reset()) {
                            if ($(ele).hasClass('pl2Crt')) popUpMob.show(__hkG.msgs.successAdd);
                            else popUpMob.show(__hkG.msgs.successRemove);

                            prVaCo.add(data.data);
							loadingPop('h');
                            }
                        
                    }
                }
				
            });
			
        }
    });
    
    /*****Variant Backnbone**E**********/
    
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
                if (response.data.length == 0 || response.data == null) {
                    $('#cartList').html('<h3 style="padding-left:20px;padding-top:20px">No Product in Cart</h3>');
                } else {
                    if (prVaCo.add(response.data)) {
                        if ($('#cartList').listview()) {
                            setTimeout(function () {
                                $('#cartList').css('height', $('#cartList').height());
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
$('#cart').bind('pageshow',function(){loadingPop('s','');});