package com.sht.shoesboot;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

/**
 * @author Aaron
 * @date 2020/11/25 20:54
 */
public class JasyptTest {

    @Test
    public void md5Test() {
        String md5Password = DigestUtils.md5DigestAsHex("Aaron.Shen".getBytes());
        System.out.println(md5Password);
        // 841b54a7a45883874c36d59ab6e7d80c
    }

    @Test
    public void jasyptTest() {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        //加密所需的salt(盐)
        textEncryptor.setPassword("841b54a7a45883874c36d59ab6e7d80c");
        //要加密的数据（数据库的用户名或密码）
        String url = textEncryptor.encrypt("47.98.128.88");
        String username = textEncryptor.encrypt("kindy");
        String password = textEncryptor.encrypt("kindy0916");
        System.out.println("url:"+url);
        System.out.println("username:"+username);
        System.out.println("password:"+password);
    }

    @Test
    public void decryptionTest() {
        StandardPBEStringEncryptor stringEncryptor = new StandardPBEStringEncryptor();
        stringEncryptor.setPassword("841b54a7a45883874c36d59ab6e7d80c");
        stringEncryptor.setAlgorithm("PBEWithMD5AndDES");
        String username = stringEncryptor.encrypt("");
        String password = stringEncryptor.encrypt("sht123");
        String pass = stringEncryptor.decrypt(password);
        System.out.println(username);
        System.out.println(password);
        System.out.println(pass);
    }
}
