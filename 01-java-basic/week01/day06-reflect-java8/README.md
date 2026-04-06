# 4.4 周六
上午：反射、动态代理、注解
下午：Lambda、Stream、Optional
算法：无重复子串、三数之和

Java 反射机制笔记
一、核心定义与原理
Java 反射机制是 Java 语言的一项强大的运行时特性。它允许程序在运行期间，无需依赖源代码，即可动态地获取类的元信息，并操作这些信息。其核心载体是 JVM 为每个加载的类创建的唯一的 java.lang.Class 对象，可以将其理解为这个类在内存中的“设计蓝图”或“图纸”。
核心能力：
• 动态获取类的完整结构：字段（Field）、构造方法（Constructor）、成员方法（Method）。
• 动态操作类的成员：创建对象、调用方法、读取/修改字段值。

二、核心应用场景——Spring框架底层
反射在现代企业级 Java 开发中最重要的应用是作为 Spring 框架的底层基石，支撑其核心的 IoC（控制反转）和依赖注入功能。
2.1 核心流程概览
1. 项目启动：JVM 仅加载 Spring 框架自身的核心类，用户自定义的业务类（如 Controller/Service）在此时未被加载。
2. 所有后续操作依赖反射：Spring 的 Bean 扫描、创建和依赖注入流程，完全通过 Java 反射机制驱动。
   2.2 Spring Bean 创建与依赖注入流程详解
   流程详解：
1. 启动与扫描：项目启动后，Spring 扫描指定包（如 com.example），寻找带有 @RestController、@Service、@Component 等注解的类，作为候选 Bean。
2. 类的加载：Spring 触发 JVM 加载这些候选类，为每个类在内存中生成唯一的 Class 对象。
3. 反射创建“半成品”Bean：Spring 通过 Class 对象的反射能力（Class.newInstance()或Constructor.newInstance()），在堆内存中创建该类的实例。此时对象已完成基本构造，但其内部的依赖属性（如 private YunShanFu yunShanFu;）只是默认值（如 null），因此称之为“半成品” Bean。
4. 识别依赖：Spring 扫描对象中的 @Autowired 注解，确定当前 Bean 需要哪些其他对象作为依赖。
5. 优先创建依赖对象：若依赖的对象尚未被创建，Spring 会暂停当前 Bean 的创建过程，转为先去创建其依赖对象。无依赖的对象会被直接创建完成并放入 IoC 容器。
6. 完成依赖注入：Spring 从 IoC 容器中找到已创建好的依赖对象，通过反射 (Field.set()) 或 Setter 方法，将其赋值给“半成品” Bean 的对应引用属性。
7. 生成完整 Bean：依赖注入全部完成后，“半成品” Bean 就变成了一个内部状态完整、随时可用的完整 Bean，并放入容器供业务使用。
   2.3 关键概念澄清
   • @Autowired 的核心作用：它并不是用于 new 一个对象。其本质作用，是将 IoC 容器中已经存在的、已创建好的某个 Bean 实例，赋值给当前对象的成员变量。
   • 两种“依赖”的区分：@Autowired 注解标识的是运行时对象之间的引用依赖，它与项目构建配置文件（如 Maven 的 pom.xml）中声明的代码包依赖（dependency）是完全不同的概念。
   • 反射的任务边界：在 Spring Bean 的生命周期中，反射主要负责 Class 对象获取、对象创建以及属性注入 这三个核心动作。当依赖注入完成，一个完整的 Bean 进入容器后，反射在此 Bean 创建阶段的核心工作就结束了。
   三、核心总结
1. 反射是 Spring IoC 容器的基石：Spring 框架正是通过反射机制，才得以在不入侵用户代码（无需手动 new 对象）的情况下，动态地管理所有 Bean 对象的生命周期。
2. 核心价值在于“动态”和“无侵入”：这种机制赋予了系统极大的灵活性，实现了解耦。
3. 理解流程链条：“候选Bean筛选 → JVM加载类 → 反射创建半成品对象 → 识别依赖 → 优先创建依赖 → 完成依赖注入 → 生成完整Bean”，这一流程是理解 Spring 框架如何工作的关键。
4. 技术本质是元编程：反射通过操作“描述数据的数据（元数据）”，实现了在运行时对程序自身结构进行检视和修改，是一种强大的元编程能力。

AOP
只保留代码中核心业务的功能，重复的，类似的，不影响主要功能的代码统一摘出出去。提升代码的可维护和拓展性

