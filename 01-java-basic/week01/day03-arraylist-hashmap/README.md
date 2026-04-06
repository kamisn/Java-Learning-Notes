# 4.1 周三
上午：ArrayList/LinkedList 源码、扩容
下午：HashMap 结构、put流程
算法：最大子数组和、爬楼梯

List接口深度对比学习笔记
List接口深度对比学习笔记
ArrayList
ArrayList 的底层是动态数组，默认无参构建的实例，第一次执行 add 之后，数组大小会被初始化为 10。
其中比较关键的是 EMPTY_ELEMENTDATA 变量：它本质是一个共享的空数组，创建它的目的是，让所有需要空数组的 ArrayList 实例都指向它，而不是各自 new 一个新的空数组，这样就避免了重复创建相同的空数组对象，节省了内存。
1. 构造方法

// 无参构造：初始容量为 0（延迟初始化，第一次 add 时扩容到 10）
public ArrayList() {
this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
}

// 指定初始容量
public ArrayList(int initialCapacity) {
if (initialCapacity > 0) {
this.elementData = new Object[initialCapacity];
} else if (initialCapacity == 0) {
this.elementData = EMPTY_ELEMENTDATA;
} else {
throw new IllegalArgumentException("Illegal Capacity: "+ initialCapacity);
}
}
注意：ArrayList 没有像 LinkedList 那样，在集合参数的构造方法里先调 this() 再调用 addAll，这是因为数组需要一次性分配合适的大小，避免反复扩容。
如果 ArrayList 也先调用无参构造（此时 elementData 指向默认空数组），再逐个添加元素，就会触发多次扩容：先扩容到默认容量 10，后面可能还要继续扩容，效率很低。
所以它的集合参数构造方法，会直接根据传入集合的大小，一次性创建足够大的数组，然后通过 Arrays.copyOf 把元素拷贝进去，一步到位，性能更好。
2. 添加元素方法
   末尾添加
   E 是参数类型，e 是泛型参数，也就是要添加的元素：

public boolean add(E e) {
// 确保容量足够，如果不够则扩容
ensureCapacityInternal(size + 1);
// 将元素放到数组末尾
elementData[size++] = e;
return true;
}
指定位置添加

// 公共方法，参数 index 是要插入的位置（从 0 开始），element 是要插入的元素，返回值为 void
public void add(int index, E element) {
rangeCheckForAdd(index); // 检查索引是否合法（0 <= index <= size）
ensureCapacityInternal(size + 1); // 确保数组容量足够存放 size + 1 个元素，不够则自动扩容
// 将 index 及其后的元素向后移动一位
System.arraycopy(elementData, index, elementData, index + 1,
size - index);
elementData[index] = element;
size++;
}
3. 扩容机制
   minCapacity 是传入的参数，表示当前所需的最小容量：

private void grow(int minCapacity) {
int oldCapacity = elementData.length;
// 新容量 = 旧容量 + 旧容量 >> 1 => 扩容为原来的 1.5 倍
// 旧容量 >> 1 是位运算，效果相当于除以二
int newCapacity = oldCapacity + (oldCapacity >> 1);
if (newCapacity - minCapacity < 0)
newCapacity = minCapacity;
if (newCapacity - MAX_ARRAY_SIZE > 0)
newCapacity = hugeCapacity(minCapacity);
// MAX_ARRAY_SIZE 通常是 Integer.MAX_VALUE - 8（预留一点空间）。如果新容量超过这个值，就调用 hugeCapacity 来返回一个接近 Integer.MAX_VALUE 的容量（或抛出 OOM）

    // 拷贝原数组到新数组
    elementData = Arrays.copyOf(elementData, newCapacity);
}
---
LinkedList
LinkedList 的底层数据结构是双向链表，其中的每一个元素都封装为一个 Node 内部类。
1. 节点类

private static class Node<E> {
E item;  // 当前结点的元素
Node<E> next;// 指向下一个节点
Node<E> prev;// 指向上一个节点

    // 构造方法：接收前驱节点、当前元素和后继节点，并保存起来
    Node(Node<E> prev, E element, Node<E> next) {
        this.item = element;
        this.next = next;
        this.prev = prev;
    }
}
2. 构造方法

// 无参构造：直接创建空链表
public LinkedList() {
}

// 通过已有集合构造，传入的 collection 是所有集合的根接口，只要实现了该接口的集合，都能存进这个新建立的 LinkedList 当中
public LinkedList(Collection<? extends E> c) {
this(); // 1. 调用无参构造方法，初始化一个空的 LinkedList（把 size 设为 0，first 和 last 设为 null）
addAll(c); // 2. 把传入的集合 c 里的元素，按迭代器顺序一个一个追加到链表的尾部
}
3. 二分优化的查找方法
   LinkedList 虽然是链表，但内部做了优化，根据索引查找节点时，会用类似二分的逻辑，选择从头部还是尾部开始遍历：

Node<E> node(int index) {
if (index < (size >> 1)) {          // 索引小于链表长度的一半，从头开始找
Node<E> x = first;// 定义变量 x，指向链表的第一个节点
for (int i = 0; i < index; i++)
x = x.next;// x 节点向后移动
return x;
} else {                            // 索引大于等于一半，从尾开始找
Node<E> x = last;
for (int i = size - 1; i > index; i--)
x = x.prev;
return x;
}
}


## 一、普通 HashMap 的存储过程

### 底层结构

底层是一个 `Node` 数组，数组的每个位置我们称为 “桶”。

### 无冲突场景

桶里直接存放一个 `Node` 对象，包含 `key`、`value`、`hash`，且 `next = null`。

### 哈希冲突场景

当多个 key 计算后落在同一个桶时，会通过 `next` 指针把多个 Node 串成链表：

- 新节点会追加到链表尾部，尾节点的 `next = null`

- 匹配逻辑：

    1. 先比较 hash 值，若 hash 不同则直接判定 key 不同

    2. 若 hash 相同，再通过 `equals` 比较 key

        - key 相同 → 覆盖旧的 value

        - key 不同 → 在链表末尾添加新节点

### 链表转红黑树的触发条件

当链表长度 **> 8** 且 **数组长度 ≥ 64** 时，链表会转为红黑树，以此提高长链表的查询效率。

如果链表长度 > 8 但数组长度 < 64，**不会转树，而是优先扩容数组**：数组会翻倍，然后重新散列所有节点，以此拆散过长的链表。

> ✅ 注意：“否则优先扩容” 是指 “长度 > 8 且数组 < 64” 的情况，而不是所有不满足转树的情况都触发扩容。
>
>

---


