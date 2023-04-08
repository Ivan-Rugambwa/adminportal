package com.example.javazeebee;


import com.example.javazeebee.message.dto.PublishRequest;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("api/message")
public class MessageController {

    @Autowired
    MessageService service;
    @PostMapping
    public ResponseEntity<?> publish(@RequestBody PublishRequest request, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws Exception {

        if (!service.isTokenValid(token)) return ResponseEntity.status(401).body("JWT is not valid");

        var key = service.publish(request);

        service.updateSeat(request, token);

        return ResponseEntity.ok().build();
    }
}
