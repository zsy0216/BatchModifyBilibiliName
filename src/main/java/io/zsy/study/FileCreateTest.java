package io.zsy.study;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author: zsy
 * @date: 2021/1/3 16:19
 * java io 学习
 */
public class FileCreateTest {
    /**
     * 使用 Files.newBufferedWriter 创建文件并写入
     * 用到了流，使用时需注意要关闭流
     *
     * @throws IOException
     * @since 1.8
     */
    @Test
    void testCreateFile1() throws IOException {
        String fileName = "E:\\Projects\\new.txt";
        Path path = Paths.get(fileName);
        // 这里使用 try-with-resources 方法来关闭流，不用手动关闭
        // 前提是该对象实现了 Closeable 接口，该接口内置自动关闭流方法：close()
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            writer.write("使用 Files.newBufferedWriter 创建并写入文件");
            writer.newLine();
        }

        // 追加写模式
        try (BufferedWriter writer = Files.newBufferedWriter(path,
                StandardCharsets.UTF_8,
                StandardOpenOption.APPEND)) {
            writer.write("可选参数 StandardOpenOption 可以选择创建文件模式-追加等");
        }
    }

    /**
     * 使用Files.write() 创建并写入文件
     * 不需要创建流
     *
     * @throws IOException
     * @since 1.7
     */
    @Test
    void testCreateFile2() throws IOException {
        String fileName = "E:\\Projects\\new2.txt";

        // JDK 1.7 开始
        Files.write(Paths.get(fileName), "使用Files.write() 创建并写入文件".getBytes(StandardCharsets.UTF_8));

        // 追加写模式
        Files.write(Paths.get(fileName), "可选参数 StandardOpenOption 可以选择创建文件模式-追加等".getBytes(
                StandardCharsets.UTF_8), StandardOpenOption.APPEND);
    }

    /**
     * 使用 PrintWriter 创建并写入文件
     * 适合一行一行地写入文件
     *
     * @throws IOException
     * @since 1.5
     */
    @Test
    void testCreateFile3() throws IOException {
        String fileName = "E:\\Projects\\new3.txt";

        try (PrintWriter writer = new PrintWriter(fileName, "UTF-8")) {
            writer.println("使用 PrintWriter 创建并写入文件");
            writer.println("对于一行一行写入的需求比较合适");
        }
    }

    /**
     * 使用File.createNewFile() 创建文件 FileWriter写入文件
     * 创建文件跟写入文件分开
     *
     * @throws IOException
     */
    @Test
    void testCreateFile4() throws IOException {
        String fileName = "E:\\Projects\\new4.txt";

        File file = new File(fileName);

        // true-创建成功 false-文件已存在
        if (file.createNewFile()) {
            System.out.println("文件创建成功");
        } else {
            System.out.println("文件已经存在，不需要重复创建");
        }

        // 使用 FileWriter 追加写入文件
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write("使用 FileWriter 写入文件");
        }
    }

    /**
     * 使用 FileOutputStream 管道流创建文件
     * 使用 BufferWriter 写入文件
     * 比较传统，但是更灵活
     *
     * @throws IOException
     */
    @Test
    void testCreateFile5() throws IOException {
        String fileName = "E:\\Projects\\new5.txt";
        try (FileOutputStream fos = new FileOutputStream(fileName);
             OutputStreamWriter osw = new OutputStreamWriter(fos);
             BufferedWriter bw = new BufferedWriter(osw)) {
            bw.write("使用 FileOutputStream 管道流创建文件");
            bw.newLine();
            bw.write("使用 BufferWriter 写入文件");
            bw.flush();
        }
    }
}
