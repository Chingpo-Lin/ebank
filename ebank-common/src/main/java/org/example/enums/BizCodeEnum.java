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
    ACCOUNT_REGISTER_FAIL(250004,"register fail");

    @Getter
    private String msg;

    @Getter
    private int code;

    private BizCodeEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }


}
