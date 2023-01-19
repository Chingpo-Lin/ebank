package org.example.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author Bob
 * @since 2023-01-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user")
public class UserDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;

    /**
     * name
     */
    private String name;

    /**
     * pwd
     */
    private String pwd;

    /**
     * 0 is female, 1 is male
     */
    private Integer sex;

    private Date createTime;

    /**
     * mail
     */
    private String mail;

    /**
     * balance in usd
     */
    private Long balance;

    /**
     * user secret
     */
    private String secret;


}
