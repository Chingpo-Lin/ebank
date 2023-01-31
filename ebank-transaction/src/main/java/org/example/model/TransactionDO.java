package org.example.model;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

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

    @TableId(value = "id")
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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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

    /**
     * status
     */
    @TableField(value="`status`")
    private Integer status;

}
