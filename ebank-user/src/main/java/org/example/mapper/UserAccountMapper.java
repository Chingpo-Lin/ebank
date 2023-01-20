package org.example.mapper;

import org.apache.ibatis.annotations.Param;
import org.example.model.UserAccountDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.math.BigDecimal;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Bob
 * @since 2023-01-19
 */
public interface UserAccountMapper extends BaseMapper<UserAccountDO> {


    int updateAccount(@Param("amount") BigDecimal amount, @Param("id") Long id);
}
