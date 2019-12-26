import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zsy.BatchModifyFileName;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试类，没有实际用途；
 * 具体功能请查看 com.zsy.BatchModifyFileName.java
 */
public class TestMethod {
    public static void main(String[] args) {
      /*  String test = " \"abc\"";
        String str = test.split("\"")[0];
        // System.out.println(str);

        String fileName = "75233634.info";
        String fileType = "info";
        // System.out.println(fileName.endsWith(fileType));

        *//*File file1 = new File("75233634_1_0.flv");
        File file2 = new File("\"test2.flv\"");
        if (file1.renameTo(file2)){
            System.out.println("success!");
        }*//*

        List<String> testStringList = new ArrayList<>();

        testStringList.add("123");
        testStringList.add("asvcsad");
        testStringList.add("sd32dqd1");

        System.out.println(testStringList);
        System.out.println("d:\\a\\" +"1_" + testStringList.get(1) + ".flv");*/

        File file = new File("71110355.info");
        String name = BatchModifyFileName.partNameByMap(file);

        System.out.println(name);
    }

}
