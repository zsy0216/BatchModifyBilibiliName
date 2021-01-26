package io.zsy.study.zimug;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * @author: zsy
 * @date: 2021/1/26 16:59
 */
public class DeleteFileTest {

    /**
     * 传统 IO File.delete()
     * 成功-true
     * 失败-false 不同给出失败原因
     */
    @Test
    void testDeleteFile1() {
        File file = new File("D:\\data\\test");
        boolean delete = file.delete();
    }

    /**
     * 传统 IO File.deleteOnExit()
     * 成功-void
     * 失败-void
     */
    @Test
    void testDeleteFile2() {
        File file = new File("D:\\data\\test");
        file.deleteOnExit();
    }

    /**
     * NIO Files.delete()
     * 成功-void
     * 失败-NoSuchFileException/DirectoryNotEmptyException
     *
     * @throws IOException
     */
    @Test
    void testDeleteFile3() throws IOException {
        Path path = Paths.get("D:\\data\\test");
        Files.delete(path);
    }

    /**
     * NIO Files.delete()
     * 成功-true
     * 失败(文件夹不存在)-false
     * 失败(文件夹有子文件夹)-DirectoryNotEmptyException
     *
     * @throws IOException
     */
    @Test
    void testDeleteFile4() throws IOException {
        Path path = Paths.get("D:\\data\\test");
        Files.deleteIfExists(path);
    }

    private void createMoreFiles() {
    }

    @Test
    void testDeleteFile5() throws IOException {
        createMoreFiles();
        Path path = Paths.get("D:\\data\\test1\\test2");

        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            // 先遍历删除文件
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                System.out.printf("文件被删除: %s%n", file);
                return FileVisitResult.CONTINUE;
            }

            // 再遍历删除目录
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                // 此时文件已被删除，目录为空目录
                Files.delete(dir);
                System.out.printf("文件夹被删除: %s%n", dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @Test
    void testDeleteFile6() throws IOException {
        createMoreFiles();
        Path path = Paths.get("D:\\data\\test1\\test2");

        try (Stream<Path> walk = Files.walk(path)) {
            // 按字符串排序，倒序时文件会排在目录前面，会先删除文件再删除目录
            walk.sorted(Comparator.reverseOrder())
                    .forEach(DeleteFileTest::deleteDirectoryStream);
        }
    }

    private static void deleteDirectoryStream(Path path) {
        try {
            Files.delete(path);
            System.out.printf("删除文件成功: %s%n", path.toString());
        } catch (IOException e) {
            System.out.printf("无法删除的路径: %s%n%s", path, e);
        }
    }

    /**
     * 传统 IO 递归删除
     *
     * @throws IOException
     */
    @Test
    void testDeleteFile7() throws IOException {
        createMoreFiles();
        File file = new File("D:\\data\\test1\\test2");
        deleteDirectoryLegacyIO(file);
    }

    private void deleteDirectoryLegacyIO(File file) {
        // 无法做到 遍历多层文件夹数据
        File[] files = file.listFiles();

        // 递归调用
        if (files != null) {
            for (File temp : files) {
                deleteDirectoryLegacyIO(temp);
            }
        }

        if (file.delete()) {
            System.out.printf("删除成功: %s%n", file);
        } else {
            System.out.printf("删除失败: %s%n", file);
        }
    }
}
