package io.zsy.study.zimug;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.*;

/**
 * @author: zsy
 * @date: 2021/1/15 15:29
 * 文件拷贝及剪切的 5 种方法及优缺点
 */
public class CopyCutFileTest {

    /**
     * 传统 IO 进行文件复制(拷贝)
     * 目标文件如果已经存在，会将原文件覆盖掉。重新创建文件并写入
     *
     * @throws IOException
     */
    @Test
    void testCopyFile1() throws IOException {
        File fromFile = new File("D:\\data\\test\\newFile.txt");
        File toFile = new File("D:\\data\\test2\\copyFile.txt");

        try (InputStream inputStream = new FileInputStream(fromFile);
             OutputStream outputStream = new FileOutputStream(toFile)) {
            byte[] buffer = new byte[1024];

            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
                outputStream.flush();
            }
        }
    }

    /**
     * NIO 文件复制(copy)
     *
     * @throws IOException
     */
    @Test
    void testCopyFile2() throws IOException {
        Path fromFile = Paths.get("D:\\data\\test\\newFile.txt");
        Path toFile = Paths.get("D:\\data\\test2\\copiedFile.txt");

        Files.copy(fromFile, toFile);
        // 覆盖已存在的目标文件
        Files.copy(fromFile, toFile, StandardCopyOption.REPLACE_EXISTING);

        // 连同文件属性一起复制(最近修改时间、最近访问时间等)
        CopyOption[] options = {StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES};
        Files.copy(fromFile, toFile, options);
    }

    /**
     * NIO 文件重命名(move)
     *
     * @throws IOException
     */
    @Test
    void testMoveFile() throws IOException {
        Path source = Paths.get("D:\\data\\test\\newFile.txt");
        Path target = Paths.get("D:\\data\\test\\renameFile.txt");
        // 覆盖已存在的目标文件
        Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);

        // 简写
        source = Paths.get("D:\\data\\test\\newFile.txt");
        // 兼容性更好，写法更简单，在不同系统环境兼容文件分隔符
        Files.move(source, source.resolveSibling("renameFile.txt"));
    }

    /**
     * 传统 IO 文件重命名(renameTo)
     *
     * @throws IOException
     */
    @Test
    void testMoveFile2() throws IOException {
        File source = new File("D:\\data\\test\\newFile.txt");
        boolean succeeded = source.renameTo(new File("D:\\data\\test\\renameFile.txt"));
        // 失败返回 false 不会抛出异常，不容易了解失败原因
        System.out.println(succeeded);
    }

    /**
     * NIO 实现文件剪切(move + resolve)
     * @throws IOException
     */
    @Test
    void testCutFile() throws IOException {
        Path fromFile = Paths.get("D:\\data\\test\\newFile.txt");
        Path anotherDir = Paths.get("D:\\data\\test\\anotherDir");

        Files.createDirectories(anotherDir);
        // resolve 会自动连接 anotherDir目录和参数中的文件名，兼容不同系统的文件分隔符
        Files.move(fromFile, anotherDir.resolve(fromFile.getFileName()), StandardCopyOption.REPLACE_EXISTING);
    }
}
