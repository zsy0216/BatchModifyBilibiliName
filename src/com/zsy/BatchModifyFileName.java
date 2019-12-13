package com.zsy;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 批量修改B站客户端下载的视频名
 * @author Ep流苏
 * @联系我： 公众号，B站等搜索Ep流苏
 */
public class BatchModifyFileName {
    private static List<File> infoList = new ArrayList<>();
    private static List<File> flvList = new ArrayList<>();

    public static void main(String[] args) {
        // 视频的下载路径
        String downloadPath = "E:\\Videos\\Bilibili videos\\78494646";
        // 视频av号：就是路径的最后一级目录
        String avNum = null;
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(downloadPath);
        if (matcher.find()) {
            avNum = matcher.group();
        }

        List<File> infoList = getInfoList(downloadPath);
        List<File> flvList = getFlvList(downloadPath);
        List<String> partNameList = getPartNameList(infoList);

        for (int i = 0; i < flvList.size(); i++) {
            // System.out.println(flvList.get(i));
            // System.out.println(getDestFile(partNameList.get(i), flvList.get(i), avNum));
            String partName = partNameList.get(i);
            File flvFile = flvList.get(i);
            // 目标文件:E:\Videos\75233634\1_1、这阶段该如何学习.flv
            File destFile = getDestFile(partName, flvFile, avNum);
            //原始文件：E:\Videos\Captures\75233634\1\75233634_1_0.flv
            File originFile = flvList.get(i);
            if (originFile.renameTo(destFile)) {
                System.out.println(i +"- success!  " + destFile.getName());
            }
        }
    }

    /**
     * 遍历下载目录，将info文件存入list集合
     * - 目的：提供info文件给getPartNameList()方法，获得想要的视频文件名
     *
     * @param downloadPath
     * @return
     */
    public static List<File> getInfoList(String downloadPath) {
        File dir = new File(downloadPath);

        // 把下载目录下的所有文件(可能是目录也可能是文件)放到数组中
        File[] subDirOrFile = dir.listFiles();

        if (subDirOrFile != null) {
            for (int i = 0; i < subDirOrFile.length; i++) {
                String fileName = subDirOrFile[i].getName();
                // 判断是否是目录，如果是目录继续遍历
                if (subDirOrFile[i].isDirectory()) {
                    getInfoList(subDirOrFile[i].getAbsolutePath());
                    //   判断是否以info结尾
                } else if (fileName.endsWith("info")) {
                    infoList.add(subDirOrFile[i]);
                } else {
                    continue;
                }
            }
        }
        return infoList;
    }

    /**
     * 读取 info 文件，获取视频文件名
     *
     * @param infoFile
     * @return
     */
    public static String getPartName(File infoFile) {
        BufferedReader br = null;
        String partName = null;
        try {
            br = new BufferedReader(new FileReader(infoFile));
            String str;
            while (null != (str = br.readLine())) {
                // 获取partName字段对应的文件名
                partName = str.split(",")[17].split(":")[1].replace("\"", "");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return partName;
    }

    /**
     * 遍历info 文件list集合，获得视频文件名list集合
     *
     * @param infoList
     * @return
     */
    public static List<String> getPartNameList(List<File> infoList) {
        List<String> partNameList = new ArrayList<>();
        for (int i = 0; i < infoList.size(); i++) {
            // 调用获取视频名的方法
            String partName = getPartName(infoList.get(i));
            partNameList.add(partName);
        }
        return partNameList;
    }

    /**
     * 根据下载路径，遍历获取所有视频文件list集合
     * -目的：改名时需要知道原始文件对象
     *
     * @param downloadPath
     * @return
     */
    public static List<File> getFlvList(String downloadPath) {
        File dir = new File(downloadPath);

        // 把下载目录下的所有文件放到数组中
        File[] subDirOrFile = dir.listFiles();

        if (subDirOrFile != null) {
            for (int i = 0; i < subDirOrFile.length; i++) {
                String fileName = subDirOrFile[i].getName();
                if (subDirOrFile[i].isDirectory()) {
                    getFlvList(subDirOrFile[i].getAbsolutePath());
                } else if (fileName.endsWith("flv")) {
                    flvList.add(subDirOrFile[i]);
                } else {
                    continue;
                }
            }
        }
        return flvList;
    }

    /**
     * 根据视频名，flv文件对象，av号来组装我们想要的文件对象
     * -用途：重命名的目标文件对象
     *
     * @param partName
     * @param flvFile
     * @param avNum
     * @return
     */
    public static File getDestFile(String partName, File flvFile, String avNum) {
        // 根据flv文件名截取视频的序号
        String index = flvFile.getName().split("_")[1];

        // 截取flv文件路径，作为重命名文件的路径 E:\Videos\75233634\
        String newPathTemp = flvFile.getPath().split(avNum + "_")[0];
        // 判断该路径最后有没有"\" ，没有则加上 E:\Videos\75233634\
        String newPath = newPathTemp.endsWith("\\") ? newPathTemp : newPathTemp + "\\";
        // 新的文件路径：即 E:\Videos\75233634\1_1、这阶段该如何学习.flv
        String newFilePath = newPath + index + "_" + partName + ".flv";

        File destFile = new File(newFilePath);
        return destFile;
    }
}
