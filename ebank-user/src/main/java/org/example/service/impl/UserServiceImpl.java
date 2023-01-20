package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.Md5Crypt;
import org.example.enums.BizCodeEnum;
import org.example.model.LoginUser;
import org.example.model.UserDO;
import org.example.mapper.UserMapper;
import org.example.request.UserLoginRequest;
import org.example.request.UserRegisterRequest;
import org.example.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.utils.CommonUtil;
import org.example.utils.JWTUtil;
import org.example.utils.JsonData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Bob
 * @since 2023-01-16
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * user signup
     * @param registerRequest
     * @return
     */
    @Override
    public JsonData register(UserRegisterRequest registerRequest) {
        String mail = registerRequest.getMail();
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<UserDO>().eq("mail", mail);
        if (userMapper.selectList(queryWrapper).size() != 0) {
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_EXISTS);
        }

        UserDO userDO = new UserDO();
        userDO.setCreateTime(new Date());
        userDO.setMail(mail);
        userDO.setName(registerRequest.getName());
        userDO.setSex(registerRequest.getSex());
        userDO.setSecret("$1$" + CommonUtil.getStringNumRandom(8));

        String cryptPwd = Md5Crypt.md5Crypt(registerRequest.getPwd().getBytes(), userDO.getSecret());
        userDO.setPwd(cryptPwd);

        // store into db
        int row = userMapper.insert(userDO);
        if (row == 0) {
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_REGISTER_FAIL);
        }
        log.info("register success:{}", userDO.toString());
        return JsonData.buildSuccess();
    }

    /**
     * user login
     * @param userLoginRequest
     * @return
     */
    @Override
    public JsonData login(UserLoginRequest userLoginRequest) {

        String mail = userLoginRequest.getMail();
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<UserDO>().eq("mail", mail);
        List<UserDO> userDOList = userMapper.selectList(queryWrapper);
        if (userDOList == null || userDOList.size() == 0) {
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_PWD_ERROR);
        }

        UserDO userDO = userDOList.get(0);
        String cryptPwd = Md5Crypt.md5Crypt(userLoginRequest.getPwd().getBytes(), userDO.getSecret());
        if (cryptPwd.equals(userDO.getPwd())) {
            LoginUser loginUser = LoginUser.builder().build();
            BeanUtils.copyProperties(userDO, loginUser);
            log.info("login with user:{}", loginUser);
            String accessToken = JWTUtil.geneJsonWebToken(loginUser);
            return JsonData.buildSuccess(accessToken);
        } else {
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_PWD_ERROR);
        }
    }
}
