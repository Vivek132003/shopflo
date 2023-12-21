package com.example.shopflo;

import org.json.simple.JSONObject;
import org.springframework.cache.annotation.Cacheable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@org.springframework.stereotype.Service
public class Service implements Base
{


    private Connection connection=database.getDataBaseAccess();

    @Override
    public synchronized int  addData(int id, String content) throws SQLException {
        String insertQuery="Insert into postdata (user_id,content) values (?, ?)";
        Integer postId=null;
        try
        {
            PreparedStatement preparedStatement=connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1,id);
            preparedStatement.setString(2,content);
            int isInserted=preparedStatement.executeUpdate();
            if(isInserted>0)
            {
                String postidQuery="select postid from postdata order by postid desc;";
                ResultSet set=connection.createStatement().executeQuery(postidQuery);
                if(set.next())
                {
                    postId=set.getInt(1);
                }
            }
        }
        catch (Exception e)
        {
            throw new SQLException(e.getMessage());
        }
        return  postId;
    }

    @Override
    @Cacheable(value = "postAnalysisData",key="#postId")
    public synchronized JSONObject getAnalysis(int postId) throws SQLException {
        JSONObject object=new JSONObject();
        try
        {
            String postDetailsQuery="select content from postdata where postid = ?";
            PreparedStatement preparedStatement=connection.prepareStatement(postDetailsQuery);
            preparedStatement.setInt(1,postId);
            ResultSet info= preparedStatement.executeQuery();
            String content = new String();
            if(info.next())
            {
                content=info.getString("content");
            }
            if(content.isBlank())throw new SQLException("Invalid post id found");
            String content_data[]=content.split(" ");
            int no_of_words=content_data.length;
            int sum=0;
            for(String word:content_data)
            {
                sum+=word.length();
            }
            object.put("NumberOfWords",no_of_words);
            object.put("AverageWordlength",sum/no_of_words);
        }
        catch (SQLException e)
        {
            throw new SQLException(e.getMessage());
        }
        return object;
    }

    @Override
    @Cacheable(value = "userAnalysisData",key="#userId")
    public synchronized JSONObject getUserData(int userId) throws SQLException {
        JSONObject object=new JSONObject();
        try
        {
            String postDetailsQuery="select postid,content from postdata where user_id = ?";
            PreparedStatement preparedStatement=connection.prepareStatement(postDetailsQuery);
            preparedStatement.setString(1,String.valueOf(userId));
            System.out.println(preparedStatement);
            ResultSet info= preparedStatement.executeQuery();
            List<JSONObject> posts=new ArrayList<>();
            System.out.println(userId);
            while (info.next())
            {
                int post_id=info.getInt("postid");
                String content=info.getString("content");
                JSONObject post_data=new JSONObject();
                String content_data[]=content.split(" ");
                int no_of_words=content_data.length;
                int sum=0;
                for(String word:content_data)
                {
                    sum+=word.length();
                }
                post_data.put("post_id",post_id);
                post_data.put("No_of_words",no_of_words);
                post_data.put("Average_word_length",sum/no_of_words);
                posts.add(post_data);
            }
            object.put("post_details",posts);
        }
        catch (SQLException e)
        {
            throw new SQLException(e.getMessage());
        }
        return object;
    }
}
