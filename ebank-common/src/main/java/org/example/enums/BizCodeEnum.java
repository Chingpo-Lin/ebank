package org.example.enums;

import lombok.Data;
import lombok.Getter;

/**
 * enum class for status and err msg
 *
 * first 3 number means service, last 3 number means interface
 */
public enum BizCodeEnum {

    /**
     * account
     * */
    ACCOUNT_EXISTS(250001,"account has already exist"),
    ACCOUNT_PWD_ERROR(250002,"name or password error"),
    ACCOUNT_NOT_LOGIN(250003,"account not login"),
    ACCOUNT_REGISTER_FAIL(250004,"register fail"),
    ACCOUNT_CURRENCY_EXIST(250005,"currency account exist"),
    ACCOUNT_CURRENCY_SENDER_NOT_EXIST(250006,"please create an account before sending money"),
    ACCOUNT_CURRENCY_RECEIVER_NOT_EXIST(250007,"please ask receiver to create an account"),

    /**
     * transfer
     */
    TRANSFER_SAME_USER(260001,"cannot transfer money to yourself"),
    BALANCE_NOT_ENOUGH(260002,"do not have enough balance"),
    ;

    @Getter
    private String msg;

    @Getter
    private int code;

    private BizCodeEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }


}
