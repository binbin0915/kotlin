package com.itheima.mvc;

/**
 * Created by lidongzhi on 2017/6/5.
 */

public class MainActivityPresenter {

    private  MainActivity mMainActivity;

    public MainActivityPresenter(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    public void login(String username, String password){
        // 1.创建user的对象并且赋值
        final User user=new User();
        user.username=username;
        user.password=password;
        //2.开启子线程
        new Thread(){
            @Override
            public void run() {
                //3.调用了一个类，模拟登陆业务
                UserLoginNet net=new UserLoginNet();

                if(net.sendUserLoginInfo(user)){
                    // 登陆成功
                    mMainActivity.success();
                }else{
                    //登陆失败
                    mMainActivity.failed();
                }

            }
        }.start();
    }
}
