1、建立工程： 使用meaven 搭配 eplise 建立工程
安装java
安装eplise
安装maven 查看mvn --version
cd 到含有pom.xml的文件夹下面 Texas_ai/ai/目录下
执行 mvn install(为了安装依赖包)
利用eplise打开工程目录 
会自动生成工程 和一个data文件加里面存储，数据库文件包括对手模型和概率计算
data 数据库文件需要单独生成（5到8个小时）复制过去就可以了

可执行文件的ai.jar必须与data同级放置不然找不到数据库




