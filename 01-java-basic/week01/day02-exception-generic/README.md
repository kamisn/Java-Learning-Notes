# 3.31 周二
上午：异常体系、自定义异常
下午：泛型、类型擦除、通配符
算法：合并有序链表、反转链表

一、泛型的核心概念
如果要用一种数据结构存储特定类型的数据，常常会出现数据类型与容器默认类型不匹配的情况。
比如我们可以为 String 类型单独编写一个专属的容器：

public class StringArrayList {
private String[] array;
private int size;
public void add(String e) {...}
public void remove(int index) {...}
public String get(int index) {...}
}
但如果每种类型都要写一个专属容器，会非常冗余。于是我们把它改成通用的模板：

// 把具体的类型换成占位符 T，就形成了通用的泛型模板
public class ArrayList<T> {
// 甚至可以更通用：A<T>，A 是任意的数据结构
}
这个通用的类型模板，就是泛型。
关于泛型的类型参数 T
T 是泛型的参数类型，也就是类型形参。我们在定义泛型的时候，可以给这个参数设定边界：
比如 <T extends Number>，表示该参数类型必须是 Number 的子类，或者就是 Number 本身。
---
1. 泛型类
   当你创建泛型类的实例时，类型参数 T 就会被具体的类型取代，并且这个实例的 T 就固定下来了。
---
2. 泛型方法
   在普通类或者普通方法中，也可以单独定义泛型方法。
   泛型方法的特点是，不固定方法签名中的传入参数类型，在调用的时候，可以实时判断传入的参数类型，然后返回对应类型的结果。

public class GenericMethodDemo {
// 泛型方法：<T> 是泛型方法的语法声明，T 是类型形参
public static <T> T identity(T t) {
// 这里的 t 是方法的参数变量，和上面的类型形参 T 对应，变量名可以自定义
return t;
}
}
---
3. 通配符
   ? 是使用泛型时的通配符，表示 “未知的具体类型”，也可以加边界，比如 ? extends T、? super T。
   它是类型实参的占位符，意思是 “我不知道这里具体是什么类型，但我可以接受它”。
   当一个泛型已经创建好，在使用的时候，如果我们不确定要使用泛型形参 T 下的哪一种具体类型，就可以用通配符 ? 做占位符。这个占位符也可以设置边界，用来限制它是 T 的父类或者子类。
---
4. 泛型接口
   接口的特点是只有方法签名，没有具体实现，不能被实例化，泛型接口就是把泛型的概念套在接口上，和泛型类、泛型方法的逻辑类似。
   最典型的例子就是 Comparable<T>：

// 泛型接口的定义
public interface Comparable<T> {
int compareTo(T o);
}

// 实现泛型接口，指定具体的类型为 Person
public class Person implements Comparable<Person> {
private int age;
@Override
public int compareTo(Person o) {
// compareTo 是三态判断接口，和 boolean 不同，它有大于、小于、等于三种状态
// 分别用正数、负数、0 来表示
// this.age 是调用该方法的实例的 age，o.age 是被比较的实例的 age
return Integer.compare(this.age, o.age);
}
}
---
二、为什么不建议创建泛型数组
1. 数组具有协变性
   数组是协变的，比如我们可以这么写：

Object[] arr = new String[10];
这会把 String[] 赋值给 Object[] 类型的变量，但如果后续我们尝试往这个数组里放其他类型的元素，就会在运行时抛出 ArrayStoreException 异常，无法在编译期提前发现问题。
2. 泛型是类型擦除的
   泛型的类型擦除机制，会把泛型的类型参数 T 在运行时擦除为 Object。
   也就是说，编译期的 T 类型信息，在运行时会被重置，我们无从得知这个数组之前的类型信息，也就无法保证数据类型的安全，会允许任意数据类型的传入。
3. 我们通常用 ArrayList 来代替
   真正的类型安全，来自泛型方法 push(T item) 的编译时类型检查，而 ArrayList 在这里只是一个灵活的容器。
   泛型擦除后，运行时确实没有类型参数 T 的信息，ArrayList 内部可以存储任意对象，但编译期的类型检查已经帮我们拦住了错误的类型。

public class MyStack<T> {
private List<T> list = new ArrayList<>();
public void push(T item) { list.add(item); }
public T pop() { return list.remove(list.size() - 1); }
}
---
三、补充知识点
1. 类型擦除后的桥接方法（Bridge Method）当泛型类 / 接口在子类中具体化类型且发生方法重写时，编译器可能生成桥接方法保证多态。例如 class Child extends Parent<String>，重写 T get() 时会生成一个 Object get() 的桥接方法。这是问得比较深的点，可以了解一下。
2. 泛型的上下界在读写时的限制口诀
   • ? extends T：只能 “读”，不能 “写”（除了 null），因为具体类型可能是 T 的任意子类。
   • ? super T：可以 “写” 入 T 及其子类，但读出来只能当成 Object。
   • 记忆：PECS（Producer Extends, Consumer Super）。
3. 泛型与 instanceof /new 的限制
   • 由于类型擦除，运行时无法直接 if (obj instanceof List<String>)，只能判断 List<?>。
   • 不能 new T() 或 new T[]，需要通过反射或传入 Class<T>/Supplier<T>。
4. 静态上下文不能使用类型参数泛型类中的静态字段或静态方法不能直接使用类的类型参数，因为类型参数属于实例级别。
5. 泛型方法与泛型类的区别你提到了泛型方法，但可以强调：泛型方法的 <T> 定义在方法上，和类的类型参数无关；泛型类内部的普通方法如果想使用独立的类型参数，需要单独声明。
6. 通配符与泛型方法的取舍当方法只需要 “只读” 集合时，可用 List<? extends Number>；若既读又写，通常使用泛型方法 <T> 搭配 List<T>，更灵活。
7. Optional：泛型在标准库中的应用Optional 的 API 很好地体现泛型的链式使用，顺便练习下。
