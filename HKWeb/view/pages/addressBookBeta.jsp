<%@ page import="net.sourceforge.stripes.util.ssl.SslUtil" %>
<%@ page import="com.hk.web.filter.WebContext" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>


<s:useActionBean beanclass="com.hk.web.action.core.user.SelectAddressAction" event="pre" var="addressBean"/>

<s:layout-render name="/layouts/checkoutLayoutBeta.jsp" pageTitle="Select a shipping address">
<%@ include file="/layouts/_userData.jsp" %>
<%
    boolean isSecure = WebContext.isSecure();
    pageContext.setAttribute("isSecure", isSecure);
%>
<c:set var="countryId" value="80"/>
<s:layout-component name="htmlHead">
    <script type="text/javascript">
        var noAddress = false;

        $(document).ready(function () {
            if (${addressBean.printAlert}) {
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

        .row {
            float: right;
        }

    </style>
</s:layout-component>

<s:layout-component name="modal">
    <div class="jqmWindow" id="jqmWindowNewAddress"><h1>Loading..</h1></div>
</s:layout-component>

<s:layout-component name="steps">
    <%-- checkout strip flow begins--%>
    <s:layout-render name="/layouts/embed/_checkoutStripBeta.jsp"/>

    <%--checkout strip flow ends--%>
</s:layout-component>
<s:layout-component name="steps_content">
    <div class='current_step_content' style="font-size: 14px;">

            <%--<h3 style="text-align: center; margin-bottom: 20px; color: #888;">
              What address should we ship your order to?
            </h3>--%>

        <c:if test="${fn:length(addressBean.addresses)>0}">
            <div class='left2' style="float: left;">
                <h3 class="arialBlackBold" style="text-align: left; margin-bottom: 20px;">
                    Use one of your saved addresses
                </h3>

                <c:forEach items="${addressBean.addresses}" var="address" varStatus="addressCount">
                    <div class="address raj_address usr-add-cntnr" style="width:300px;">
                        <s:link beanclass="com.hk.web.action.core.user.SelectAddressAction" class="usr-add"
                                event="checkout"
                                title="Click to use this address and proceed"
                                style="display: block; margin-bottom: 30px;">
                            <s:param name="selectedAddress" value="${address.id}"/>

                            <h5 class="name fnt-caps adresss-usr-name">${address.name}</h5>

                            <div class='street street1'>${address.line1}</div>
                            <c:if test="${hk:isNotBlank(address.line2)}">
                                <div class="street street2">${address.line2}</div>
                            </c:if>
                            <div class='city address-cityId'>${address.city}</div>
                            <div class='state'>${address.state}</div>
                            <div class='pin'>${address.pincode}</div>
                            <div class='phone'>${address.phone}</div>

                            <s:link style="display:inline-block "
                                    beanclass="com.hk.web.action.core.user.SelectAddressAction"
                                    event="remove" class=" delete span2 btn btn-gray ">
                                <s:param name="deleteAddress" value="${address.id}"/>
                                DELETE
                            </s:link>

                            <span style="display:inline-block;margin-left: 20px; "
                                  class=" edit btn btn-gray">EDIT</span>

                            <div class="useAddressButton">
                                Click to use this address
                                and proceed
                            </div>
                            <div class="clear"></div>
                        </s:link>
                    </div>
                </c:forEach>
                <script type="text/javascript">
                    $(document).ready(function () {
                        var bool = false;
                        $('.edit').click(function () {
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
                            form.find("[name='address.state']").val(state.toUpperCase());
                            form.find("input[type='text'][name='address.pincode']").val(pin);
                            form.find("input[type='text'][name='address.phone']").val(phone);
                            form.find("input[type='hidden'][name='address.id']").val(id);
                        });
                        $('.delete').click(function () {
                            if (confirm('Are you sure you want to delete this address?')) {
                                bool = true;
                                return true;
                            } else {
                                return false;
                            }
                        });

                        $('.address').hover(
                                function () {
                                    $(this).children('.hidden').slideDown(100);
                                    $(this).children('.edit').click(function () {
                                        return false;
                                    });
                                    $(this).children('.delete').click(function () {
                                        if (bool) return true;
                                        else
                                            return false;
                                    });
                                },
                                function () {
                                    $(this).children('.hidden').slideUp(50);
                                }
                        );
                        $('.address').click(function () {
                            var add_url = $(this).children('a').attr('href');
                            document.location.href = add_url;
                        });
                    });

                </script>


            </div>
        </c:if>

        <div class='right'
             style="width: 440px; background: initial; border: none; padding: 0 0 0 50px; float: right;   ${fn:length(addressBean.addresses)<=0 ? 'float: none;margin: 0 auto; left:0;' : ''} ">
            <h3 class="shippingAddressheading arialBlackBold"
                style="margin-bottom: 20px;float: right;text-align: left;display: inline-block;width: 320px;">
                <c:if test="${fn:length(addressBean.addresses)>0}">
                    Or
                </c:if>
                add a new shipping address
            </h3>


            <div class="addressContainer shipping_address cont-rht">
                <s:form beanclass="com.hk.web.action.core.user.NewAddressAction" id="newAddressForm">
                    <s:layout-render name="/layouts/addressLayoutBeta.jsp"/>
                    <s:hidden name="countryId" value="${countryId}"/>
                    <div style="display: inline-block; margin: 20px 0 0;">
                        <s:submit name="create" value="Continue" class="btn btn-blue continue"/>
                    </div>
                </s:form>
            </div>

            <div class="addressSuccessContainer" style="display:none;">
                <h3>New address has been added</h3>

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
            document.getElementById("vizuryTargeting").src = vizuryLink + "&uid=" + user_hash;
        </script>
    </c:if>
</s:layout-component>

</s:layout-render>


