package com.imooc.test;


import io.jsonwebtoken.Jwts;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JasyptTest
{
    /**
     * 实例化加密器
     * 配置加密算法和密钥
     *  加密
     */
    @Test
    public void testPwdEncryption() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();

        config.setPassword("aasdndiawjwnmo2039");
        config.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setConfig(config);


        // 对密码进行加密，注意对于加密每次生成的密码都不同，但是解密的密码都相同
        String encrypted = encryptor.encrypt("guest");
        System.out.println(encrypted);
    }

    @Test
    public void testPwdDecryption() {
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
        config.setPassword("aasdndiawjwnmo2039");
        config.setAlgorithm("PBEWithMD5AndDES");

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setConfig(config);
        String decrypted = encryptor.decrypt("ihZsUVhTSpwqOh47J1hf1g==");
        System.out.println(decrypted);


    }
}
