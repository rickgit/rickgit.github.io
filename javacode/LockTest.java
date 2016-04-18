package edu.ptu.test.concurence;

import java.util.Objects;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by anshu.wang on 2016/3/29.
 */
public class LockTest {
    private int a;
    private int b;
    private Object o = new Object();
    private Lock l = new ReentrantLock();

    public void testSynchronized() {
        synchronized (LockTest.this) {
            a++;
        }
    }

    public void testLock() {

        try {
            l.lock();
        } catch (Exception e) {
        } finally {
            l.unlock();
        }
        try {
            Condition condition = l.newCondition();
            a++;
            condition.await();
            b++;
            condition.signalAll();
        } catch (Exception e) {

        }
    }

    public void testObjectWait() {
        try {

            a++;
            o.wait();
            b++;
            o.notifyAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
