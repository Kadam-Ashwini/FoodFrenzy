/*
 * package com.app.food;
 * 
 * import org.springframework.boot.SpringApplication; import
 * org.springframework.boot.autoconfigure.SpringBootApplication;
 * 
 * @SpringBootApplication public class FoodFrenzyApplication {
 * 
 * public static void main(String[] args) {
 * SpringApplication.run(FoodFrenzyApplication.class, args); //
 * http://localhost:8088/ }
 * 
 * }
 */


package com.app.food;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EntityScan(basePackages = "com.app.food.model")
@EnableScheduling
public class FoodFrenzyApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoodFrenzyApplication.class, args);
    }
}