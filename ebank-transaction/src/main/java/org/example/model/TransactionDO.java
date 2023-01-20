package org.example.model;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("transaction")
public class TransactionDO implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * currency
     */
    private String currency;

    /**
     * amount
     */
    private BigDecimal amount;

    /**
     * international bank account number
     */
    private String accountIban;

    /**
     * transaction day time
     */
    private Date valueDate;

    /**
     * description
     */
    private String description;

    /**
     * sender user id
     */
    @TableField(value="`from`")
    private Long from;

    /**
     * receiver user id
     */
    @TableField(value="`to`")
    private Long to;


}
