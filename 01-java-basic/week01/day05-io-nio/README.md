# 4.3 周五
上午：IO字节/字符/缓冲/序列化
下午：NIO Buffer/Channel/Selector
算法：买卖股票、二分查找


Java IO流学习笔记
Java IO流学习笔记
IO 流的核心分类
IO 流分为两部分，一是输入（Input），二是输出（Output）。
操作输入输出的时候，需要考虑操作文件的数据类型，这就会涉及到两类核心流：
• 字节流：InputStream、OutputStream
• 字符流：Reader、Writer
字节流可以处理所有类型的文件，可以称得上是万能流；字符流仅能处理纯文本文件。
---
流的包装：装饰器模式
这两类流体系中，都存在基础的节点流，以及用于增强功能的装饰器流。
最基础的文件操作流，就是直接对接文件数据源的节点流：FileInputStream/FileOutputStream，以及FileReader/FileWriter。
装饰器模式 vs 继承的区别
装饰器的逻辑和继承重写有相似之处，但核心差异很大：
• 装饰器是直接在已有的节点流（比如FileInputStream）上套一层增强功能的包装类，例如：

BufferedInputStream bis = new BufferedInputStream(
new FileInputStream("a.txt")
);
• 如果用继承来实现同样的缓冲功能，就需要这么写：

class BufferedFileInputStream extends FileInputStream {
@Override
int read() {
// 加缓冲逻辑
}
}
你的疑问解答：如果用继承来实现装饰器的效果，确实需要给每一个 InputStream 的子类都单独写一个对应的缓冲子类。
因为InputStream的子类不止FileInputStream，还有ByteArrayInputStream、PipedInputStream等等，如果每个子类都要加缓冲功能，就要写BufferedFileInputStream、BufferedByteArrayInputStream、BufferedPipedInputStream... 这会导致类数量爆炸，这也是 Java IO 选择装饰器模式的核心原因。
---
缓冲流（Buffered）的工作原理
缓冲流（BufferedInputStream/BufferedOutputStream）的核心，是在程序和文件之间加了一个缓冲区：
CPU 寄存器的运算速度很快，但是读写 ** 硬盘（外存）** 的速度非常慢。
这里有个概念修正：你之前混淆了 “堆” 和 “硬盘”，堆是 JVM 运行时的内存区域（属于内存的一部分），而硬盘是外存，二者完全不同。我们需要缓冲区，就是为了匹配 CPU 和硬盘的速度差，用内存里的缓冲区做中转，就像内存条的作用一样，暂时缓存需要读写的数据，减少和硬盘的交互次数，从而节约时间。
这个缓冲区，在代码中就是一个字节数组，默认大小是 8KB：

public class BufferedInputStream extends FilterInputStream {
// 这就是缓冲区！一个字节数组！
protected volatile byte buf[];

    // 默认大小 8192 字节（8KB）
    public BufferedInputStream(InputStream in) {
        this(in, 8192);
    }
}
你的疑问解答：缓冲区的读取流程是这样的：
1. 当你第一次调用缓冲流的read()方法时，会发现内部的buf数组是空的；
2. 此时缓冲流会调用底层被包装的流（比如FileInputStream）的read方法，一次性把最多buf.length（也就是 8KB）的数据，从磁盘读到这个buf数组里；
3. 读完之后，缓冲流会把buf[0]这个字节返回给你，同时内部维护一个指针，标记当前读到了缓冲区的哪个位置；
4. 之后你再调用read()，就不需要再碰磁盘了，直接从buf数组里取下一个位置的字节返回给你，直到缓冲区的数据被读完，才会再次触发磁盘读取。
   （注：文档部分内容可能由 AI 生成）
