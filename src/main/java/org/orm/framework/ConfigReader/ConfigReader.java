package org.orm.framework.ConfigReader;

import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class ConfigReader {
        private JSONObject jsonObject;

        public ConfigReader(String filePath) throws IOException, ParseException {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(filePath));
            jsonObject = (JSONObject) obj;
        }

        public String getModelsPath() {
            return  (String) jsonObject.get("models");
        }

        public String getDatabaseUrl() {
            JSONObject dbConfig = (JSONObject) jsonObject.get("database");
            return (String) dbConfig.get("url");
        }

        public String getDatabaseUsername() {
            JSONObject dbConfig = (JSONObject) jsonObject.get("database");
            return (String) dbConfig.get("username");
        }

        public String getDatabasePassword() {
            JSONObject dbConfig = (JSONObject) jsonObject.get("database");
            return (String) dbConfig.get("password");
        }

        public String getStrategy() {
            JSONObject dbConfig = (JSONObject) jsonObject.get("database");
            return (String) dbConfig.get("strategy");
        }

        public String getDialect() {
            JSONObject dbConfig = (JSONObject) jsonObject.get("database");
            return (String) dbConfig.get("dialect");
        }


    public int getConnectionPoolMaxSize() {
            JSONObject connectionPoolConfig = (JSONObject) jsonObject.get("connectionPool");
            return Integer.parseInt(connectionPoolConfig.get("maxSize").toString());
        }

        public boolean isCacheEnabled() {
            JSONObject cacheConfig = (JSONObject) jsonObject.get("cache");
            return (boolean) cacheConfig.get("enabled");
        }

        public int getCacheMaxSize() {
            JSONObject cacheConfig = (JSONObject) jsonObject.get("cache");
            return Integer.parseInt(cacheConfig.get("maxSize").toString());
        }

        public int getCacheExpireAfter() {
            JSONObject cacheConfig = (JSONObject) jsonObject.get("cache");
            return Integer.parseInt(cacheConfig.get("expireAfter").toString());
        }

        public boolean isTransactionManagerEnabled() {
            JSONObject tmConfig = (JSONObject) jsonObject.get("transactionManager");
            return (boolean) tmConfig.get("enabled");
        }
    }



//try {
//        ConfigReader configReader = new ConfigReader("config.json");
//        String databaseUrl = configReader.getDatabaseUrl();
//        String databaseUsername = configReader.getDatabaseUsername();
//        String databasePassword = configReader.getDatabasePassword();
//        int connectionPoolMaxSize = configReader.getConnectionPoolMaxSize();
//        boolean isCacheEnabled = configReader.isCacheEnabled();
//        int cacheMaxSize = configReader.getCacheMaxSize();
//        int cacheExpireAfter = configReader.getCacheExpireAfter();
//        boolean isTransactionManagerEnabled = configReader.isTransactionManagerEnabled();
//
//        // Use the configuration values as needed...
//        } catch (IOException | ParseException e) {
//        e.printStackTrace();
//        System.exit(1);
//        }
