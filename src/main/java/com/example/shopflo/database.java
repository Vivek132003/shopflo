package com.example.shopflo;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;

public class database {


    private database() {}

    private static Connection connection=null;
    public static Connection getDataBaseAccess()
    {
        if(connection==null)
        {
            connection=getConnection();
        }
        return connection;
    }
    private static Connection getConnection() {

        try(InputStream input = new FileInputStream("src/main/resources/database.yaml")){
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(input);

            Map<String, Object> spring = (Map<String, Object>) data.get("spring");
            Map<String, Object> datasource = (Map<String, Object>) spring.get("datasource");

            String url=(String) datasource.get("url");
            String username=(String) datasource.get("username");
            String password=(String) datasource.get("password");
            connection= DriverManager.getConnection(url, username, password);

        } catch (Exception e) {
            System.out.println("connection failed "+e.getMessage());
        }
        return connection;
    }
}
