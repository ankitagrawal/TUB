$('#productDetails').bind('pagebeforeshow', function () {
    jQuery.support.cors = true;

    _.templateSettings = {
        evaluate: /\{\{(.+?)\}\}/g
    };

    Backbone.emulateJSON = true;
    var urlEval = new URLEval();
    var x = $.mobile.path.parseUrl(urlEval.getURLFromHash(location.href));
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
            $('#variantList').append(prVaVi.render().el);
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
            $('#variantList').html('');
        },
        updateUI: function () {
            $('#variantList').listview();
            $('#variantList').listview('refresh');
        }
    });
    
    var ProductVariantView = Backbone.View.extend({
        tagName: 'li',
        template: _.template($('#product-variant-template').html()),
        initialize: function () {
            _.bindAll(this, 'render');
        },
        render: function () {
            $(this.el).empty();
            $(this.el).html(this.template(this.model.toJSON()));
            return this;
        }
    });
    /*****Variant Backnbone**E**********/

    /*****Product Backnbone**S**********/
    var ProductDetailModel = Backbone.Model.extend({
        initialize: function () {
            this.render();
        },
        render: function () {
            var prDeVi = new ProductDetailView({
                model: this
            });
            $('#productDetailMain').append(prDeVi.render().el);
        }
    });
    
    var ProductDetailCollection = Backbone.Collection.extend({
        model: ProductDetailModel,
        initialize: function () {
            this.on('reset', this.clearView, this);
            this.on('add', this.updateUI, this);
            this.clearView();
        },
        clearView: function () {
            $('#productDetailMain').html('');
        },
        updateUI: function () {

        }
    });
    
    var ProductDetailView = Backbone.View.extend({
        tagName: 'tr',
        template: _.template($('#product-detail-main-template').html()),
        initialize: function () {
            _.bindAll(this, 'render');
        },
        render: function () {
            $(this.el).empty();
            $(this.el).html(this.template(this.model.toJSON()));
            return this;
        }
    });
    /*****Variant Backnbone**E**********/
    
    loadingPop('s', '');
    var prVaCo = new ProductVariantCollection();
    var prDeCo = new ProductDetailCollection();
    
    $.ajax({
        url: wSURL + 'mProduct/productDetail/' + queryString,
        dataType: 'json',
        success: function (response) {
            if (hasErr(response)) {
                loadingPop('h');
                popUpMob.show(getErr(response.message));
            } else {
                if (response.data.length == 0 || response.data == null) {
                    $('#productDetailMain').html('<h3 style="padding-left:20px">No Data Found!</h3>');
                } else {
                    prDeCo.add(response.data);
                    
                    if (prVaCo.add(response.data.productVariants)) {
                        $('#variantList').listview();
                    }
                }
                loadingPop('h');
            }
        },
        error: function () {
            popUpMob.show(__hkG.errs.requestFail);
            loadingPop('h');
        }

    });
    $('#productDetails').on('click', '.ad2Crt', function (e) {
       loadingPop('s','');
        var ele = e.currentTarget;
        var requestURL = $(this).attr('data-url');

        $.ajax({
            url: wSURL + __hkG.urls.addToCart,
            data: requestURL,
            dataType: 'json',
            type: 'GET',
            success: function (response) {
                if (hasErr(response)) {
                    loadingPop('h');
                    popUpMob.show(getErr(response.message));
                } else {
                    popUpMob.show(response.message);
                    $(ele).removeClass('ad2Crt').addClass('go2Crt').html('<a href="'+httpPath+'/cart.jsp" style="text-decoration:none;color:#333">Added to Cart</a>');
					loadingPop('h');
                }
            }
        });
      

    });
});
$('#productDetails').bind('pageshow',function(){loadingPop('s','');});