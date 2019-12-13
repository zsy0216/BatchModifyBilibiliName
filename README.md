# 起因

昨天晚上从B站电脑客户端下了一个分集视频

但是下载后的视频是这样的：

![](https://zsy0216.github.io/image/notes/20191212182132.png)

视频名是这样的：

![](https://zsy0216.github.io/image/notes/20191212182147.png)

这样既不直观又不美观，就算把视频文件放到一个文件夹内，连续看视频时也不容易记住看到哪个。所以就有了今天的事情。

# 经过

起初，我的想法是复制出来一个一个该文件名，但是当时我想到，作为一个优秀的程序员，怎么能干这种无脑的活呢？:laughing:(其实是太懒了。。。)

于是，，，就去百度了，百度确实找到一个，但是上面的方法根本没有写全，虽然我已经尽力补全，但是他还是漏了一个非常关键的一步，所以我就决定自己来写。

## 分析

根据下载的文件来看，在info文件中，可以找到原视频的所有介绍信息，其中当然就有原视频的文件名(PartName)，所以我们只需要把info文件中的文件名提取出来，然后再对flv视频文件重命名不就完事了吗。

具体可以分为以下步骤：

1. 遍历我们下载好的视频目录，得到info文件集合和flv视频文件集合；
2. 遍历读取info文件，从中提取我们需要的视频文件名；
3. 循环遍历flv文件，提取视频序号，用来组装我们想要的视频名；
4. 遍历flv，给每一个flv视频文件换上新名字；

## 代码实现

1. 遍历视频目录，将info文件存入list集合；

   ```java
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
   ```

2. 遍历视频目录，把flv视频文件放入list集合；

   ```java
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
   ```

3. 遍历读取info文件，提取需要的视频名集合；

   ```java
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
   ```

4. 遍历flv文件集合，组装文件名；

   ```java
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
   ```

5. main方法，完成批量重命名；

   ```java
   // 这两个属于类的私有属性，定义在方法外边，分别代表info文件集合和flv视频文件集合
   private static List<File> infoList = new ArrayList<>();
   private static List<File> flvList = new ArrayList<>();
   
   public static void main(String[] args) {
       // 视频的下载路径
       String downloadPath = "E:\\Videos\\Bilibili videos\\75233634";
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
               System.out.println("success!" + destFile.getName());
           }
       }
   }
   ```

# 结果

结果当然成功了，这肯定毋庸置疑了。

![](https://zsy0216.github.io//image/notes/20191212190101.png)

终于可以愉快的看视频了...:laughing:

坚定而缓慢地做自己力所能及的事。

## 时间

2019-12-12

## 地点

zut.north#5 2f

## 人物

me

> 竟然顺带复习了一下记叙文的六要素。
