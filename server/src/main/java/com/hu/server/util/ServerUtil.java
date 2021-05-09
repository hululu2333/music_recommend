package com.hu.server.util;

import com.hu.server.model.entity.MusicInfo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServerUtil {
    public static ArrayList<MusicInfo> rsToArray(ResultSet rs) throws SQLException {
        ArrayList<MusicInfo> list = new ArrayList<>();

        while(rs.next()){
            MusicInfo mi = new MusicInfo();
            mi.setMusicId(rs.getString("musicId"));
            mi.setMusicName(rs.getString("musicName"));
            mi.setStorePath(rs.getString("storePath"));
            mi.setSinger(rs.getString("singer"));

            list.add(mi);
        }

        return list;
    }
}
