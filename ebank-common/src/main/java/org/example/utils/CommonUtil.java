package org.example.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.Random;

@Slf4j
public class CommonUtil {

    /**
     * get random length string
     * @param len
     * @return
     */
    private static final String ALL_CHAR_NUM = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public static String getStringNumRandom(int len) {
        Random random = new Random();
        StringBuilder saltString = new StringBuilder(len);
        for (int i = 1; i <= len; ++i) {
            saltString.append(ALL_CHAR_NUM.charAt(random.nextInt(ALL_CHAR_NUM.length())));
        }
        return saltString.toString();
    }

    /**
     * get current timestamp
     * @return
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * response Json data to frontend
     * @param response
     * @param obj
     */
    public static void sendJsonMessage(HttpServletResponse response, Object obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json; chartset=utf-8");

        try (PrintWriter writer = response.getWriter()) { // auto close in jdk8
            writer.print(objectMapper.writeValueAsString(obj));
            response.flushBuffer();
        } catch (IOException e) {
            log.warn("error when respond json to frontend:{}", e);
        }
    }
}
