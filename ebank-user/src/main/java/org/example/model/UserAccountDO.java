package org.example.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author Bob
 * @since 2023-01-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_account")
public class UserAccountDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;

    private Long userId;

    /**
     * balance in usd
     */
    private BigDecimal balance;

    /**
     * account currency
     */
    private String currency;


}
