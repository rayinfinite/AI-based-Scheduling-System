package com.github.rayinfinite.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;

@Slf4j
@SpringBootApplication
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
		try {
			// 创建KeyPairGenerator实例，并指定算法为EC(椭圆曲线)
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");

			// 指定椭圆曲线参数，secp256r1是256位的标准曲线之一
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256r1");
			keyGen.initialize(ecSpec);

			// 生成密钥对
			KeyPair keyPair = keyGen.generateKeyPair();

			// 获取公钥和私钥，并将其编码为Base64格式
			String publicKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
			String privateKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());

			// 打印Base64编码后的公钥和私钥
            log.info("Public Key (Base64): {}", publicKeyBase64);
            log.info("Private Key (Base64): {}", privateKeyBase64);
		} catch (NoSuchAlgorithmException e) {
            log.error("无法找到EC算法：{}", e.getMessage());
		} catch (Exception e) {
            log.error("发生错误：{}", e.getMessage());
		}
	}
}
