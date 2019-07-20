package com.stylefeng.guns;

import com.stylefeng.guns.rest.AlipayApplication;
import com.stylefeng.guns.rest.common.util.FtpUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AlipayApplication.class)
public class GunsRestApplicationTests {

	@Autowired
	private FtpUtil ftpUtil;

	@Test
	public void contextLoads() {
//		String fileStrByPath = ftpUtil.getFileStrByPath("seats/123214.json");
//		System.out.println(fileStrByPath);
		File file = new File("C:\\Users\\GCC\\Desktop\\qrcode\\qr-72781b5ad8af4b94a33725a975d0fa51.png");
		boolean isSuccess = ftpUtil.uploadFile(file.getName(), file);
		System.out.println(isSuccess);
	}

}
