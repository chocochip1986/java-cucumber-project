package cds_automation.configuration.datasource;

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
    @Getter
    public enum Env {
        LOCAL("local"),
        QA("qa"),
        AUTOMATION("automation");

        private String name;

        Env(String name) {
            this.name = name;
        }
    }

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
    private Env env;

    @Value(value = "${test-env.datasource-ui.url}")
    private String datasourceUiUrl;

    @Value(value = "${test-env.datasource-ui.port}")
    private String datasourceUiPort;

    @Value(value = "${test-env.datasource.url}")
    private String datasourceUrl;

    @Value(value = "${test-env.datasource.port}")
    private String datasourcePort;

    @Value(value = "${test-env.datasource-ui.username}")
    private String datasourceUiUsername;

    @Value(value = "${test-env.datasource-ui.password}")
    private String datasourceUiPassword;

    @Value(value = "${test-env.implicit-wait}")
    private Long implicitWait;

    @Value(value = "${test-env.explicit-wait}")
    private Long explicitWait;

    @Value(value = "${test-env.wait}")
    private Double wait;

    @Value(value = "${aws.sns.topic-arn}")
    private String topicArn;

    @Value(value = "${test-env.fail-fast}")
    private boolean failFast;

    public String toString() {
        String str = "spring.profiles: "+springProfile+System.lineSeparator()
                + "spring.jpa.hibernate.ddl-auto: "+ddlAuto+System.lineSeparator()
                +"spring.jpa.show-sql: "+showSql+System.lineSeparator()
                +"spring.jpa.database-platform: "+databasePlatform+System.lineSeparator()
                +"spring.datasource.url: "+url+System.lineSeparator()
                +"spring.datasource.username: "+datasourceDbUsername+System.lineSeparator()
                +"spring.datasource.password: "+datasourceDbPassword+System.lineSeparator()
                +"spring.datasource.driver-class-name: "+datasourceDbDriver+System.lineSeparator()
                +"spring.output.ansi.enabled: "+springOutputAnsiColor+System.lineSeparator()
                +"test-env.env-name: "+env+System.lineSeparator()
                +"test-env.datasource-ui.url: "+datasourceUiUrl+System.lineSeparator()
                +"test-env.datasource-ui.port: "+datasourceUiPort+System.lineSeparator()
                +"test-env.datasource.url: "+datasourceUrl+System.lineSeparator()
                +"test-env.datasource.port: "+datasourcePort+System.lineSeparator()
                +"test-env.datasource.username: "+datasourceUiUsername+System.lineSeparator()
                +"test-env.datasource.password: "+datasourceUiPassword+System.lineSeparator()
                +"test-env.implicit-wait: "+implicitWait+System.lineSeparator()
                +"test-env.explicit-wait: "+explicitWait+System.lineSeparator()
                +"test-env.fail-fast: "+failFast+System.lineSeparator()
                +"aws.sns.topic-arn: "+topicArn;
        return str;
    }
}
