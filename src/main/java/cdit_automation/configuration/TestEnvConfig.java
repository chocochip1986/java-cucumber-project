package cdit_automation.configuration;


import org.springframework.context.annotation.Configuration;

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
