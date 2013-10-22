package com.hk.api.edge.constants;

public class MessageConstants {
    public static final String BLANK_CAT_PREFIXES               = "cat prefixes cannot be blank, need to specify at least one";
    public static final String INVALID_BRAND_IDS                = "brands should specified as ids";
    public static final String INVALID_REQ                      = "invalid request";
    public static final String INVALID_NODE_FOR_CATALOG         = "invalid node for catalog req";
    public static final String INVALID_PARAMS_FOR_PAGE          = "page param params cannot be blanl";
    public static final String NO_USER_FOUND                    = " user does not exist in system";
    public static final String CATEGORY_NOT_FOUND               = "Category details could not be retreived";

    // user signup/login messages
    public static final String CANNOT_CREATE_GUEST_USER         = " cannot create guest user";
    public static final String SIGNUP_REQ_INVALID               = "Either email or password is blank";
    public static final String USER_ID_ALREADY_TAKEN            = "Login Email is already registered in the system, Please choose another";
    public static final String RESET_PASSWORD_REQ_INVALID       = "Either the new password passed is blank or the link passed is invalid";
    public static final String USER_EXIST                       = "User Exist";
    public static final String USER_NOT_EXIST                   = "User Doesn't Exist";

    // revies/rating messages
    public static final String USER_ALREADY_CAST_VOTE           = "You have already cast vote for this review";
    public static final String USER_ALREADY_MARKED_SPAM         = "You have already marked this review as spam";
    public static final String USER_VOTE_RECORDED               = "Your vote has been recorded successfully";
    public static final String USER_REVIEW_RECORDED             = "Your review has been recorded successfully";
    public static final String USER_RATING_RECORDED             = "Your rating has been recorded successfully";
    public static final String REVIEWS_NOT_FOUND                = "Variant reviews could not be retreived";

    // user activation messages
    public static final String ACTIVATION_LINK_INVALID          = "The link passed is invalid";
    public static final String ACTIVATION_LINK_EXPIRED          = "The link passed has expired.";
    public static final String ACTIVATION_SUCCESSFUL            = "Your account has been activated";
    public static final String ALREADY_ACTIVATED                = "Your has already been activated";

    // address messages
    public static final String INVALID_CREATE_ADDRESS_REQ       = "Address cannot be created with name, line1, city or pincode blank";
    public static final String INVALID_PINCODE                  = "Currently, service is not provided to the specified pincode";
    public static final String NO_ADDRESS_FOUND                 = "Address could not be found in the system";

    // user account update messages
    public static final String INVALID_UPDATE_BASIC_INFO_REQ    = "Name cannot be blank";
    public static final String INVALID_UPDATE_EMAIL_REQ         = "Email cannot be blank";
    public static final String INVALID_UPDATE_PASSWORD_REQ      = "Either old password or new password is blank";
    public static final String INVALID_NAME                     = "Kindly enter a valid name";
    public static final String INVALID_EMAIL                    = "Kindly enter a valid email";
    public static final String INVALID_OLD_PASSWORD             = "Invalid old password";
    public static final String PASSWORD_RESET_SUCCESSFULLY      = "Your password has been reset successfully";

    // menu
    public static final String NO_MENU_EXISTS                   = "No menu has been designed for the store";

    // cart
    public static final String PRODUCT_OOS                      = "product is out of stock";
    public static final String REQ_QTY_NA                       = "requested quantity not available";
    public static final String INVALID_CART_QTY                 = "variant qty cannot be zero or negative for adding in cart";
    public static final String PRODUCT_ADDED_TO_CART            = "product added to cart";
    public static final String CART_UPDATED                     = "cart updated with requested qty";
    public static final String UNABLE_TO_ADD_TO_CART            = "Cannot add product to cart right now";
    public static final String EMPTY_CART                       = "No Variant added to your cart";

    // variant
    public static final String VARIANT_NOT_FOUND                = "Variant details cannot be retrieved";
    public static final String USER_LOGGED_IN                   = "user logged in";

    // location
    public static final String COUNTRY_NOT_FOUND                = "Country details could not be retrieved";
    public static final String STATE_NOT_FOUND                  = "State details could not be retrieved";

    // order
    public static final String UNABLE_TO_PLACE_ORDER            = "Order could not be placed";
    public static final String AMOUNT_MISMATCH                  = "Your Order Amount has been changed, please check and place order again";
    public static final String CHECKSUM_MISMATCH                = "checksum mismatch";
    public static final String INVALID_CART                     = "Invalid Cart";

    // payment
    public static final String PAYMENT_SUCCESSFUL               = "Payment Successful";
    public static final String PAYMENT_FAILED                   = "Payment Failed";
    public static final String AP                               = "Authorization Pending";
    public static final String COD_AP                           = "COD Verification Pending";

    // pack
    public static final String PACK_ADDED_MSG                   = "1 Pack Added to cart";

    // offer
    public static final String COUPON_NOT_FOUND                 = "Coupon details could not be retrieved";
    public static final String COUPON_MAX_ALLOWED_TIMES_REACHED = "This coupon is no longer valid";

    // reward
    public static final String REWARD_POINT_ADDED               = "Reward Point added successfully";
    public static final String ERROR                            = "Internal Server Error, please try again";
}
