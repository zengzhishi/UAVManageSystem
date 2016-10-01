package com.zlion;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.PrintStream;

@SpringBootApplication
public class UavManageSystemApplication {

//	@RequestMapping("/")
//	public String home() {
//		return "Hello Docker World."
//				+ "<br />Welcome to <a href='http://waylau.com'>zlion.com</a></li>";
//	}

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(UavManageSystemApplication.class);
		app.setBanner(new Banner() {
			@Override
			public void printBanner(Environment environment, Class<?> aClass, PrintStream printStream) {
				printStream.print("\n\n>>>>>This is the uav manage system banner!\n\n");
			}
		});
		app.addListeners(new ApplicationListener<ApplicationEvent>() {
			@Override
			public void onApplicationEvent(ApplicationEvent applicationEvent) {

			}
		});
		app.run(args);
	}
}
