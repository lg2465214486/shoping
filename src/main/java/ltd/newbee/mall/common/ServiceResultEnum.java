/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本系统已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2020 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package ltd.newbee.mall.common;

/**
 * @author 13
 * @qq交流群 796794009
 * @email 2449207463@qq.com
 * @link https://github.com/newbee-ltd
 */
public enum ServiceResultEnum {
    ERROR("error"),

    SUCCESS("success"),

    DATA_NOT_EXIST("No records found！"),

    SAME_CATEGORY_EXIST("There already exists a category with the same level and name！"),

    SAME_LOGIN_NAME_EXIST("The user already exists！"),

    LOGIN_NAME_NULL("Please enter your login name！"),

    LOGIN_PASSWORD_NULL("Please input a password！"),

    LOGIN_VERIFY_CODE_NULL("Please enter the verification code！"),

    LOGIN_VERIFY_CODE_ERROR("Verification code error！"),

    SAME_INDEX_CONFIG_EXIST("The same homepage configuration item already exists！"),

    GOODS_CATEGORY_ERROR("Abnormal classification data！"),

    SAME_GOODS_EXIST("The same product information already exists！"),

    GOODS_NOT_EXIST("The product does not exist！"),

    GOODS_PUT_DOWN("The product has been taken down！"),

    SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR("Exceeding the maximum purchase quantity for a single product！"),

    SHOPPING_CART_ITEM_TOTAL_NUMBER_ERROR("Exceeding the maximum capacity of the shopping cart！"),

    LOGIN_ERROR("Login failed！"),

    LOGIN_USER_LOCKED("The user has been banned from logging in！"),

    ORDER_NOT_EXIST_ERROR("Order does not exist！"),

    ORDER_ITEM_NOT_EXIST_ERROR("The order item does not exist！"),

    NULL_ADDRESS_ERROR("The address cannot be empty！"),

    ORDER_PRICE_ERROR("Abnormal order price！"),

    ORDER_GENERATE_ERROR("Order generation exception！"),

    SHOPPING_ITEM_ERROR("Abnormal shopping cart data！"),

    SHOPPING_ITEM_COUNT_ERROR("Insufficient inventory！"),

    ORDER_STATUS_ERROR("Abnormal order status！"),

    CLOSE_ORDER_ERROR("Failed to close order！"),

    OPERATE_ERROR("operation failed！"),

    NO_PERMISSION_ERROR("No permission！"),

    DB_ERROR("database error");

    private String result;

    ServiceResultEnum(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}