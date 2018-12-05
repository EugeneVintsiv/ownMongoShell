package io.github.eugenevintsiv;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.shell.jline.PromptProvider;

import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
public class SpringShellSample {

    public static void main(String[] args) throws Exception {
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        ConfigurableApplicationContext context = SpringApplication.run(SpringShellSample.class, args);
    }

    @Bean
    public PromptProvider myPromptProvider() {
        return () -> new AttributedString("sqlToMongo-shell:>", AttributedStyle.BOLD.foreground(AttributedStyle.CYAN));
    }
}