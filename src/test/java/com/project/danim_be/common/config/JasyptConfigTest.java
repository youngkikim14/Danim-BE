package com.project.danim_be.common.config;

import static org.junit.jupiter.api.Assertions.*;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JasyptConfigTest {

	@Test
	void encryptTest(){
		String id = "root";
		// String kakaoClientId = "d47514ab2cd7a805e902a2c8d4d70ea6";

		// String googleClientId = "646452480095-kitf99od8ha7h6tmbgmsojafb777em71.apps.googleusercontent.com";
		// String googleSecret = "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";

		// String naverClientId = "CIBjjBYsXLE57LDuX_3D";
		// String naverSecret = "fa9XkTCLos";

		String mysqlurl = "jdbc:mysql://danim-database.cjzr705yf0ml.ap-northeast-2.rds.amazonaws.com:3306/mydatabase?serverTimezone=Asia/Seoul";
		// String username = "danim";
		// String password = "danim123123";


		// System.out.println("kakaoClientId = "+jasyptEncoding(kakaoClientId));

		// System.out.println("googleClientId = "+jasyptEncoding(googleClientId));
		// System.out.println("googleSecret = "+jasyptEncoding(googleSecret));

		// System.out.println("naverClientId = "+jasyptEncoding(naverClientId));
		// System.out.println("naverSecret = "+jasyptEncoding(naverSecret));

		// System.out.println("jwt = "+jasyptEncoding(jwt));

		System.out.println("mysqlurl = "+jasyptEncoding(mysqlurl));
		// System.out.println("username = "+jasyptEncoding(username));
		// System.out.println("password = "+jasyptEncoding(password));

	}

	public String jasyptEncoding(String value){

		String key = "64uk64uY7J247KeA66qo66W86rq87JW8Cg==";
		StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
		pbeEnc.setAlgorithm("PBEWithMD5AndDES");
		pbeEnc.setPassword(key);
		return pbeEnc.encrypt(value);

	}

}