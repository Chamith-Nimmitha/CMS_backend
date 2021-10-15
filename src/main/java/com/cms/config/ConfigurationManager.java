package com.cms.config;

import com.cms.utils.Json;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.FileReader;
import java.io.IOException;

public class ConfigurationManager {

    private static Configuration configuration;
    private static ConfigurationManager configurationManager;

    public static ConfigurationManager getInstance(){
        if(configurationManager == null){
            configurationManager = new ConfigurationManager();
        }
        return configurationManager;
    }

    public Configuration loadConfiguration(String filepath) throws IOException {

        FileReader fileReader = new FileReader(filepath);
        StringBuffer stringBuffer = new StringBuffer();

        int i;
        while( (i=fileReader.read()) != -1){
            stringBuffer.append((char)i);
        }

        JsonNode node = Json.parse(stringBuffer.toString());
        configuration = Json.fromJson(node, Configuration.class);
        return configuration;
    }

    public Configuration getCurrentConfiguration(){
        return configuration;
    }
}
