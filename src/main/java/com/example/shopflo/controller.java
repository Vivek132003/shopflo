package com.example.shopflo;

import com.example.shopflo.DTO.RequestDTO;
import com.google.common.util.concurrent.RateLimiter;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/v1/")
public class controller
{
    private final RateLimiter rateLimiter = RateLimiter.create(1.0);
    @Autowired
    Base service;
    @PostMapping(value = "/posts",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JSONObject> create(@RequestBody RequestDTO data)
    {
        JSONObject response =new JSONObject();
        try
        {
            int post_id=service.addData(data.getUserId(), data.getContent());
            response.put("post_id",post_id);
        }
        catch (SQLException e)
        {
            response.put("failure_reason",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping(value = "{id}/analysis")
    public ResponseEntity<JSONObject> fetch(@PathVariable("id") int postId)
    {
        JSONObject response =new JSONObject();
        if(!rateLimiter.tryAcquire())
        {
            response.put("failure_reason","more requests");
            return new ResponseEntity<>(response,HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);
        }
        try
        {
            response=service.getAnalysis(postId);
        }
        catch (SQLException e)
        {
            response.put("failure_reason",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
