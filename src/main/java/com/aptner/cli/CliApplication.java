package com.aptner.cli;

import com.aptner.v3.global.config.S3ClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(S3ClientConfig.class)
public class CliApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(CliApplication.class);
        ConfigurableApplicationContext context = app.run(args);
        context.getBean(ResizeRunner.class).run(args);
        context.close();
    }
}
