package org.lomo.study.log4jstudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

@SpringBootApplication
public class Main {

    static Logger log = Logger.getLogger(MyController.class.getName());


    public static void main( String[] args )
        {
            log.info("abcde");
            log.info("abcde");
            SpringApplication.run(Main.class, args);
            log.info("abcde");
            log.info("abcde");
            System.out.println(111);
        }

}