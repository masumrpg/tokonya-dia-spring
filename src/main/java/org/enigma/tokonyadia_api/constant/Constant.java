package org.enigma.tokonyadia_api.constant;

public class Constant {
    // Table
    public static final String USER_ACCOUNT_TABLE = "m_user_account";
    public static final String CUSTOMER_TABLE = "m_customer";
    public static final String STORE_TABLE = "m_store";
    public static final String PRODUCT_TABLE = "m_product";
    public static final String PRODUCT_CATEGORY_TABLE = "m_product_category";
    public static final String PRODUCT_PROMO_TABLE = "t_product_promo";
    public static final String PRODUCT_RATING_TABLE = "t_product_rating";
    public static final String SHIPMENT_TABLE = "t_shipment";
    public static final String PAYMENT_TABLE = "t_payment";
    public static final String ORDER_TABLE = "t_order";
    public static final String ORDER_DETAIL_TABLE = "t_order_detail";

    // API
    public static final String USER_API = "/api/users";
    public static final String AUTH_API = "/api/auth";
    public static final String CUSTOMER_API = "/api/customers";
    public static final String PRODUCT_API = "/api/products";
    public static final String STORE_API = "/api/stores";
    public static final String ORDER_API = "/api/order";
//    public static final String TRANSACTION_DETAIL_API = "/api/transaction_details";

    // Message
    // user
    public static final String SUCCESS_CREATE_USER = "Successfully created user";
    public static final String SUCCESS_LOGIN = "Success login";
    public static final String SUCCESS_FETCH_USER_INFO = "Successfully fetch info user";

    // customer
    public static final String SUCCESS_CREATED_CUSTOMER = "Successfully created customer";
    public static final String SUCCESS_GET_CUSTOMER = "Successfully get customer";
    public static final String SUCCESS_UPDATE_CUSTOMER = "Successfully update customer";
    public static final String SUCCESS_DELETE_CUSTOMER = "Successfully delete customer";
    public static final String SUCCESS_GET_ALL_CUSTOMER = "Successfully get customers";
    // store
    public static final String SUCCESS_CREATED_STORE = "Successfully created store";
    public static final String SUCCESS_GET_STORE = "Successfully get store";
    public static final String SUCCESS_UPDATE_STORE = "Successfully update store";
    public static final String SUCCESS_DELETE_STORE = "Successfully delete store";
    public static final String SUCCESS_GET_All_STORE = "Successfully get all stores";
    // product
    public static final String SUCCESS_CREATED_PRODUCT = "Successfully created product";
    public static final String SUCCESS_GET_PRODUCT = "Successfully get product";
    public static final String SUCCESS_UPDATE_PRODUCT = "Successfully update product";
    public static final String SUCCESS_DELETE_PRODUCT = "Successfully delete product";
    public static final String SUCCESS_GET_ALL_PRODUCT = "Successfully get products";
    // order
    public static final String SUCCESS_CREATED_ORDER = "Successfully created order";
    public static final String SUCCESS_GET_ORDER = "Successfully get orders";

    // Error
    // user
    public static final String ERROR_USERNAME_DUPLICATE = "Username already exists";
    public static final String ERROR_ROLE_NOT_FOUND = "Role not found";
//    public static final String INVALID_CREDENTIALS = "Invalid credentials";
    public static final String ERROR_USERNAME_NOT_FOUND = "User not found";
    // jwt
    public static final String ERROR_CREATE_JWT = "Error creating JWT";
}
