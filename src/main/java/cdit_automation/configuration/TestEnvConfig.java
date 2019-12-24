package cdit_automation.configuration;


import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.net.URL;

@Configuration
public class TestEnvConfig {

//    @Bean
//    public static PropertySourcesPlaceholderConfigurer createPropertySource() {
//        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
//        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
//
//        URL path = TestEnvConfig.class.getClassLoader().getResource("application.yaml");
//        if ( path != null ) {
//            String yamlConfiguration = path.getPath();
//            Resource yamlResource = new FileSystemResource(yamlConfiguration);
//            yaml.setResources(yamlResource);
//            configurer.setProperties(yaml.getObject());
//            return configurer;
//        }
//        else {
//            return null;
//        }
//    }
}
