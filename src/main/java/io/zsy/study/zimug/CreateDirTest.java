package io.zsy.study.zimug;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author: zsy
 * @date: 2021/1/12 21:16
 * 创建文件夹的四种方法及优缺点
 */
public class CreateDirTest {

    /**
     * 缺点：对异常处理不好，不能反馈是哪种原因导致文件夹创建失败
     */
    @Test
    void testCreateDir1() {
        // "E:/data111" 目录不存在
        String dirPath = "E:\\data111\\test";
        File directory = new File(dirPath);

        // mkdir
        boolean hasSucceeded = directory.mkdir();
        System.out.println("创建文件夹结果(不含父文件夹): " + hasSucceeded);

        // mkdirs
        hasSucceeded = directory.mkdirs();
        System.out.println("创建文件夹结果(包含父文件夹): " + hasSucceeded);
    }

    /**
     * 优点：对失败原因进行异常返回，更加清晰
     * @throws IOException
     */
    @Test
    void testCreateDir2() throws IOException {
        // "E:/data222" 目录不存在
        String dirPath = "E:\\data222\\test";

        Path path = Paths.get(dirPath);
        // NoSuchFileException
        Files.createDirectory(path);

        Files.createDirectories(path);
    }
}
