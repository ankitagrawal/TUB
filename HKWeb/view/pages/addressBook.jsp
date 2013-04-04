<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>


<s:useActionBean beanclass="com.hk.web.action.core.user.SelectAddressAction" event="pre" var="addressBean"/>
<%--<s:useActionBean beanclass="com.hk.web.action.core.cart.CartAction" var="cartAction" event="pre"/>--%>

<s:layout-render name="/layouts/checkoutLayout.jsp" pageTitle="Select a shipping address">
<%@ include file="/layouts/_userData.jsp" %>
<%
  boolean isSecure = pageContext.getRequest().isSecure();
  pageContext.setAttribute("isSecure", isSecure);
%>
<c:set var="countryId" value="80"/>
<s:layout-component name="htmlHead">
    <script type="text/javascript">
        var noAddress = false;

        $(document).ready(function() {
            if(${addressBean.printAlert}){
                alert("We don't service to this pincode, please enter valid pincode or Call Customer Care!!!!");
            }
            // selected address boxes should have a different bg-color
            $('input.cbox').click(addressColor).each(addressColor);

            $('#guestEmail').labelify({labelledClass: 'input_tip'});

        });
        function _closeAddress(hash) {
            hash.o.fadeOut();
            hash.w.hide();
            hash.w.html('<h1>Loading..</h1>');
        }

        function addressColor() {
            if ($(this).attr('checked')) {
                $(this).parents('tr').addClass('item_selected');
            } else {
                $(this).parents('tr').removeClass('item_selected');
            }
        }
    </script>
    <%-- use attribute selectors to apply a specify styles  --%>
    <style type="text/css">
        select.error {
            background: none repeat scroll 0 0 #FFFAFA;
            border: 1px solid #CC0000;
        }
    </style>
</s:layout-component>

<s:layout-component name="modal">
    <div class="jqmWindow" id="jqmWindowNewAddress"><h1>Loading..</h1></div>
</s:layout-component>

<s:layout-component name="steps">
    <div class='logoBox' style="z-index: 50;float:left;top: 50px; left: 12px;position: relative;">
        <s:link href="/" title='go to healthkart home'>
            <img src='<hk:vhostImage/>/images/logo.png' alt="healthkart logo"/>
        </s:link>
    </div>
    <div class='steps'>
        <hr noshade class="stepLine">
        <div class='newStep '>
            <div class="newStepCount current_step">1</div>
            <div class='newStepText'>
                Select A shipping address
            </div>
        </div>
        <div class='newStep'>
            <div class="newStepCount">2</div>

            <div class='newStepText'>
                Confirm your order
            </div>
        </div>
        <div class='newStep'>
            <div class="newStepCount">3</div>

            <div class='newStepText'>
                Choose Payment Method
            </div>
        </div>
        <div class='newStep' style="margin-left: 28px;">
            <div class="newStepCount">4</div>

            <div class='newStepText'>
                Completed !
            </div>
        </div>
    </div>
