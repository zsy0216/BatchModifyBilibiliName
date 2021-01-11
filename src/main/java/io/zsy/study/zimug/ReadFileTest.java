package io.zsy.study.zimug;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: zsy
 * @date: 2021/1/11 21:19
 * java io 学习 - 读取文件数据的 6 种方法
 */
public class ReadFileTest {

    /**
     * Scanner(Java1.5) String, Int 类型等按分隔符读数据
     *
     * @throws IOException
     */
    @Test
    void readFile1() throws IOException {
        String fileName = "E:\\Projects\\new.txt";

        // 按行读取字符串
        try (Scanner sc = new Scanner(new FileReader(fileName))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                System.out.println(line);
            }
        }
        // 按分隔符读取字符串
        try (Scanner sc = new Scanner(new FileReader(fileName))) {
            sc.useDelimiter("\\|");
            while (sc.hasNext()) {
                String str = sc.next();
                System.out.println(str);
            }
        }
        // sc.hasNextInt(), sc.hasNextFloat() 按分隔符读取基础数据类型...
    }

    /**
     * Files.lines, 返回 Stream(Java8) 流式数据处理, 按行读取
     *
     * @throws IOException
     */
    @Test
    void readFile2() throws IOException {
        String fileName = "E:\\Projects\\new.txt";

        // 读取文件内容到 Stream 流中，按行读取
        Stream<String> lines = Files.lines(Paths.get(fileName));

        // 随机行顺序进行数据处理
        lines.forEach(element -> {
            System.out.println(element);
        });

        // 按文件行顺序进行处理
        lines.forEachOrdered(System.out::println);

        // 转换成 List<String> 要注意文件太大造成 java.lang.OutOfMemoryError: Java heap space
        List<String> collect = lines.collect(Collectors.toList());
    }

    /**
     * Files.readAllLines, 返回 List<String> (Java8)
     *
     * @throws IOException
     */
    @Test
    void readFile3() throws IOException {
        String fileName = "E:\\Projects\\new.txt";

        // 转换成 List<String> 要注意文件太大造成 java.lang.OutOfMemoryError: Java heap space
        List<String> lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
        lines.forEach(System.out::println);
    }

    /**
     * Files.readString, 读取 String(Java11), 文件最大 2G
     *
     * @throws IOException
     */
    @Test
    void readFile4() throws IOException {
        String fileName = "E:\\Projects\\new.txt";
        // Java 11 开始提供的方法，读取文件不能超过 2G, 与主机内存息息相关
        // Files.readString(Paths.get(fileName));
    }

    /**
     * Files.readAllBytes, 读取 byte[] (Java7), 文件最大 2G
     *
     * @throws IOException
     */
    @Test
    void readFile5() throws IOException {
        String fileName = "E:\\Projects\\new.txt";
        // 如果是 JDK 11, 用上面的方法，否则用此方法比较容易
        byte[] bytes = Files.readAllBytes(Paths.get(fileName));

        String s = new String(bytes, StandardCharsets.UTF_8);
        System.out.println(s);
    }

    @Test
    void readFile6() throws IOException, ClassNotFoundException {
        String fileName = "E:\\Projects\\new.txt";

        // 带缓冲的流读取，默认缓冲区 8K
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }

        // Java8 也可以这样写
        try (BufferedReader br = Files.newBufferedReader(Paths.get(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }

        // 读取文件中的 Java 对象
        try (FileInputStream fis = new FileInputStream(fileName)) {
            ObjectInputStream ois = new ObjectInputStream(fis);
            ois.readObject();
        }
    }
}
