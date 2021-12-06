package camel;

import config.AppConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class StartApp {
    static final Logger LOG = LoggerFactory.getLogger(StartApp.class);

    public static void main(String[] args) {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfiguration.class);

        try {
            Thread.sleep(7000);
        } catch (InterruptedException failure) {
            LOG.error("Current thread was interrupted. " + failure);
        }
    }
}