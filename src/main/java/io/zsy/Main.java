package io.zsy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.zsy.bilibili.model.Info;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author: zsy
 * @date: 2021/1/10 13:15
 */
public class Main {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("E:\\Videos\\Bilibli Videos\\test\\1\\83133496.info");
        String info = new String(Files.readAllBytes(path));
        System.out.println(info);
        Info infoObj = JSONObject.parseObject(info, Info.class);
        System.out.println(infoObj.getPartName());
    }
}
