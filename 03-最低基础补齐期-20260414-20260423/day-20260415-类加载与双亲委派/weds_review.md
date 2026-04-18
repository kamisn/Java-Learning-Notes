20. 并发复习：volatile

volatile 主要保证两点：

20.1 可见性

一个线程修改共享变量后，其他线程能及时看到最新值。

20.2 一定程度的有序性

通过内存屏障，禁止特定的指令重排序。

但它 不能保证原子性，尤其不能保证复合操作的线程安全。

核心结论

volatile 能保证可见性和一定程度的有序性，但不能保证原子性。

21. 为什么 count++ 即使用 volatile 也不安全

因为 count++ 不是原子操作，它至少包含：

读取旧值
加一计算
写回新值

如果多个线程同时执行，这三步之间就可能互相覆盖结果，所以即使用 volatile 修饰，仍然不安全。

核心结论

volatile 不能把 count++ 这种复合操作变成原子操作。

22. synchronized 和 ReentrantLock 的区别
22.1 使用方式不同
synchronized：关键字
ReentrantLock：JUC 提供的锁类，通过 API 调用
22.2 释放方式不同
synchronized：自动释放
ReentrantLock：必须手动 unlock()，通常放在 finally 中
22.3 功能灵活性不同
ReentrantLock 支持可中断锁、公平锁、多个 Condition
synchronized 更简单直接
核心结论

ReentrantLock 更灵活，synchronized 更直接。
20. volatile 能保证什么？不能保证什么？为什么它不能代替 synchronized？

答：
volatile 主要能保证两点：第一是可见性，也就是一个线程修改共享变量后，其他线程能够及时看到最新值；第二是一定程度的有序性，它会通过内存屏障禁止特定的指令重排序。

但 volatile 不能保证原子性，特别是像 count++ 这种读、改、写组成的复合操作，在多线程下仍然可能出现线程安全问题。

它不能代替 synchronized，因为 synchronized 不仅能保证可见性和有序性，还能通过加锁让同一时刻只有一个线程进入临界区，从而保证复合操作的原子性。

21. 为什么 count++ 用 volatile 修饰后，仍然不是线程安全的？

答：
因为 count++ 不是一个原子操作，它底层至少包含读取旧值、进行加一计算、再把新值写回这几个步骤。volatile 只能保证线程之间看到的是最新值，但不能把这几个步骤变成一个不可分割的整体。这样一来，如果多个线程同时执行 count++，就可能在读改写之间互相覆盖结果，导致线程不安全。

22. synchronized 和 ReentrantLock 的核心区别是什么？

答：
synchronized 和 ReentrantLock 的核心区别主要有三点。

第一，使用方式不同。synchronized 是 Java 关键字，直接写在代码里；ReentrantLock 是 JUC 提供的锁类，需要通过 API 显式调用 lock 和 unlock。
第二，锁的释放方式不同。synchronized 在代码执行结束或发生异常时会自动释放锁；ReentrantLock 不会自动释放，一般要在 finally 中手动 unlock。
第三，功能灵活性不同。synchronized 更简单直接，而 ReentrantLock 更灵活，支持可中断锁、公平锁和多个 Condition 条件队列，适合更复杂的并发控制场景。