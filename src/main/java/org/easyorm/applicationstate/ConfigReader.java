package org.easyorm.applicationstate;

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

        public String getFkStrategy() {
            JSONObject dbConfig = (JSONObject) jsonObject.get("database");
            return dbConfig.get("fk_modified").toString();
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