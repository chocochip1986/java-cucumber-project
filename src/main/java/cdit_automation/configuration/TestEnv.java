package cdit_automation.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties
@EnableConfigurationProperties()
public class TestEnv {

    @Value(value = "${spring.profiles}")
    private String springProfile;

    @Value(value = "${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    @Value(value = "${spring.jpa.show-sql}")
    private String showSql;

    @Value(value = "${spring.jpa.database-platform}")
    private String databasePlatform;

    @Value(value = "${spring.datasource.url}")
    private String url;

    @Value(value = "${spring.datasource.username}")
    private String datasourceDbUsername;

    @Value(value = "${spring.datasource.password}")
    private String datasourceDbPassword;

    @Value(value = "${spring.datasource.driver-class-name}")
    private String datasourceDbDriver;

    @Value(value = "${spring.output.ansi.enabled}")
    private String springOutputAnsiColor;

    @Value(value = "${test-env.env-name}")
    private String env;

    @Value(value = "${test-env.datasource-ui.url}")
    private String datasourceUiUrl;

    @Value(value = "${test-env.datasource-ui.port}")
    private String datasourceUiPort;

    @Value(value = "${test-env.implicit-wait}")
    private Long implicitWait;

    @Value(value = "${test-env.explicit-wait}")
    private Long explicitWait;

    public String toString() {
        String str = "spring.profiles: "+springProfile+"\n"
                + "spring.jpa.hibernate.ddl-auto: "+ddlAuto+"\n"
                +"spring.jpa.show-sql: "+showSql+"\n"
                +"spring.jpa.database-platform: "+databasePlatform+"\n"
                +"spring.datasource.url: "+url+"\n"
                +"spring.datasource.username: "+datasourceDbUsername+"\n"
                +"spring.datasource.password: "+datasourceDbPassword+"\n"
                +"spring.datasource.driver-class-name: "+datasourceDbDriver+"\n"
                +"spring.output.ansi.enabled: "+springOutputAnsiColor+"\n"
                +"test-env.env-name: "+env+"\n"
                +"test-env.datasource-ui.url: "+datasourceUiUrl+"\n"
                +"test-env.datasource-ui.port: "+datasourceUiPort+"\n"
                +"test-env.implicit-wait: "+implicitWait+"\n"
                +"test-env.explicit-wait: "+explicitWait;
        return str;
    }
}
