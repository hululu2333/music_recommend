package com.hu.server.api;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@RestController
@Log4j2
@RequestMapping("/getMusic")
public class GetMusic {

    /**
     * hello world
     */
    @RequestMapping("/confirm")
    public String confirm(){
        return "hello world";
    }


}
