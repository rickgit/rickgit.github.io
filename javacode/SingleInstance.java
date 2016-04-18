package edu.ptu.androidanimation;

/**
 * Created by WangAnshu on 2016/3/28.
 */
public class SingleInstance {
    private static SingleInstance sInst = null;

    public static synchronized SingleInstance getInstance() {
    	synchronized(SingleInstance.class){
        if (sInst == null) {
            sInst = new SingleInstance();
        }}
        return sInst;
    }
}