</s:layout-component>
<s:layout-component name="steps_content">
    <div class='current_step_content'>

            <%--<h3 style="text-align: center; margin-bottom: 20px; color: #888;">
              What address should we ship your order to?
            </h3>--%>

        <div class='left2'>
            <h3 style="text-align: left;">
                Use one of your saved addresses &darr;
            </h3>

            <c:forEach items="${addressBean.addresses}" var="address" varStatus="addressCount">
                <s:link beanclass="com.hk.web.action.core.user.SelectAddressAction" event="checkout" title="Click to use this address and proceed">
                    <s:param name="selectedAddress" value="${address.id}"/>
                    <div class="address" style="position: relative;">
                        <h5 class="name">${address.name}</h5>
                        <div class='street street1'>${address.line1}</div>
                        <c:if test="${hk:isNotBlank(address.line2)}">
                            <div class="street street2">${address.line2}</div>
                        </c:if>
                        <div class='city'>${address.city}</div>
                        <div class='state'>${address.state}</div>
                        <div class='pin'>${address.pincode.pincode}</div>
                        <div class='phone'>${address.phone}</div>
                        <br/>
                        <s:link beanclass="com.hk.web.action.core.user.SelectAddressAction" event="remove" class="delete">
                            <s:param name="deleteAddress" value="${address.id}"/>
                            (delete)
                        </s:link>

                        <span class="edit">(edit)</span>

                        <div class="hidden help yellow">
                            Click to use this address
                            and proceed &rarr;

                        </div>
                    </div>
                </s:link>
            </c:forEach>
            <script type="text/javascript">
                $(document).ready(function() {
                    var bool = false;
                    $('.edit').click(function() {
                        form = $('#newAddressForm');
                        addressBlock = $(this).parents('.address');
                        name = addressBlock.find('.name').text();
                        street1 = addressBlock.find('.street1').text();
                        street2 = addressBlock.find('.street2').text();
                        city = addressBlock.find('.city').text();
                        state = addressBlock.find('.state').text();
                        pin = addressBlock.find('.pin').text();
                        phone = addressBlock.find('.phone').text();
                        id = addressBlock.find('.address_id').val();
                        form.find("input[type='text'][name='address.name']").val(name);
                        form.find("input[type='text'][name='address.line1']").val(street1);
                        if (street2) {
                            form.find("input[type='text'][name='address.line2']").val(street2);
                        }
                        form.find("input[type='text'][name='address.city']").val(city);
                        //          form.find("input[type='text'][name='address.state']").val(state);
                        form.find("[name='address.state']").val(state.toUpperCase());
                        form.find("input[type='text'][name='address.pincode']").val(pin);
                        form.find("input[type='text'][name='address.phone']").val(phone);
                        form.find("input[type='hidden'][name='address.id']").val(id);
                    });
                    $('.delete').click(function(){
                        if (confirm('Are you sure you want to delete this address?')) {
                            bool = true;
                            return true;
                        } else {
                            return false;
                        }
                    });

                    $('.address').hover(
                            function() {
                                $(this).children('.hidden').slideDown(100);
                                $(this).children('.edit').click(function() {
                                    return false;
                                });
                                $(this).children('.delete').click(function() {
                                    if(bool) return true;
                                    else
                                        return false;
                                });
                            },
                            function() {
                                $(this).children('.hidden').slideUp(50);
                            }
                            );
                    $('.address').click(function() {
                        var add_url = $(this).children('a').attr('href');
                        document.location.href = add_url;
                    });
                });

            </script>


        </div>
        <div class='or'>
            or
        </div>
                <h3 class="shippingAddressheading">
                    Add a new shipping address
                </h3>
        <div class='right' style="width: 440px;background: initial;">


            <div class="addressContainer shipping_address" style="left: 5px;">
                <s:form beanclass="com.hk.web.action.core.user.NewAddressAction" id="newAddressForm" style="width:425px;" >
                    <s:layout-render name="/layouts/addressLayout.jsp" />
                    <s:hidden name="countryId" value="${countryId}"/>
                    <s:submit name="create" value="Use this address and continue" style="margin:15px 0 0 26px;width:260px;position: static;" class="button"/>
                </s:form>
            </div>

            <div class="addressSuccessContainer" style="display:none;">
                <h2>New address has been added</h2>

                <p>Reloading the address book..</p>
            </div>
        </div>
    </div>
    <c:if test="${not isSecure }">
        <iframe src="" id="vizuryTargeting" scrolling="no" width="1"
                height="1" marginheight="0" marginwidth="0" frameborder="0"></iframe>


        <script type="text/javascript">
            var vizuryLink = "http://www.vizury.com/analyze/analyze.php?account_id=VIZVRM112&param=e400";
            var user_hash;
            <c:forEach items="${addressBean.order.productCartLineItems}" var="cartLineItem" varStatus="liCtr">
            vizuryLink += "&pid${liCtr.count}=${cartLineItem.productVariant.product.id}&catid${liCtr.count}=${cartLineItem.productVariant.product.primaryCategory.name}&quantity${liCtr.count}=${cartLineItem.qty}";
            user_hash = "${user_hash}";
            </c:forEach>

            vizuryLink += "&currency=INR&section=1&level=2";
            document.getElementById("vizuryTargeting").src = vizuryLink + "&uid="+user_hash;
        </script>
    </c:if>
</s:layout-component>

</s:layout-render>