如何实现AOP
和ioc基本相同
都是先要进行扫包，扫出要构建成bean的类，打上标记
存入存入 BeanDefinitionMap
但这里不一样的地方是，spring这次多扫了一个东西（并不是以前没扫，只是之前的例子里没有）
@Aspect 和@component ，同样也给这个切面类打上标记
但是和前面的被打上标记的普通类不一样的地方在于
这些切面类还要进行一个步骤：
提前解析切面里的@Pointcut切点表达式、@Before等通知注解，把「要对哪些类增强、什么时候增强、怎么增强」的规则，缓存到 Spring 的 AspectJAdvisorFactory 中。
接下来和之前的一样都是读取标记点通过jvm加载并横城唯一.class类，然后通过反射创建bean类。（注意，此时创建的只是个半成品 Bean 实例：虽然物理上存放在 JVM 堆内存中，且全程在 Spring ApplicationContext 的生命周期管理范围内，但尚未被放入容器最终对外暴露的单例池，仅由 Spring 内部的 Bean 创建线程持有局部引用，完全对外隔离。）
然后进行依赖注入，有就注入，没有就算了，
最后进行初始化，这么一个bean类就创建好了，（此是bean所在位置和上述相同）
当这个bean类被创建好了，aop就开始匹配所有的bean类和之前被标记的切面类中的@pointcut能不能对得上。对不上直接返回原始目标对象，匹配上了就要进行下一步生成代理对象。
代理对象是什么
Spring 使用 JDK 动态代理（重点，记得复习） 或 CGLIB 生成一个全新的类：也就是代理类
生成的这个过程太长了
AddService$$Proxy（代理类，这里是个例子，并不是都叫这个名字）
代理对象内部结构：
持有 目标对象 target（原始 AddService）
持有 切面通知逻辑（@Before beforeLog ()）
重写了 add() 方法
接下来
容器扔掉原始的 AddService 对象。
将其强引用于新的代理对象
把代理对象放入 Spring 容器。单链池向外暴露
此后任何地方 @Autowired 注入的，都是代理对象，不是原始对象
（关于这注入依赖这一点：
优先创建依赖对象：若依赖的对象尚未被创建，Spring 会暂停当前 Bean 的创建过程，转为先去创建其依赖对象。无依赖的对象会被直接创建完成并放入 IoC 容器。）
以下是代码示例
// 1. 定义业务接口（JDK 动态代理必须基于接口）
public interface AddServiceInterface {
void add(); // 核心业务方法
}


// 2. 目标类（原生业务代码，完全不改动）
public class AddService implements AddServiceInterface {
@Override
public void add() {
System.out.println("执行核心业务：添加数据");
}
}


// 3. 切面类（存放增强逻辑：前置日志）
public class LogAspect {
// 前置通知：对应 Spring 的 @Before
public void beforeLog() {
System.out.println("【前置日志】方法即将执行");
}
}

// 4. JDK 动态代理核心处理器（对应 Spring 代理对象的内部执行逻辑）
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
public class AddServiceProxyHandler implements InvocationHandler {
// 核心1：持有目标对象（原生 AddService 实例）
private final Object target;
// 核心2：持有切面对象（LogAspect 实例）
private final LogAspect logAspect;
// 构造方法：注入目标对象和切面对象
public AddServiceProxyHandler(Object target, LogAspect logAspect) {
this.target = target;
this.logAspect = logAspect;
}
/**
* 核心方法：代理对象的所有方法调用，都会进入这个 invoke 方法
* @param proxy  生成的代理对象本身（一般不用）
* @param method 当前被调用的目标方法（对应 Spring 的 JoinPoint）
* @param args   方法参数
* @return 方法返回值
*/
@Override
public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
// 第一步：执行切面增强逻辑（前置通知 @Before）
logAspect.beforeLog();
// 第二步：通过反射调用目标对象的原始业务方法
// 对应 Spring 代理内部的 method.invoke(target, args)
Object result = method.invoke(target, args);
// 第三步：返回方法执行结果（如果有返回值）
return result;
}
}


// 5. 测试类：生成代理对象并调用
import java.lang.reflect.Proxy;
public class ProxyTest {
public static void main(String[] args) {
// 1. 创建原生目标对象（对应 Spring 反射实例化 AddService）
AddServiceInterface target = new AddService();
// 2. 创建切面对象（对应 Spring 扫描到 @Aspect 并创建切面 Bean）
LogAspect logAspect = new LogAspect();
// 3. 创建 InvocationHandler 处理器（注入目标对象和切面对象）
AddServiceProxyHandler handler = new AddServiceProxyHandler(target, logAspect);
// 4. 【核心】生成代理对象（对应 Spring AOP 生成 AddService$$Proxy）
// 参数说明：
// - 类加载器：和目标对象用同一个类加载器
// - 接口数组：目标对象实现的所有接口（JDK 动态代理必须基于接口）
// - InvocationHandler：核心处理器，所有方法调用走这里
AddServiceInterface proxy = (AddServiceInterface) Proxy.newProxyInstance(
target.getClass().getClassLoader(),
target.getClass().getInterfaces(),
handler
);
// 5. 调用代理对象的方法（对应业务代码中 @Autowired 注入后调用）
proxy.add();
}
}