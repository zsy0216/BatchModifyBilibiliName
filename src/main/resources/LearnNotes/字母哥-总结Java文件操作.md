参考：https://space.bilibili.com/436492104/channel/detail?cid=147588
# 创建并写入文件的五种方法
src/main/java/io/zsy/study/zimug/FileCreateTest.java

- 使用 Files.newBufferedWriter 创建文件并写入
- 使用 Files.write() 创建并写入文件
- 使用 PrintWriter 创建并写入文件
- 使用File.createNewFile() 创建文件, FileWriter写入文件
- 使用 FileOutputStream 管道流创建文件, 使用 BufferWriter 写入文件

# 读取文件数据的 6 种方法
src/main/java/io/zsy/study/zimug/ReadFileTest.java

- Scanner(Java1.5) String, Int 类型等按分隔符读数据;
- Files.lines, 返回 Stream(Java8) 流式数据处理, 按行读取;
- Files.readAllLines, 返回 List<String> (Java8);
- Files.readString, 读取 String(Java11), 文件最大 2G;
- Files.readAllBytes, 读取 byte[] (Java7), 文件最大 2G;
- BufferedReader, 经典方式;

# 创建文件夹的四种方法及优缺点
src/main/java/io/zsy/study/zimug/CreateDirTest.java

1. 传统 IO File 方式
- mkdir 创建文件夹成功返回 true， 失败返回 false；
- mkdirs 创建文件夹连同该文件夹的父文件夹；
  
2. NIO Files 方式
- createDirectory
    - 如果父文件夹不存在，抛出 NoSuchFileException.
    - 如果文件夹已经存在，抛出 FileAlreadyExistException.
    - 如果因为磁盘 IO 出现异常，抛出IOException.
- createDirectories
    - 如果父文件夹不存在，就创建他
    - 如果文件夹已经存在，不会重复创建，没有异常抛出
    - 如果因为磁盘 IO 出现异常，抛出IOException. 

# 文件拷贝及剪切的 5 种方法及优缺点
src/main/java/io/zsy/study/zimug/CopyCutFileTest.java

- 文件拷贝：将文件从一个文件夹复制到另一个文件夹;(copy)
- 文件剪切：将文件从一个文件夹移动到另一个文件夹;(move + resolve)
- 文件重命名：将文件改名，也可以理解为剪切为文件夹下另一个文件;(renameTo)(move)

# 删除文件夹或文件的 7 种方法
src/main/java/io/zsy/study/zimug/DeleteFileTest.java

文件或文件夹的代表形式：
  - NIO 使用 Path 
  - 传统 IO 使用 File

1. 删除空文件夹(不含子文件夹或子文件)

| 删除空文件夹   | 成功返回值 | 是否能判别文件夹不存在导致失败 | 是否能判别文件夹不为空导致失败 | 备注                 |
| -------------------------- | ---------- | ------------------------------ | ------------------------------ | -------------------- |
| File.delete()              | true       | 否（返回false）                | 否（返回false）                | 传统IO               |
| File.deleteOnExit()        | void       | 否，但不存在就不会执行删除     | 否（返回void）                 | 传统IO，避免使用，坑 |
| Files.delete(Path)         | void       | NoSuchFileException            | DirectoryNotEmptyException     | NIO，推荐            |
| Files.deleteIfExsits(Path) | true       | false                          | DirectoryNotEmptyException     | NIO                  |

2. 删除带子文件的目录
包含对满足条件的文件目录删除（大于多少 M，或者以 .log 结尾）
   
- Files.walkFileTree(path, SimpleFileVisitor<Path>()): 遍历文件及目录
- Files.walk
- 递归 listFiles: 传统 IO 递归删除
