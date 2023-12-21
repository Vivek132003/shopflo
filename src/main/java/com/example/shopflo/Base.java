package com.example.shopflo;

import org.json.simple.JSONObject;

import java.sql.SQLException;

public interface Base
{
    public int addData(int id,String content) throws SQLException;
    public JSONObject getAnalysis(int postId) throws SQLException;

    public JSONObject getUserData(int userId) throws SQLException;
}
