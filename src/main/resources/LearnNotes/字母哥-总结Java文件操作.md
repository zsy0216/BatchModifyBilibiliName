参考：https://space.bilibili.com/436492104/channel/detail?cid=147588
# 创建并写入文件的五种方法
  io.zsy.study.zimug.FileCreateTest.java

# 读取文件数据的6种方法
  io.zsy.study.zimug.ReadFile.java

- Scanner(Java1.5) String, Int 类型等按分隔符读数据;
- Files.lines, 返回 Stream(Java8) 流式数据处理, 按行读取;
- Files.readAllLines, 返回 List<String> (Java8);
- Files.readString, 读取 String(Java11), 文件最大 2G;
- Files.readAllBytes, 读取 byte[] (Java7), 文件最大 2G;
- BufferedReader, 经典方式;