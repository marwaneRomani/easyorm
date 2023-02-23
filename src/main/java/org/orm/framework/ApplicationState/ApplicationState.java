package org.orm.framework.ApplicationState;

import java.io.IOException;
import org.json.simple.parser.ParseException ;

import org.orm.framework.ConfigReader.ConfigReader;

public class ApplicationState {

    private String modelsPath;


    private String configPath;

    private String url;
    private String username;
    private String password;
    private String strategy;
    private String dialect ;

    private Integer connectionPoolMaxSize;

    private Boolean isCacheEnabled;
    private Integer cacheMaxSize;
    private Integer cacheExpireAfter;

    private Boolean isTransactionManagerEnabled;


    private ApplicationState(String configPath) {
        this.configPath = configPath;
    }

    public static ApplicationState getState(String configPath) {
        ApplicationState state = new ApplicationState(configPath);
        state.readConfig();

        return state;
    }

    private void readConfig() {
        try {
            ConfigReader configReader = new ConfigReader(this.configPath);

            modelsPath = configReader.getModelsPath();

            url = configReader.getDatabaseUrl();
            username = configReader.getDatabaseUsername();
            password = configReader.getDatabasePassword();
            strategy = configReader.getStrategy();
            dialect = configReader.getDialect();

            connectionPoolMaxSize = configReader.getConnectionPoolMaxSize();

            isCacheEnabled = configReader.isCacheEnabled();
            cacheMaxSize = configReader.getCacheMaxSize();
            cacheExpireAfter = configReader.getCacheExpireAfter();

            isTransactionManagerEnabled = configReader.isTransactionManagerEnabled();

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public String getModelsPath() {
        return modelsPath;
    }


    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getStrategy() {
        return strategy;
    }

    public Integer getConnectionPoolMaxSize() {
        return connectionPoolMaxSize;
    }

    public Boolean getCacheEnabled() {
        return isCacheEnabled;
    }

    public Integer getCacheMaxSize() {
        return cacheMaxSize;
    }

    public Integer getCacheExpireAfter() {
        return cacheExpireAfter;
    }

    public Boolean getTransactionManagerEnabled() {
        return isTransactionManagerEnabled;
    }

    public String getDialect() { return dialect; }

    public void setDialect(String dialect) { this.dialect = dialect; }
}
