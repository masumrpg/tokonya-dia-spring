package org.enigma.tokonyadia_api.constant;

public class Constant {
    // Table
    public static final String USER_ACCOUNT_TABLE = "m_user_account";
    public static final String PERSON_TABLE = "m_person";
    public static final String STORE_TABLE = "m_store";
    public static final String PRODUCT_TABLE = "m_product";
    public static final String PRODUCT_CATEGORY_TABLE = "m_product_category";
    public static final String PAYMENT_TABLE = "t_payment";
    public static final String SHIPMENT_TABLE = "t_shipment";
    public static final String PRODUCT_RATING_TABLE = "t_product_rating";
    public static final String ORDER_TABLE = "t_order";
    public static final String ORDER_DETAIL_TABLE = "t_order_detail";
    public static final String INVOICE_TABLE = "t_invoice";
    public static final String INVOICE_ITEM_TABLE = "t_invoice_item";

    // auth
    public static final String SUCCESS_LOGOUT = "Success logout";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    public static final String REFRESH_TOKEN_REQUIRED = "Refresh token is required";

    // API
    public static final String USER_API = "/api/users";
    public static final String AUTH_API = "/api/auth";
    public static final String PERSON_API = "/api/persons";
    public static final String PRODUCT_API = "/api/products";
    public static final String STORE_API = "/api/stores";
    public static final String ORDER_API = "/api/orders";
    public static final String PRODUCT_CATEGORY_API = "/api/products/categories";
    public static final String PRODUCT_RATING_API = "/api/products/ratings";
    public static final String PAYMENT_API = "/api/payments";
    public static final String INVOICE_API = "/api/invoices";

    // Message
    // user
    public static final String SUCCESS_CREATE_USER = "Successfully created user";
    public static final String SUCCESS_LOGIN = "Success login";
    public static final String SUCCESS_FETCH_USER_INFO = "Successfully fetch info user";
    public static final String SUCCESS_REACTIVATE_ACCOUNT = "Successfully reactivate account";

    // person
    public static final String SUCCESS_CREATED_PERSON = "Successfully created person";
    public static final String SUCCESS_GET_PERSON = "Successfully get person";
    public static final String SUCCESS_UPDATE_PERSON = "Successfully update person";
    public static final String SUCCESS_DELETE_PERSON = "Successfully delete person";
    public static final String SUCCESS_GET_ALL_PERSON = "Successfully get persons";
    public static final String SUCCESS_DELETE_SELF = "Successfully delete self";
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
    // product category
    public static final String SUCCESS_GET_ALL_PRODUCT_CATEGORY = "Successfully retrieved products category";
    // product rating
    public static final String SUCCESS_CREATE_PRODUCT_RATING = "Successfully created product rating";
    public static final String SUCCESS_GET_PRODUCT_RATING = "Successfully get product rating";
    public static final String SUCCESS_UPDATE_PRODUCT_RATING = "Successfully update product rating";
    public static final String SUCCESS_UPDATE_DELETE_RATING = "Successfully delete product rating";
    public static final String SUCCESS_GET_ALL_PRODUCT_RATING = "Successfully get all product rating";
    // order
    public static final String SUCCESS_CREATED_ORDER = "Successfully created order";
    public static final String SUCCESS_CREATE_ORDER_DETAIL = "Successfully created order detail";
    public static final String SUCCESS_GET_ORDER = "Successfully get orders";
    public static final String SUCCESS_GET_ORDER_DETAIL = "Successfully get orders details";
    public static final String SUCCESS_CHECKOUT = "Checkout Success";
    // order detail
    public static final String SUCCESS_DECREASE_ORDER_DETAIL = "Successfully decrease order detail";
    // Checkout
    public static final String SUCCESS_CHECKOUT_ORDER = "Successfully checkout order";
    // shipment
//    public static final String SUCCESS_GET_SHIPMENT = "Successfully get shipment";
//    public static final String SUCCESS_GET_ALL_SHIPMENT = "Successfully get all shipments";

    // invoice
    public static final String SUCCESS_GET_INVOICE = "Successfully get invoice";
    // OK
    public static final String OK = "OK";

    // Error
    // person
    public static final String ERROR_PERSON_NOT_FOUND = "Person not found";
    // user
    public static final String ERROR_USERNAME_DUPLICATE = "Username already exists";
    public static final String ERROR_ROLE_NOT_FOUND = "Role not found";
    public static final String PRODUCT_CATEGORY_NOT_FOUND = "Product category not found";
    public static final String INVALID_CREDENTIALS = "Invalid credentials";
    public static final String ERROR_USERNAME_NOT_FOUND = "User not found";
    // store
    public static final String ERROR_STORE_NOT_FOUND = "Store not found";
    // jwt
    public static final String ERROR_CREATE_JWT = "Error creating JWT";
    public static final String SUCCESS_GENERATED_ACCESS_TOKEN = "Success generated access token";
    // order
    public static final String ERROR_ORDER_ALREADY_EXISTS = "Order draft already exists";
    public static final String ERROR_ORDER_NOT_FOUND = "Order not found";
    public static final String ERROR_ADD_ITEMS_TO_NON_DRAFT = "Error add items to non draft";
    public static final String ERROR_CHECKOUT_NON_DRAFT = "Error checkout non draft";
    public static final String ERROR_ORDER_CANNOT_EMPTY = "Error order details cannot be empty";
    public static final String ERROR_ORDER_IS_NOT_PENDING = "Order is not PENDING";
    // shipment
    public static final String ERROR_SHIPMENT_NOT_FOUND = "Shipment not found";
    // product rating
    public static final String PRODUCT_RATING_NOT_FOUND = "Product rating not found";
    // product promo
    public static final String ERROR_PRODUCT_PROMO_NOT_FOUND = "Product promo not found";
    public static final String ERROR_REMOVE_ITEMS_FROM_NON_DRAFT = "Error remove items from non draft";
    // invoice
    public static final String ERROR_INVOICE_ALREADY_EXIST = "Invoice already exists";
    public static final String ERROR_INVOICE_NOT_FOUND = "Invoice not found";
}
