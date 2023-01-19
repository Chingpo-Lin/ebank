package org.example.request;

import lombok.Data;

@Data
public class UserRegisterRequest {

    /**
     * user name
     */
    private String name;

    /**
     * user pwd
     */
    private String pwd;

    /**
     * user sex, 0 is female, 1 is male
     */
    private Integer sex;

    /**
     * user email address
     */
    private String mail;
}
