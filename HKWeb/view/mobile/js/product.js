$('#product').bind('pagebeforeshow', function () {
	//$('div[id=productDetails]').remove();
    var urlEval = new URLEval();
    var x = $.mobile.path.parseUrl(urlEval.getURLFromHash(location.href));
    var queryString = x.search;

    jQuery.support.cors = true;

    _.templateSettings = {
        evaluate: /\{\{(.+?)\}\}/g
    };

    Backbone.emulateJSON = true;

    var ProductScrollModel = Backbone.Model.extend({
        initialize: function () {
            _.bindAll(this, 'render');
            this.render();
        },
        render: function () {
            var prScVi = new ProductScrollView({
                model: this
            });
            $('#product-scroll').append(prScVi.render().el);
        }
    });

    var ProductScrollCollection = Backbone.Collection.extend({
        model: ProductScrollModel,
        initialize: function () {
            this.on('reset', this.clearView, this);
            this.clearView();
        },
        clearView: function () {
            $('#product-scroll').html('');
        }
    });

    var ProductScrollView = Backbone.View.extend({
        tagName: 'table',
        className: 'scrollWindow',
        initialize: function () {
            _.bindAll(this, 'render');
        },
        template: _.template($('#product-fav-template')
            .html()),
        render: function () {
            $(this.el).empty();
            $(this.el).html(
            this.template(this.model.toJSON()));
            return this;
        }
    });

    var prScCo = new ProductScrollCollection();
  /*  if (prScCo.add({
        "favItem": [{
            "id": 1,
            "label": "Nutrition",
            "img": "1.jpg"
        }, {
            "id": 2,
            "label": "Sports & Fitness",
            "img": "2.jpg"
        }, {
            "id": 3,
            "label": "Diabetes",
            "img": "3.jpg"
        }, {
            "id": 4,
            "label": "Home Devices",
            "img": "4.jpg"
        }, {
            "id": 5,
            "label": "Eye",
            "img": "5.jpg"
        }, {
            "id": 6,
            "label": "Personal Care",
            "img": "6.jpg"
        }, {
            "id": 7,
            "label": "Beauty",
            "img": "7.jpg"
        }, {
            "id": 8,
            "label": "Parenting",
            "img": "8.jpg"
        }, {
            "id": 9,
            "label": "Services",
            "img": "9.jpg"
        }]
    })) {
        $('.scrollWindow').scrollWindow({
            scrollFactor: 35
        });
    }
*/
    /** Backbone code for product list*S */
    var ProductModel = Backbone.Model.extend({
        initialize: function () {
            _.bindAll(this, 'render');
            this.render();
        },
        render: function () {
            var prVi = new ProductView({
                model: this
            });
            $('#productList ul').append(prVi.render().el);
        }
    });

    var ProductCollection = Backbone.Collection.extend({
        model: ProductModel,
        initialize: function () {
            this.on('reset', this.clearView, this);
            this.clearView();
        },
        clearView: function () {
            var par = getURLParameterValue(queryString, 'secondaryCategory');
            if (par == '' || par == null) par = 'Search Result';
            $('#productTitle').html(par);
            $('#productList ul').html('');
        }
    });

    var ProductView = Backbone.View.extend({
        tagName: 'li',
        initialize: function () {
            _.bindAll(this, 'render');

        },
        template: _.template($('#product-list-template').html()),
        render: function () {
            $(this.el).empty();
            $(this.el).html(
            this.template(this.model.toJSON()));
            $(this.el).attr('data-icon', 'arr');
            return this;
        }
    });

    loadingPop('s', '');

    var prCo = new ProductCollection();
    prCo.reset();

    var temp = queryString;
    var URL;

    temp = temp.replace('?', '');

    if (temp.indexOf('query') == 0) {
        URL = wSURL + __hkG.urls.search + queryString;
    } else {
        URL = wSURL + __hkG.urls.catalog + queryString;
    }

    function getProductList() {
        var perPage = $('#productList').attr('data-perPage');
        var pageNo = $('#productList').attr('data-pageNo');
        var hasMore = $('#productList').attr('data-more');
        var dataRequest = $('#productList').attr('data-request');
        if (hasMore == 'true' && dataRequest == 'free') {
            $('#productList').attr('data-request', 'processing');
            loadingPop('s', '');
            $.ajax({
                url: URL + '&pageNo=' + pageNo + '&perPage=' + perPage,
                dataType: 'json',
                timeout: '15000',
                success: function (response) {
                    if (hasErr(response)) {
                        loadingPop('h');
                        alert(getErr(response.message));
                        $('#productList').attr('data-request', 'free');
                    } else {
                        if (response.data.length == 0 || response.data == null) {
                            $('#productList ul').html('<h3 style="padding-left:20px">No Data Found!</h3>');
                        } else {
                            if (prCo.add(response.data.data)) {
                                $('#productList ul').listview();
                                $('#productList ul').listview('refresh');
                            }
                        }

                        $('#productList').attr('data-pageNo',
                        Number(pageNo) + 1);
                        $('#productList').attr('data-more',
                        response.data.hasMore);
                        $('#productList').attr('data-request', 'free');
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

    getProductList();

    $(window).scroll(function () {
        if ($.mobile.activePage.attr('id') == 'product') {
            var currentScrollTop = $(window)
                .scrollTop();
            var windowHeight = $(window)
                .height();
            var documentHeight = $('#productList').height() + 160;

            if (documentHeight < (currentScrollTop + windowHeight)) {

                getProductList();

            }
        }
    });
loadingPop('s', '');
    /**Backbone code for product list*E*/
});
$('#product').bind('pageshow',function(){loadingPop('s','');});