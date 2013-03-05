(function() {

Ember.TEMPLATES["templates/appliedOffers"] = Ember.Handlebars.template(function anonymous(Handlebars,depth0,helpers,partials,data) {
this.compilerInfo = [2,'>= 1.0.0-rc.3'];
helpers = helpers || Ember.Handlebars.helpers; data = data || {};
  var stack1, hashTypes, escapeExpression=this.escapeExpression, self=this;

function program1(depth0,data) {
  
  var buffer = '', hashTypes;
  data.buffer.push("\n  <div class=\"appliedOffer\">\n    <div class=\"appliedOfferHead\">Currently Applied Offer</div>\n    <div class=\"appliedOfferTitle\">");
  hashTypes = {};
  data.buffer.push(escapeExpression(helpers._triageMustache.call(depth0, "value.description", {hash:{},contexts:[depth0],types:["ID"],hashTypes:hashTypes,data:data})));
  data.buffer.push("\n        <div id=\"couponPopUp\" class=\"couponPopUp\">\n          <div class=\"popupArrow\"></div>\n          <div class=\"couponPopUpMsg\">");
  hashTypes = {};
  data.buffer.push(escapeExpression(helpers._triageMustache.call(depth0, "value.description", {hash:{},contexts:[depth0],types:["ID"],hashTypes:hashTypes,data:data})));
  data.buffer.push("</div>\n          <div class=\"couponPopUpTerms\"><strong>Terms:</strong> ");
  hashTypes = {};
  data.buffer.push(escapeExpression(helpers._triageMustache.call(depth0, "value.terms", {hash:{},contexts:[depth0],types:["ID"],hashTypes:hashTypes,data:data})));
  data.buffer.push("</div>\n          <div class=\"couponPopUpDate\">Valid till: ");
  hashTypes = {};
  data.buffer.push(escapeExpression(helpers._triageMustache.call(depth0, "value.endDate", {hash:{},contexts:[depth0],types:["ID"],hashTypes:hashTypes,data:data})));
  data.buffer.push("</div>\n        </div>\n        <a class=\"appliedOfferDetails\" onclick=\"showCouponDetails()\">[show details]</a>\n        <a class=\"appliedOfferDetails\" style=\"display: none;\" onclick=\"showCouponDetails()\">[hide details]</a>\n    </div>\n    <form action=\"/healthkart/core/discount/ApplyCoupon.action\" method=\"post\">\n        <input name=\"offer\" ");
  hashTypes = {'value': "STRING"};
  data.buffer.push(escapeExpression(helpers.bindAttr.call(depth0, {hash:{
    'value': ("value.id")
  },contexts:[],types:[],hashTypes:hashTypes,data:data})));
  data.buffer.push(" type=\"hidden\">        \n        <input name=\"removeOffer\" value=\"Remove\" class=\"button_raj\" style=\"float: right;left: 0px;margin-bottom: 0px;bottom: 8px;\" type=\"submit\">\n        <div style=\"display: none;\">\n          <input type=\"hidden\" name=\"_sourcePage\" value=\"nO5Kai0iUtU9VJRiFjXm-vAFsFkyxchJ\">\n          <input type=\"hidden\" name=\"__fp\" value=\"CATsOzMhS2E=\">\n        </div>\n    </form>    \n  </div>  \n");
  return buffer;
  }

  hashTypes = {};
  stack1 = helpers.each.call(depth0, "value", "in", "controller.currentlyAppliedOffer", {hash:{},inverse:self.noop,fn:self.program(1, program1, data),contexts:[depth0,depth0,depth0],types:["ID","ID","ID"],hashTypes:hashTypes,data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  else { data.buffer.push(''); }
  
});

Ember.TEMPLATES["templates/cart"] = Ember.Handlebars.template(function anonymous(Handlebars,depth0,helpers,partials,data) {
this.compilerInfo = [2,'>= 1.0.0-rc.3'];
helpers = helpers || Ember.Handlebars.helpers; data = data || {};
  var stack1, hashTypes, escapeExpression=this.escapeExpression, self=this;

function program1(depth0,data) {
  
  var buffer = '', stack1, hashTypes;
  data.buffer.push("\n  <div class=\"applicableOfferHead\">Other Applicable Offers!</div>\n  <div class=\"offerCount\">");
  hashTypes = {};
  data.buffer.push(escapeExpression(helpers._triageMustache.call(depth0, "controller.applicableOffers.length", {hash:{},contexts:[depth0],types:["ID"],hashTypes:hashTypes,data:data})));
  data.buffer.push("</div>\n  <hr noshade=\"\" size=\"1\" width=\"95%\" style=\"border-color: #ddd;\">\n  ");
  hashTypes = {};
  stack1 = helpers.each.call(depth0, "value", "in", "controller.applicableOffers", {hash:{},inverse:self.noop,fn:self.program(2, program2, data),contexts:[depth0,depth0,depth0],types:["ID","ID","ID"],hashTypes:hashTypes,data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n");
  return buffer;
  }
function program2(depth0,data) {
  
  var buffer = '', stack1, hashTypes;
  data.buffer.push("\n    <div class=\"applicableOffer\">    \n      <div class=\"applicableOfferDesc\">");
  hashTypes = {};
  data.buffer.push(escapeExpression(helpers._triageMustache.call(depth0, "value.description", {hash:{},contexts:[depth0],types:["ID"],hashTypes:hashTypes,data:data})));
  data.buffer.push("</div>\n      <form action=\"/healthkart/core/discount/ApplyCoupon.action\" method=\"post\">\n        <input name=\"offer\" ");
  hashTypes = {'value': "STRING"};
  data.buffer.push(escapeExpression(helpers.bindAttr.call(depth0, {hash:{
    'value': ("value.id")
  },contexts:[],types:[],hashTypes:hashTypes,data:data})));
  data.buffer.push(" type=\"hidden\">        \n        <input name=\"applyOffer\" value=\"Apply\" class=\"button_raj\" type=\"submit\">\n        <div style=\"display: none;\">\n          <input type=\"hidden\" name=\"_sourcePage\" value=\"nO5Kai0iUtU9VJRiFjXm-vAFsFkyxchJ\">\n          <input type=\"hidden\" name=\"__fp\" value=\"CATsOzMhS2E=\">\n        </div>\n      </form>\n      ");
  hashTypes = {};
  stack1 = helpers['if'].call(depth0, "value.terms", {hash:{},inverse:self.noop,fn:self.program(3, program3, data),contexts:[depth0],types:["ID"],hashTypes:hashTypes,data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n      <div class=\"endDate\">Valid till: ");
  hashTypes = {};
  data.buffer.push(escapeExpression(helpers._triageMustache.call(depth0, "value.endDate", {hash:{},contexts:[depth0],types:["ID"],hashTypes:hashTypes,data:data})));
  data.buffer.push("</div>        \n    </div>\n  ");
  return buffer;
  }
function program3(depth0,data) {
  
  var buffer = '', hashTypes;
  data.buffer.push("\n        <div class=\"applicableOfferTerms\"><strong>Terms:</strong>");
  hashTypes = {};
  data.buffer.push(escapeExpression(helpers._triageMustache.call(depth0, "value.terms", {hash:{},contexts:[depth0],types:["ID"],hashTypes:hashTypes,data:data})));
  data.buffer.push("</div>\n      ");
  return buffer;
  }

  hashTypes = {'class': "STRING",'controllerBinding': "STRING",'isVisibleBinding': "STRING"};
  stack1 = helpers.view.call(depth0, {hash:{
    'class': ("applicableOfferContainer"),
    'controllerBinding': ("controller"),
    'isVisibleBinding': ("HK.CartOfferController.showOfferFlag")
  },inverse:self.noop,fn:self.program(1, program1, data),contexts:[],types:[],hashTypes:hashTypes,data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  else { data.buffer.push(''); }
  
});

Ember.TEMPLATES["templates/couponModal"] = Ember.Handlebars.template(function anonymous(Handlebars,depth0,helpers,partials,data) {
this.compilerInfo = [2,'>= 1.0.0-rc.3'];
helpers = helpers || Ember.Handlebars.helpers; data = data || {};
  var buffer = '', stack1, hashTypes, escapeExpression=this.escapeExpression, self=this;

function program1(depth0,data) {
  
  var buffer = '', stack1, hashTypes;
  data.buffer.push("\n    <script type=\"text/javascript\">\n      // call pricing update\n      $.getJSON($('#updatePricingLink').attr('href'), function(res) {_updateTotals(res);});\n    </script>\n    <table class=\"cont_2 offerTable\">\n      <thead>\n      <tr>\n        <th>Offer Description</th>\n        <th>Valid till</th>\n        <th>Coupon</th>\n      </tr>\n      </thead>\n      <tr>\n        <td width=\"200\">\n          ");
  hashTypes = {};
  data.buffer.push(escapeExpression(helpers._triageMustache.call(depth0, "controller.modalData.offer.description", {hash:{},contexts:[depth0],types:["ID"],hashTypes:hashTypes,data:data})));
  data.buffer.push("\n          <br/>\n          ");
  hashTypes = {};
  stack1 = helpers['if'].call(depth0, "controller.modalData.offer.terms", {hash:{},inverse:self.noop,fn:self.program(2, program2, data),contexts:[depth0],types:["ID"],hashTypes:hashTypes,data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n        </td>\n        <td width=\"150\">");
  hashTypes = {};
  data.buffer.push(escapeExpression(helpers._triageMustache.call(depth0, "controller.modalData.offer.endDate", {hash:{},contexts:[depth0],types:["ID"],hashTypes:hashTypes,data:data})));
  data.buffer.push("</td>\n        <td width=\"100\">\n          <span class=\"upc gry sml\">");
  hashTypes = {};
  data.buffer.push(escapeExpression(helpers._triageMustache.call(depth0, "controller.modalData.coupon.code", {hash:{},contexts:[depth0],types:["ID"],hashTypes:hashTypes,data:data})));
  data.buffer.push("</span>\n        </td>\n      </tr>\n    </table>\n  ");
  return buffer;
  }
function program2(depth0,data) {
  
  var buffer = '', hashTypes;
  data.buffer.push("\n            <h3>Terms:</h3>\n            ");
  hashTypes = {};
  data.buffer.push(escapeExpression(helpers._triageMustache.call(depth0, "controller.modalData.offer.terms", {hash:{},contexts:[depth0],types:["ID"],hashTypes:hashTypes,data:data})));
  data.buffer.push("\n          ");
  return buffer;
  }

function program4(depth0,data) {
  
  var buffer = '', stack1, hashTypes;
  data.buffer.push("\n\n    ");
  hashTypes = {'controllerBinding': "STRING",'isVisibleBinding': "STRING"};
  stack1 = helpers.view.call(depth0, {hash:{
    'controllerBinding': ("controller"),
    'isVisibleBinding': ("HK.CartOfferController.showErrAlreadyApplied")
  },inverse:self.noop,fn:self.program(5, program5, data),contexts:[],types:[],hashTypes:hashTypes,data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n\n    ");
  hashTypes = {};
  data.buffer.push(escapeExpression(helpers._triageMustache.call(depth0, "controller.errorMessage", {hash:{},contexts:[depth0],types:["ID"],hashTypes:hashTypes,data:data})));
  data.buffer.push("\n  ");
  return buffer;
  }
function program5(depth0,data) {
  
  var buffer = '', stack1, hashTypes;
  data.buffer.push("\n      The offer associated with this coupon has now been selected. <br/>\n      <script type=\"text/javascript\">\n      // call pricing update\n      $.getJSON($('#updatePricingLink').attr('href'), function(res) {_updateTotals(res);});\n      </script>\n      <table class=\"cont_2 offerTable\">\n        <thead>\n        <tr>\n          <th>Offer Description</th>\n          <th>Valid till</th>\n          <th>Coupon</th>\n        </tr>\n        </thead>\n        <tr>\n          <td width=\"200\">\n            ");
  hashTypes = {};
  data.buffer.push(escapeExpression(helpers._triageMustache.call(depth0, "controller.modalData.offer.description", {hash:{},contexts:[depth0],types:["ID"],hashTypes:hashTypes,data:data})));
  data.buffer.push("\n            <br/>\n            ");
  hashTypes = {};
  stack1 = helpers['if'].call(depth0, "controller.modalData.offer.terms", {hash:{},inverse:self.noop,fn:self.program(6, program6, data),contexts:[depth0],types:["ID"],hashTypes:hashTypes,data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n          </td>\n          <td width=\"150\">");
  hashTypes = {};
  data.buffer.push(escapeExpression(helpers._triageMustache.call(depth0, "controller.modalData.offer.endDate", {hash:{},contexts:[depth0],types:["ID"],hashTypes:hashTypes,data:data})));
  data.buffer.push("</td>\n          <td width=\"100\">\n            <span class=\"upc gry sml\">");
  hashTypes = {};
  data.buffer.push(escapeExpression(helpers._triageMustache.call(depth0, "controller.modalData.coupon.code", {hash:{},contexts:[depth0],types:["ID"],hashTypes:hashTypes,data:data})));
  data.buffer.push("</span>\n          </td>\n        </tr>\n      </table>\n      You can see all your offers by clicking on the \"Select Available Offers\" link.\n    ");
  return buffer;
  }
function program6(depth0,data) {
  
  var buffer = '', hashTypes;
  data.buffer.push("\n              <h3>Terms:</h3>\n              ");
  hashTypes = {};
  data.buffer.push(escapeExpression(helpers._triageMustache.call(depth0, "controller.modalData.offer.terms", {hash:{},contexts:[depth0],types:["ID"],hashTypes:hashTypes,data:data})));
  data.buffer.push("\n            ");
  return buffer;
  }

  data.buffer.push("<div class=\"modal_header\">\n    <div class=\"jqDrag\" style=\"padding: 5px 3px;\">Apply Coupon</div>\n    <a href=\"#\" id=\"couponModalClose\" class=\"modal_close jqmClose\" style=\" margin-top: 0px;\"><img src=\"/healthkart/images/spacer.gif\" width=\"21\" height=\"21\"/></a>\n</div>\n<div class=\"modal_content\">\n  <p class=\"lrg em\">");
  hashTypes = {};
  data.buffer.push(escapeExpression(helpers._triageMustache.call(depth0, "controller.modalData.message", {hash:{},contexts:[depth0],types:["ID"],hashTypes:hashTypes,data:data})));
  data.buffer.push("</p>\n  ");
  hashTypes = {};
  stack1 = helpers['if'].call(depth0, "controller.modalData.success", {hash:{},inverse:self.noop,fn:self.program(1, program1, data),contexts:[depth0],types:["ID"],hashTypes:hashTypes,data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n  ");
  hashTypes = {};
  stack1 = helpers['if'].call(depth0, "controller.modalData.error", {hash:{},inverse:self.noop,fn:self.program(4, program4, data),contexts:[depth0],types:["ID"],hashTypes:hashTypes,data:data});
  if(stack1 || stack1 === 0) { data.buffer.push(stack1); }
  data.buffer.push("\n</div>");
  return buffer;
  
});

})();

(function() {

HK.CartOfferController = Ember.Controller.create({
    isOffer:null,
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
    init:function(){
        this.getRoles();
        this.getOffer();
    },

    getRoles:function(){
        self = this;
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

                if($.inArray("HK_UNVERIFIED", self.get("roles"))> -1){
                    self.set("isHKUnverified", true);
                }
                else{
                    self.set("isHKUnverified", false);
                }
            }
        });
    },
    getOffer:function(){
        self = this;
        self.get("applicableOffers").clear();
        tempArray = [],
        $.ajax({
            url: HK.contextPath + "/rest/api/cartResource/otherApplicableOffers",
            success: function ( data ) {                
                if(!Ember.empty(data.appliedOffer)){
                    if(data.applicableOffers.length === 0){
                        if(self.get("currentlyAppliedOffer").length === 0){
                            self.get("currentlyAppliedOffer").pushObject(data.appliedOffer);
                        }
                    }
                    data.applicableOffers.forEach(function(offer){
                        self.get("currentlyAppliedOffer").clear();
                        if(offer.id !== parseInt(data.appliedOffer.id)){
                            self.get("currentlyAppliedOffer").pushObject(data.appliedOffer);
                        }
                    });
                    data.applicableOffers.forEach(function(offer){
                        if(offer.id === parseInt(data.appliedOffer.id)){
                            if(self.get("currentlyAppliedOffer").length === 0){
                                self.get("currentlyAppliedOffer").pushObject(offer);
                            }                            
                        }
                        else{
                            self.get("applicableOffers").pushObject(offer);
                        }
                    });                    
                }
                else{
                    self.set("applicableOffers",data.applicableOffers);
                }
                if(self.get("applicableOffers").length > 0){
                    self.set("showOfferFlag", true);
                }
                else{
                    self.set("showOfferFlag", false);
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

HK.CartOfferView.appendTo('#applicableOfferDiv');
HK.AppliedOfferView.appendTo('#appliedOfferDiv');


})();