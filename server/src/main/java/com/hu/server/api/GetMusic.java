package com.hu.server.api;

import com.hu.server.model.dto.ResponseDto;
import com.hu.server.model.entity.MusicInfo;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.Response;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

@RestController
@Log4j2
@RequestMapping("/getMusic")
public class GetMusic {

    /**
     * hello world
     */
    @RequestMapping("/confirm")
    public ResponseDto<ArrayList<MusicInfo>> confirm(){

        ArrayList<MusicInfo> list = new ArrayList<>();
        list.add(new MusicInfo());
        list.add(new MusicInfo());
        list.add(new MusicInfo());

        return new ResponseDto<>(list);
    }



}
