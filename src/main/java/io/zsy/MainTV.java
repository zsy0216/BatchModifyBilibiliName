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
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

/**
 * 1. 影视剧
 * 2. 纪录片
 * <p>
 * 多余文件未删除
 *
 * @author: zsy
 * @date: 2021/1/10 13:15
 */
public class MainTV implements MagicValue {
    /**
     * 视频目录
     */
    final static Path PATH = Paths.get("E:\\Videos\\Bilibli Videos\\8867566");

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

        // 最外层
        Files.walkFileTree(PATH, new SimpleFileVisitor<Path>() {
            String episodeName = "";

            // 遍历剧集
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println("当前文件名：" + file.getFileName().toFile().getName());
                if (!file.getParent().equals(PATH)) {
                    String fileName = file.getFileName().toFile().getName();
                    if (fileName.endsWith(SUFFIX_INFO)) {
                        Info videoInfo = getVideoInfo(file);
                        episodeName = videoInfo.getTitle();
                        System.out.println("剧集名：" + episodeName);
                    }
                    // 当前文件是视频文件，改名，移动到一级目录
                    else if (fileName.endsWith(SUFFIX_MP4) || fileName.endsWith(SUFFIX_FLV)) {
                        String extension = fileName.split("\\.")[1];
                        String newName = episodeName + "." + extension;
                        System.out.println("新文件名：" + newName);
                        Files.move(file, newPath.resolve(newName));
                    }
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                System.out.println("当前文件夹：" + dir.getFileName().toFile().getName());
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
     * 获取剧集名
     *
     * @param episode 剧集文件夹
     * @return 剧集名
     * @throws IOException e
     */
    private static String getEpisodeName(Path episode) throws IOException {
        // Path path = Paths.get(episode.toString(), "\\desktop.ini");

        AtomicReference<String> episodeName = new AtomicReference<>();

        Files.readAllLines(episode, Charset.forName("GBK")).forEach(s -> {
            if (s.startsWith(INFO_TIP)) {
                String[] strings = s.split("=");
                episodeName.set(handleVideoName(strings[1]));
            }
        });
        return episodeName.get();
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
