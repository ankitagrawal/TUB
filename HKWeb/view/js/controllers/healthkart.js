require('js/templates');

HK.CartOfferController = Ember.Controller.create({
    finalApplicableOffers:[],
    array:[],
    totalOffers:[],
    isOffer:null,
    showOfferText:false,
    showOfferFlag:false,
    showCoupon:false,
    isHKUser:false,
    isTempUser:false,
    isHKUnverified:false,
    applicableOffers:[],
    roles:[],
    currentlyAppliedOffer:[],
    couponCode:"",
    showModal:false,
    modalData:null,
    showErrAlreadyApplied:false,
    errorMessage:"",
    showApply:true,
    showRemove:false,
    showRemoveButton: false,
    verifyActionURL: HK.contextPath + "/core/user/MyAccount.action",
    loginURL: HK.contextPath + "/core/auth/Login.action",
    cartURL: HK.contextPath + "/core/cart/Cart.action",
    loginWithRedirectURL: HK.contextPath + "/core/auth/Login.action" + "?redirectUrl=" + HK.contextPath + "/core/cart/Cart.action",
    applyURL: HK.contextPath + "/core/discount/ApplyCoupon.action",
    imageURL: HK.contextPath + "/images/close.png",
    init:function(){
        this.getRoles();
        this.getOffer();
    },

    getRoles:function(){
        var self = this;
        $.ajax({
            url: HK.contextPath + "/rest/api/cartResource/roles",
            success: function ( data ) {
                data.roles.forEach(function(role){
                    self.get("roles").pushObject(role.name);
                });
                if($.inArray("COUPON_BLOCKED", self.get("roles"))> -1){
                    self.set("showCoupon", false);
                }
                else{
                    self.set("showCoupon", true);
                }

                if($.inArray("HK_USER", self.get("roles"))> -1){
                    self.set("isHKUser", true);
                }
                else{
                    self.set("isHKUser", false);
                }

                if($.inArray("TEMP_USER", self.get("roles"))> -1){
                    self.set("isTempUser", true);
                }
                else{
                    self.set("isTempUser", false);
                }

                if($.inArray("HKUNVERIFIED", self.get("roles"))> -1){
                    self.set("isHKUnverified", true);
                }
                else{
                    self.set("isHKUnverified", false);
                }
            }
        });
    },

    removeOffer:function(context){
        $("#" + context).click();
    },

    scrollToOffer:function(){
        $('html, body').animate({scrollTop: $(".products_container").height() + 200}, 1000);
    },

    getOffer:function(){
        var self = this,
            tempArray;
        $.ajax({
            url: HK.contextPath + "/rest/api/cartResource/otherApplicableOffers",

            success: function ( data ) {
                tempArray = [];
                self.set("applicableOffers",[]);
                self.set("finalApplicableOffers",[]);
                self.set("array",[]);
                self.set("totalOffers",[]);
                self.set("currentlyAppliedOffer",[]);
                data.applicableOffers.forEach(function(offer){
                    self.get("totalOffers").pushObject(Ember.Object.create(offer));
                    self.get("applicableOffers").pushObject(Ember.Object.create(offer));
                });
                
                self.get("applicableOffers").forEach(function(offer){
                    offer.set("removeFlag",false);
                });
                var num = self.get("applicableOffers").length %3;
                if(num >0){
                    for(var i = 0; i< num; i++){
                        self.get("array").pushObject(self.get("applicableOffers").popObject());
                    }
                }
                var count = 0;
                var dummyArray = [];
                self.get("applicableOffers").forEach(function(offer){
                    dummyArray.pushObject(offer);
                    count++;
                    if(count%3 == 0){
                        self.get("finalApplicableOffers").pushObject(dummyArray);
                        dummyArray = [];
                    }
                });
                self.get("finalApplicableOffers").pushObject(self.get("array"));

                if(!Ember.empty(data.appliedOffer)){                    
                    self.get("finalApplicableOffers").forEach(function(array){
                        array.forEach(function(offer){
                            if(offer.id === parseInt(data.appliedOffer.id)){
                            if(self.get("currentlyAppliedOffer").length === 0){
                                self.get("currentlyAppliedOffer").pushObject(offer);
                            }
                            offer.set("removeFlag",true);
                        }
                        else{
                            offer.set("removeFlag",false);
                        }
                        });
                    });                    
                }
                if(self.get("totalOffers").length > 0){
                    $("#appOfferID").show();
                    self.set("showOfferText", true);
                }
                else{
                    $("#appOfferID").hide();
                    self.set("showOfferText", false);
                }
            }
        });
    }
});

HK.AppliedOfferView = Ember.View.create({
    templateName:"templates/appliedOffers",
    controllerBinding:"HK.CartOfferController"
});

HK.CartOfferView = Ember.View.create({
    templateName:"templates/cart",
    controllerBinding:"HK.CartOfferController",
    isVisibleBinding:"HK.CartOfferController.showCoupon"
});

HK.OfferTopTextView = Ember.View.create({
    templateName:"templates/offerTopText",
    controllerBinding:"HK.CartOfferController"
});

HK.CartOfferView.appendTo('#applicableOfferDiv');
HK.AppliedOfferView.appendTo('#appliedOfferDiv');
HK.OfferTopTextView.appendTo('#offerTextOnTop');
