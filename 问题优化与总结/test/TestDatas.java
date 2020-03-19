package com.ingame.wwl.utils;

import com.ingame.service.bean.ActiveDetail;
import com.ingame.service.bean.BindGameInfoBean;
import com.ingame.service.bean.MyActivelist;
import com.ingame.service.bean.UserMainData;
import com.ingame.service.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestDatas {
    public static final UserMainData testUserMainData() {
        UserMainData userMainData = new UserMainData();
        userMainData.user_name= getNickname();
        userMainData.phone= getPhone();
        userMainData.icon= getIcon();
        userMainData.invite_code= getInviteCode();
        userMainData.money= getMoney();
        userMainData.novel_api_url= getNovelApiUrl();
        userMainData.novel_url= getNovelUrl();
        return userMainData;
    }


    /**
     * @return
     */
    public static final List<BindGameInfoBean.BindGameBean> testBindGameInfo() {
        BindGameInfoBean bindGameInfoBean = new BindGameInfoBean();
        List list = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            BindGameInfoBean.BindGameBean e = bindGameInfoBean.new BindGameBean();
            e.name = getName();
            e.create_time = getCreate_time();
            e.game_account = getAccount();
            e.game_uid = getAccount();
            e.icon = getImage();
//            e.icon = "https://img-blog.csdn.net/20180803200649800?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2JhaWR1XzMwMTY0ODY5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70";
            list.add(e);
        }
        return list;
    }


    public static final List<MyActivelist.ActiveItem> activeItemList() {
        MyActivelist bean = new MyActivelist();
        List list = new ArrayList<>();
        Random random = new Random();
        int recode = Constants.LOADNUM;
        if (random.nextInt(2) % 2 == 1)
            recode = Constants.LOADNUM / 2;//if loaded
        for (int i = 0; i < recode; i++) {
            MyActivelist.ActiveItem e = bean.new ActiveItem();
            e.name = getName();
            e.describe = getDesc();
            e.id = 1;
            e.activity_title = getTile();
            e.icon = getImage();
            list.add(e);
        }
        return list;
    }

    public static final ActiveDetail activeDetail() {
        ActiveDetail bean = new ActiveDetail();

        bean.game_info = bean.new GameInfo();
        bean.game_info.icon = getImage();
        bean.game_info.type = 1;//type 1~2
        bean.game_info.name = getName();
        bean.game_info.style = getStyle();
        bean.game_info.number = 1234;
        bean.game_info.describe = getDesc();
        List list = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            ActiveDetail.ActiveItem e = bean.new ActiveItem();
            e.id = 1;
            e.title = getTile();
            e.rebate_ratio = "333";
            e.min_money = "333.00";
            e.buttom_status = i % 2 == 0 ? 1 : 3;// 1=充值 3=已完成
            e.success_money = "1.00";
//            e.introduce = "魔君魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天<br/> <br/><br/><br/> <br/><br/> <br/><br/> <br/><br/> <br/>下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下天下";//
            e.introduce = getIntroduce();//
            list.add(e);
        }
        bean.list = list;
        return bean;
    }


    @NotNull
    private static String getPhone() {
        return "13076950595";
    }

    @NotNull
    private static String getNickname() {
        return "用户_245dddddddddddddddddddd6";
    }

    @NotNull
    private static String getNovelUrl() {
        return "http://test.book.lieying.cn/Public/view/active_all/getcash/sucesspage.html";
    }

    @NotNull
    private static String getNovelApiUrl() {
        return "http://test.book.lieying.cn/Order/orderList";
    }

    @NotNull
    private static String getMoney() {
        return "0.00";
    }

    @NotNull
    private static String getInviteCode() {
        return "lMy0zMnX45ffddddddddddddddddddddddddddd";
    }

    @NotNull
    private static String getIcon() {
        return "https://ingame.oss-cn-hangzhou.aliyuncs.com/falcon/header/default_avatar_male_180.gif";
    }

    private static String getTile() {
        return getName();
    }
    @NotNull
    private static String getAccount() {
        return "test_1";
    }

    @NotNull
    private static String getCreate_time() {
        return "0000-00-00 00:00:00";
    }

    public static String getIntroduce() {
        return "1、活动时效：领取活动时间至领取活动当日0点有效，0点以后未完成活动需重新领取。\r\n2、活动完成标准：活动时效内，活动游戏内充值金额&gt;=活动达标金额。\r\n3、返现：返现即充即返；以红包形式存入“个人中心-现金中心-返现红包”；返现红包金额超出最高返现红包金额时，只返最大返现红包金额，超出部分不返现。\r\n4、返利：奖励红包每天0点结算；以红包形式存入“个人中心-现金中心-奖励红包”；奖励红包金额超出最高奖励红包金额时，只返最大奖励红包金额，超出部分不返利。 \r\n5、每日提现需满足：\r\n1)提现金额&lt;=每日提现最大限额；\r\n2)当日提现次数&lt;=可提现次数；\r\n3)提现金额&gt;=每日单笔最小提现金额；\r\n4)所有用户提现金额&lt;=玩玩乐平台每日提现最大限额；\r\n5)每日提现时间：开始时间09：00：00至结束时间20：59：59";
    }

    public static String getDesc() {
        return "魔魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下君天下";
    }

    public static String getName() {
        return "魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下魔君天下";
    }

    public static String getStyle() {
        return "休闲|男生休闲|男生休闲|男生休闲|男生休闲|男生休闲|男生休闲|男生休闲|男生休闲|男生休闲|男生休闲|男生休闲|男生休闲|男生";
    }

    public static String getImage() {
        return "https://cn.bing.com/th?id=OIP.ksHrVRz1vXbkVSE933C2kwHaEL&pid=Api&rs=1";
    }
}
