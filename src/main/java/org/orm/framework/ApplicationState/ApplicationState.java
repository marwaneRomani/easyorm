package org.orm.framework.ApplicationState;

import java.io.IOException;
import java.net.URL;

import org.json.simple.parser.ParseException ;

import org.orm.framework.ConfigReader.ConfigReader;

public class ApplicationState {

    private static ApplicationState state = null;

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


    private ApplicationState() {

    }

    public static ApplicationState getState() {
        if (state == null) {
            state = new ApplicationState();
            state.readConfig();
        }
        return state;
    }

    private void readConfig() {
        try {
            URL resource = getClass().getClassLoader().getResource("config.json");

            this.configPath = resource.getPath();

            ConfigReader configReader = new ConfigReader(configPath);

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

    public String getDialect() { return dialect; }

    public void setDialect(String dialect) { this.dialect = dialect; }
}
