package io.zsy;

import com.alibaba.fastjson.JSONObject;
import io.zsy.bilibili.model.Info;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: zsy
 * @date: 2021/1/10 13:15
 * // TODO 修改文件名后 1.4.7.13等文件消失
 */
// @SuppressWarnings("ALL")
public class Main {
    /**
     * 视频目录
     */
    final static Path PATH = Paths.get("C:\\Users\\ZSY\\Music\\4563994683");

    /**
     * 文件夹名特殊字符判断
     */
    final static Pattern PATTERN = Pattern.compile("[\\\\/:*?\"<>|]");

    /**
     * 视频名
     */
    private static String videoName;

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
                System.out.println("当前所在目录: " + file.getParent());
                String fileName = file.getFileName().toFile().getName();
                // 当前文件是分p视频基本信息文件
                if (fileName.endsWith(".info")) {
                    Info videoInfo = getVideoInfo(file);
                    partNo = videoInfo.getPartNo();
                    partName = videoInfo.getPartName();
                    System.out.println("视频编号: " + partNo + ", 视频名称: " + partName);
                    Files.delete(file);
                    System.out.println("该目录文件: " + file.getFileName() + " 已被删除");
                }
                // 当前文件是视频文件
                else if (fileName.endsWith(".mp4") || fileName.endsWith(".flv")) {
                    String extension = fileName.split("\\.")[1];
                    String newName = partNo + "_" + partName + "." + extension;
                    modifyFileName(newName, file);
                } else {
                    Files.delete(file);
                    System.out.println("该目录文件: " + file.getFileName() + " 已被删除");
                }

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (dir != PATH) {
                    Files.delete(dir);
                    System.out.println("视频所在文件夹: " + dir.getFileName() + " 已被删除");
                }
                return FileVisitResult.CONTINUE;
            }
        });
        // 给视频文件夹重命名
        Files.move(PATH, Paths.get(PATH.getParent().toString() + "\\" + videoName));
        System.out.println("批量重命名完成......");
    }

    /**
     * 重命名视频文件并移动到一级目录
     *
     * @param newName
     * @param file
     * @throws IOException
     */
    private static void modifyFileName(String newName, Path file) throws IOException {
        System.out.println("新文件名: " + newName + "");
        // TODO 此处报FileAlreadyExistsException 增加StandardCopyOption.REPLACE_EXISTING未验证
        Files.move(file, PATH.resolve(newName), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * 读取 av.info 文件，获取 Info 对象信息
     *
     * @param infoPath
     * @return
     * @throws IOException
     */
    private static Info getVideoInfo(Path infoPath) throws IOException {
        String info = new String(Files.readAllBytes(infoPath));
        return JSONObject.parseObject(info, Info.class);
    }

    /**
     * 获取视频名称: 非分p视频名
     *
     * @throws IOException
     */
    private static void getVideoName() throws IOException {
        Path path = Paths.get(PATH.toString(), "\\desktop.ini");

        Files.readAllLines(path, Charset.forName("GBK")).forEach(s -> {
            if (s.startsWith("InfoTip")) {
                String[] strings = s.split("=");
                videoName = handleVideoName(strings[1]);
            }
        });
    }

    /**
     * 处理视频标题中的特殊字符
     *
     * @return
     */
    private static String handleVideoName(String videoName) {
        return PATTERN.matcher(videoName).replaceAll("");
    }
}
