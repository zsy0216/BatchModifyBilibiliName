package io.zsy;

import com.alibaba.fastjson.JSONObject;
import io.zsy.bilibili.model.Info;
import io.zsy.bilibili.model.MagicValue;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Pattern;

/**
 * @author: zsy
 * @date: 2021/1/10 13:15
 */
public class Main implements MagicValue {
    /**
     * 视频目录
     */
    final static Path PATH = Paths.get("E:\\Videos\\Bilibli Videos\\42264659");

    /**
     * 文件夹名特殊字符判断
     */
    final static Pattern PATTERN = Pattern.compile("[\\\\/:*?\"<>|]");

    /**
     * 视频名
     */
    private static String videoName;

    /**
     * 改名后视频存放目录
     */
    private static Path newPath;

    public static void main(String[] args) throws IOException {
        // 获取下载视频的视频名
        getVideoName();
        System.out.println("本视频名为: " + videoName);

        // 遍历视频目录
        Files.walkFileTree(PATH, new SimpleFileVisitor<Path>() {
            // 读取出来的 分p号 分p视频名
            String partNo = null;
            String partName = null;

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println("当前所在视频目录: " + file.getParent());
                String fileName = file.getFileName().toFile().getName();
                // 当前文件是分p视频基本信息文件
                if (fileName.endsWith(SUFFIX_INFO)) {
                    Info videoInfo = getVideoInfo(file);
                    partNo = videoInfo.getPartNo();
                    partName = videoInfo.getPartName();
                    System.out.println("视频编号: " + partNo + ", 视频名称: " + partName);
                    Files.delete(file);
                    System.out.println("分p文件: " + file.getFileName() + " 已被删除");
                }
                // 当前文件是视频文件，改名，移动到一级目录
                else if (fileName.endsWith(SUFFIX_MP4) || fileName.endsWith(SUFFIX_FLV)) {
                    String extension = fileName.split("\\.")[1];
                    String newName = partNo + "_" + partName + "." + extension;
                    System.out.println("新视频名: " + newName + "");
                    Files.move(file, newPath.resolve(newName));
                }
                // 弹幕文件，直接删除
                else if (fileName.endsWith(SUFFIX_XML)) {
                    if (Files.exists(file)) {
                        Files.delete(file);
                        System.out.println("弹幕文件: " + file.getFileName() + " 已被删除");
                    }
                } else {
                    if (Files.exists(file)) {
                        Files.delete(file);
                        System.out.println("未知文件: " + file.getFileName() + " 已被删除");
                    }
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (Files.exists(dir)) {
                    Files.delete(dir);
                    System.out.println("视频所在文件夹: " + dir.getFileName() + " 已被删除");
                }
                return FileVisitResult.CONTINUE;
            }
        });
        System.out.println("批量重命名完成......");
    }

    /**
     * 读取 av.info 文件，获取 Info 对象信息
     *
     * @param infoPath av.info
     * @return Info
     * @throws IOException e
     */
    private static Info getVideoInfo(Path infoPath) throws IOException {
        String info = new String(Files.readAllBytes(infoPath));
        return JSONObject.parseObject(info, Info.class);
    }

    /**
     * 获取视频名称: 非分p视频名
     *
     * @throws IOException e
     */
    private static void getVideoName() throws IOException {
        Path path = Paths.get(PATH.toString(), "\\desktop.ini");

        Files.readAllLines(path, Charset.forName("GBK")).forEach(s -> {
            if (s.startsWith(INFO_TIP)) {
                String[] strings = s.split("=");
                videoName = handleVideoName(strings[1]);
            }
        });
        // 新建存储视频的文件夹
        newPath = Paths.get(PATH.getParent().toString() + "\\" + videoName);
        if (Files.notExists(newPath)) {
            Files.createDirectory(newPath);
        }
    }

    /**
     * 处理视频标题中的特殊字符
     *
     * @return 视频标题
     */
    private static String handleVideoName(String videoName) {
        return PATTERN.matcher(videoName).replaceAll("");
    }
}
