package config;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.springframework.stereotype.Component;

@Component
public class CamelContextConfig extends CamelConfiguration {
    @Override
    protected void setupCamelContext(CamelContext camelContext) throws Exception {
        camelContext.getPropertiesComponent().setLocation("camel.properties");
    }
}
