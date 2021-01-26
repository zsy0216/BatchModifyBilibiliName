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