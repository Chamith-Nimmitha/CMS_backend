package com.cms;

import com.cms.config.Configuration;
import com.cms.config.ConfigurationManager;
import com.cms.services.UserService;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        System.out.println("Load configurations");
        ConfigurationManager configurationManager = ConfigurationManager.getInstance();
        Configuration configuration = null;
        try {
            configuration = configurationManager
                    .loadConfiguration("src/main/resources/applicationConfig.json");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Configuration manager failed in Main");
            System.exit(1);
        }

        System.out.println("start server");
        Rapidoid rd = new Rapidoid();
        rd.runServer();
        System.out.println("Working....");
    }
}
