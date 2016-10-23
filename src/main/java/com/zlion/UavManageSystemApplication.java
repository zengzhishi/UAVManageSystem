package com.zlion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class UavManageSystemApplication extends SpringBootServletInitializer{

//	@RequestMapping("/")
//	public String home() {
//		return "Hello Docker World."
//				+ "<br />Welcome to <a href='http://waylau.com'>zlion.com</a></li>";
//	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(UavManageSystemApplication.class);
	}

//	public static void main(String[] args) {
//		SpringApplication app = new SpringApplication(UavManageSystemApplication.class);
//		app.setBanner(new Banner() {
//			@Override
//			public void printBanner(Environment environment, Class<?> aClass, PrintStream printStream) {
//				printStream.print("\n\n>>>>>This is the uav manage system banner!\n\n");
//			}
//		});
//		app.addListeners(new ApplicationListener<ApplicationEvent>() {
//			@Override
//			public void onApplicationEvent(ApplicationEvent applicationEvent) {
//
//			}
//		});
//		app.run(args);
//	}

	public static void main(String[] args) {
		SpringApplication.run(UavManageSystemApplication.class, args);
	}

//	@Bean
//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurerAdapter() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/*");
//			}
//		};
//	}
}
