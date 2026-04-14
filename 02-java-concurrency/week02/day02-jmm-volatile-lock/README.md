# 4.7 周二
上午：JMM、volatile、happens-before
下午：Lock、ReentrantLock、读写锁
算法：层序遍历、验证BST
ReentrantLock 通过 AQS 的 state 表示锁状态，通过 exclusiveOwnerThread 记录持锁线程，竞争失败的线程进入 AQS 同步队列并通过 LockSupport.park() 挂起，释放锁时再通过 unpark() 唤醒后继线程。
线程调用 lock()
↓
先 CAS 抢 state
↓
成功 → 设置 owner → 进入临界区
↓
失败 → 不是自己重入 → 进入 AQS 队列
↓
park 挂起等待
↓
前驱线程 unlock()
↓
state 归 0，唤醒后继线程
↓
被唤醒线程再尝试 CAS 抢锁