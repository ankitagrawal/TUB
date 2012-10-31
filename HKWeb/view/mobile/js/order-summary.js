$('#orderSummary').bind('pagebeforeshow', function () {

    jQuery.support.cors = true;

    _.templateSettings = {
        evaluate: /\{\{(.+?)\}\}/g
    };

    Backbone.emulateJSON = true;

    var OrderSummary = Backbone.Model.extend({
        initialize: function () {
            this.render();
        },
        render: function () {
            var adVi = new OrderSummaryView({
                model: this
            });
            $("#summary-address-block").append(adVi.render().el);

        }
    });
    
    var OrderSummaryCollection = Backbone.Collection.extend({
        model: OrderSummary,
        initialize: function () {
            this.clearView();
            this.on('reset', this.clearView(), this);
        },
        clearView: function () {
            $("#summary-address-block").html('');
        }
    });
    
    var OrderSummaryView = Backbone.View.extend({
        tagName: 'ul',
        template: _.template($('#summary-view-template').html()),
        initialize: function () {
            _.bindAll(this, 'render');
        },

        render: function () {
            $(this.el).empty();
            $(this.el).html(this.template(this.model.toJSON()));
            return this;
        }

    });

    var addrCol = new OrderSummaryCollection();

    $.ajax({
        url: wSURL + __hkG.urls.orderSummary,
        dataType: 'json',
        async: false,
        success: function (response) {
            if (hasErr(response)) {
                loadingPop('h');
                popUpMob.show(getErr(response.message));
            } else {
                if (addrCol.add(response)) {
                    $('#summary-address-block').listview();
                }

                loadingPop('h');
            }
        },
        error: function () {
            popUpMob.show(__hkG.errs.requestFail);
            loadingPop('h');
        }

    });

    $('#orderSummary_makepayment').click(function () {
        var txt = $("#orderSummary_comments").val();
        $.ajax({
            url: wSURL + __hkG.urls.payment + "?comment=" + txt,
            type: 'post',
            dataType: 'json',
            data: $("#order_makeapayment").serialize(),
            async: false,
            success: function (data) {
                if (hasErr(data)) {
                    popUpMob.show(data.message);
                    if ((data.message).toLowerCase() == 'cod is not available for this location') {
                        setTimeout(function () {
                            location.href = httpPath + __hkG.urls.addressJ
                        }, 800);
                    }

                } else {
                    setTimeout(function () {
                        location.href = httpPath + __hkG.urls.orderSuccessJ + '?orderId=' + data.data.gatewayOrderId
                    }, 500);
                }
            },

            error: function (e) {
                popUpMob.show(__hkG.errs.requestFail);
            }
        });
        return false;
    });
    
});