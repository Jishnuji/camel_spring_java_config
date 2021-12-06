package config;

import bitronix.tm.BitronixTransactionManager;
import bitronix.tm.TransactionManagerServices;
import bitronix.tm.resource.jdbc.PoolingDataSource;
import bitronix.tm.resource.jms.PoolingConnectionFactory;
import org.apache.camel.component.jms.JmsComponent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.jta.JtaTransactionManager;

import java.util.Map;
import java.util.Properties;

@Configuration
@PropertySource("classpath:spring.properties")
@ComponentScan(basePackages = {"routes", "config"})
public class AppConfiguration {
    @Value("${brokerURL}")
    private String brokerURL;

    @Value("${connectionFactoryName}")
    private String connectionFactoryName;

    @Value("${dataSourceName}")
    private String dataSourceName;

    @Value("#{${connectionProperties}}")
    private Map<String, String> connectionProperties;


    @Bean(name = "transactionManager")
    public JtaTransactionManager createTransactionManager() {
        bitronix.tm.Configuration BTMconfig = TransactionManagerServices.getConfiguration();
        BTMconfig.setServerId("spring-btm");
        BitronixTransactionManager BitronixTM =  TransactionManagerServices.getTransactionManager();

        JtaTransactionManager JTAtm = new JtaTransactionManager();
        JTAtm.setUserTransaction(BitronixTM);
        JTAtm.setTransactionManager(BitronixTM);
        return JTAtm;
    }

    @Bean(name = "jms")
    public JmsComponent connectToJMS() {
        PoolingConnectionFactory myConnectionFactory = new PoolingConnectionFactory ();
        myConnectionFactory.setClassName(connectionFactoryName);
        myConnectionFactory.setUniqueName("jms");
        myConnectionFactory.setMaxPoolSize(5);
        myConnectionFactory.setAllowLocalTransactions(true);
        myConnectionFactory.getDriverProperties().setProperty("brokerURL", brokerURL);

        JmsComponent jms = new JmsComponent();
        jms.setConnectionFactory(myConnectionFactory);
        return jms;
    }

    @Bean(name = "xaDataSource")
    public PoolingDataSource connectToXADataSource() {
        PoolingDataSource xaDataSource = new PoolingDataSource();
        xaDataSource.setClassName(dataSourceName);
        xaDataSource.setUniqueName("xaDataSource");
        xaDataSource.setMinPoolSize(1);
        xaDataSource.setMaxPoolSize(5);
        xaDataSource.setAllowLocalTransactions(true);
        xaDataSource.setAutomaticEnlistingEnabled(false);
        xaDataSource.setDriverProperties(addConnectionProperties());
        return xaDataSource;
    }

    public Properties addConnectionProperties() {
        Properties xaDataSourceProp = new Properties();
        xaDataSourceProp.putAll(connectionProperties);
        return xaDataSourceProp;
    }
}
