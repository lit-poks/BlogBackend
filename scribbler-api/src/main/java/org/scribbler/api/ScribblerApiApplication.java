package org.scribbler.api;



import org.scribbler.service.ServiceConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(ServiceConfiguration.class)
public class ScribblerApiApplication {
    public static void main(String [] args){
        SpringApplication.run(ScribblerApiApplication.class,args);
    }
}
