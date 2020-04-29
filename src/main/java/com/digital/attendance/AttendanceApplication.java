package com.digital.attendance;


import io.micrometer.core.instrument.util.JsonUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.io.IOException;

@EnableScheduling
@SpringBootApplication
public class AttendanceApplication extends SpringBootServletInitializer{


	public static void main(String[] args) throws IOException {
//		byte[] bytes = FileUtils.readFileToByteArray(new File("C:\\Users\\LONGBRIDE\\Desktop\\myprojects\\backendDigitalAttendance-master\\ssl_server.jks"));
//		String bdata = new String(Base64.encodeBase64(bytes, true));
//		System.out.println(bdata);
		SpringApplication.run(AttendanceApplication.class, args);
//		System.out.println(System.getProperties().toString());
	}



}

