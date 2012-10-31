$('#address').bind('pagebeforeshow', function () {

    jQuery.support.cors = true;
    
    $("a.addAddressToOrder").live("click", (function (e) {
        authenticateAddress2("addressId=" + $(this).attr("addrId"));
    }));
    
    _.templateSettings = {
        evaluate: /\{\{(.+?)\}\}/g
    };

    Backbone.emulateJSON = true;

    var AddressModel = Backbone.Model.extend({
        initialize: function () {
            this.render();
        },
        render: function () {
            var adVi = new AddressView({
                model: this
            });
            
            $("#address-block").append(adVi.render().el);
        }
    });
    
    var AddressCollection = Backbone.Collection.extend({
        model: AddressModel,
        initialize: function () {
            this.clearView();
            this.on('reset', this.clearView(), this);
        },
        clearView: function () {
            $("#address-block").html('');
        }
    });
    
    var AddressView = Backbone.View.extend({
        tagName: 'ul',
        template: _.template($('#address-view-template').html()),
        //className: 'savedAddressView',
        initialize: function () {
            _.bindAll(this, 'render');
        },
        render: function () {
            $(this.el).empty();
            $(this.el).html(this.template(this.model.toJSON()));
            return this;
        }
    });

    var addrCol = new AddressCollection();

    $.ajax({
        url: wSURL + __hkG.urls.address,
        dataType: 'json',
        success: function (response) {
            if (hasErr(response)) {
                loadingPop('h');
                $('#address-block').html('<h3 style="padding-left:20px">' + getErr(response.message) + '</h3>');
            } else {
                if (addrCol.add(response)) {
                    //$('#address-block ul').listview();
                }

                loadingPop('h');
            }
        },
        error: function () {
            popUpMob.show('Request failed');
            loadingPop('h');
        }

    });

    $('.gNav').click(function (e) {
        var ele = e.currentTarget;
        var eleId = $(ele).attr('id');
        
        $('.viewContent').hide();
        $('#' + eleId + 'Content').show();
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
        message: "Please enter Address Line 1",
    });

    $("#address_pin").validate({
        expression: "if (VAL==''||VAL.length>6 || !VAL.match(/^[0-9]*$/)) return false; else return true;",
        message: "Please enter valid PIN",
    });
    
    $("#address_phone").validate({
        expression: "if (VAL==''||!VAL.match(/^[0-9]*$/)) return false; else return true;",
        message: "Please Enter correct Phone Number",
    });

    $('#address_registration').validated(function () {
        $('.loaderContainer').show();
        authenticateAddress2(null);
        return false;
    });

    function addToOrder(addrId) {
        authenticateAddress2("addressId=" + addrId);
    }

    function authenticateAddress2(addrId) {
        var path = $('#address_registration').attr('action');
        var data = $('#address_registration').serialize();
        if (addrId != null) {
            data = addrId;
        }
        $.ajax({
            url: wSURL + path + "?" + data,
            type: 'get',
            dataType: 'json',
            async: false,
            success: function (data) {
                if (hasErr(data)) {
                	console.log(data.message);
                    popUpMob.show(data.message);
                } else {
                    setTimeout(function () {
                        location.href = httpPath+"/orderSummary.jsp"
                    }, 500);

                }
            },

            error: function (e) {
                popUpMob.show("Request Failed");
            }
        });

    }

});
$('#address').bind('pageshow',function(){loadingPop('s','');});