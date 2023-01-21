package org.example.unitTest;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.example.UserApplication;
import org.example.enums.BizCodeEnum;
import org.example.interceptor.LoginInterceptor;
import org.example.model.LoginUser;
import org.example.model.TransferInfo;
import org.example.request.CreateAccountRequest;
import org.example.request.UserLoginRequest;
import org.example.request.UserRegisterRequest;
import org.example.service.UserAccountService;
import org.example.service.UserService;
import org.example.utils.CommonUtil;
import org.example.utils.JWTUtil;
import org.example.utils.JsonData;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApplication.class)
@Slf4j
public class UserAccountTest {

    @Autowired
    private UserAccountService userAccountService;

    private final String token = "ebankbobeyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlYmFuayIsImlkIjoxNjE2NTg0NDM4MTI3MzU3OTUzLCJuYW1lIjoidGVzdEJvYiIsIm1haWwiOiJ0ZXN0QGdtYWlsLmNvbSIsImlhdCI6MTY3NDI1ODc4NCwiZXhwIjoxNjg2MzU0Nzg0fQ.H7TAj_ncdAfgbBU_y5uBCJO1-FSLbSNR-n7Be9mdGSY";

    /**
     * id: 1616584438127357953
     * mail: test@gmail.com
     * name: testBob
     * sex: 0
     * pwd: test
     */
    @Test
    public void createAccountTest() {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setCurrency("CHF");

        String accessToken = token;
        Claims claims = JWTUtil.checkJWT(accessToken);
        Assert.assertNotNull(claims);

        long userId = Long.valueOf(claims.get("id").toString());
        String name = (String) claims.get("name");
        String mail = (String) claims.get("mail");
        LoginUser loginUser = LoginUser
                .builder()
                .name(name)
                .id(userId)
                .mail(mail)
                .build();

        LoginInterceptor.threadLocal.set(loginUser);
        JsonData jsonData = userAccountService.createAccount(createAccountRequest);
        Assert.assertEquals("0", jsonData.getCode().toString());
    }

    @Test
    public void transferTest() {
        TransferInfo transferInfo = new TransferInfo();
        transferInfo.setCurrency("CHF");
        transferInfo.setFrom(1616584438127357953L);
        transferInfo.setTo(1615129628081508354L);
        transferInfo.setAmount(new BigDecimal(1));

        String accessToken = token;
        Claims claims = JWTUtil.checkJWT(accessToken);
        Assert.assertNotNull(claims);

        long userId = Long.valueOf(claims.get("id").toString());
        String name = (String) claims.get("name");
        String mail = (String) claims.get("mail");
        LoginUser loginUser = LoginUser
                .builder()
                .name(name)
                .id(userId)
                .mail(mail)
                .build();

        LoginInterceptor.threadLocal.set(loginUser);

        JsonData jsonData = userAccountService.transfer(transferInfo);
        Assert.assertEquals("0", jsonData.getCode().toString());
    }
}
