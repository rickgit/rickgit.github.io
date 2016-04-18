package edu.ptu.test.bean;

/**
 * Created by anshu.wang on 2016/3/25.
 */
public class Cat {
    public static void main(String[] args) {
        try {
            Cat cat = Cat.class.newInstance();
        } catch (Exception e) {
        }
    }
}
