package org.enigma.tokonyadia_api.constant;

public class Constant {
    // Table
    public static final String USER_ACCOUNT_TABLE = "m_user_account";
    public static final String PERSON_TABLE = "m_person";
    public static final String STORE_TABLE = "m_store";
    public static final String PRODUCT_TABLE = "m_product";
    public static final String PRODUCT_CATEGORY_TABLE = "m_product_category";
    public static final String PAYMENT_TABLE = "m_payment";
    public static final String SHIPMENT_TABLE = "t_shipment";
    public static final String PRODUCT_PROMO_TABLE = "t_product_promo";
    public static final String PRODUCT_RATING_TABLE = "t_product_rating";
    public static final String ORDER_TABLE = "t_order";
    public static final String ORDER_DETAIL_TABLE = "t_order_detail";

    // API
    public static final String USER_API = "/api/users";
    public static final String AUTH_API = "/api/auth";
    public static final String PERSON_API = "/api/persons";
    public static final String PRODUCT_API = "/api/products";
    public static final String STORE_API = "/api/stores";
    public static final String ORDER_API = "/api/orders";
    public static final String PRODUCT_CATEGORY_API = "/api/product/categories";
//    public static final String TRANSACTION_DETAIL_API = "/api/transaction_details";

    // Message
    // user
    public static final String SUCCESS_CREATE_USER = "Successfully created user";
    public static final String SUCCESS_LOGIN = "Success login";
    public static final String SUCCESS_FETCH_USER_INFO = "Successfully fetch info user";

    // person
    public static final String SUCCESS_CREATED_PERSON = "Successfully created person";
    public static final String SUCCESS_GET_PERSON = "Successfully get person";
    public static final String SUCCESS_UPDATE_PERSON = "Successfully update person";
    public static final String SUCCESS_DELETE_PERSON = "Successfully delete person";
    public static final String SUCCESS_GET_ALL_PERSON = "Successfully get persons";
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
    // order
    public static final String SUCCESS_CREATED_ORDER = "Successfully created order";
    public static final String SUCCESS_CREATE_ORDER_DETAIL = "Successfully created order detail";
    public static final String SUCCESS_GET_ORDER = "Successfully get orders";
    public static final String SUCCESS_GET_ORDER_DETAIL = "Successfully get orders details";

    // Error
    // person
    public static final String ERROR_PERSON_NOT_FOUND = "Person not found";
    // user
    public static final String ERROR_USERNAME_DUPLICATE = "Username already exists";
    public static final String ERROR_ROLE_NOT_FOUND = "Role not found";
    public static final String PRODUCT_CATEGORY_NOT_FOUND = "Product category not found";
//    public static final String INVALID_CREDENTIALS = "Invalid credentials";
    public static final String ERROR_USERNAME_NOT_FOUND = "User not found";
    // jwt
    public static final String ERROR_CREATE_JWT = "Error creating JWT";
    // order
    public static final String ERROR_ORDER_ALREADY_EXISTS = "Order already exists";
    public static final String ERROR_ORDER_NOT_FOUND = "Order not found";
    public static final String ERROR_ADD_ITEMS_TO_NON_DRAFT = "Error add items to non draft";
}
