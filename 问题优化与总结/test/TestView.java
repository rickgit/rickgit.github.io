package edu.ptu.gson;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

public class TestView {
    //使用CPU工具观察view init onMeasure onLayout耗时
    public static void testAddViewRepeat(ViewGroup vg) {
        for (int j = 0; j < 10; j++) {
            //一个层级测试 10 个左右linear最快，Relative onMeasure少于ConstrantLayout,但是onlayout耗时。基本耗时相当；
            //添加100个linear10ms, Relative 15ms, Constrant 30ms
            vg.addView(new View(vg.getContext()));
        }
    }
    public static void testAddFrameDeep(ViewGroup viewGroup) {
        //深度 init耗时 2ms一个ViewGroup； Linear 7ms；Relative 1s;Constraint 35ms
            FrameLayout cur = new FrameLayout(viewGroup.getContext());
            viewGroup.addView(cur);
            for (int j = 0; j < 20; j++) {//实际开发遇到过最深层级
                FrameLayout  frameLayout = new FrameLayout(viewGroup.getContext());
                cur.addView(frameLayout);
                cur=frameLayout;
            }
    }
    public static void testAddSelfDeep(ViewGroup viewGroup) {
        //深度 init耗时 2ms一个ViewGroup； Linear 7ms；Relative 1s;Constraint 35ms
        if (viewGroup instanceof LinearLayout) {
            LinearLayout cur = (LinearLayout) viewGroup;
            for (int j = 0; j < 20; j++) {//实际开发遇到过最深层级
                LinearLayout child = new LinearLayout(viewGroup.getContext());
                cur.addView(child);
                cur = child;
            }
        }
        if (viewGroup instanceof RelativeLayout) {
            RelativeLayout cur = (RelativeLayout) viewGroup;
            for (int j = 0; j < 20; j++) {//实际开发遇到过最深层级
                RelativeLayout child = new RelativeLayout(viewGroup.getContext());
                cur.addView(child);
                cur = child;
            }
        }
        if (viewGroup instanceof ConstraintLayout) {
            ConstraintLayout cur = (ConstraintLayout) viewGroup;
            for (int j = 0; j < 20; j++) {//实际开发遇到过最深层级
                ConstraintLayout child = new ConstraintLayout(viewGroup.getContext());
                cur.addView(child);
                cur = child;
            }
        }
    }
}
