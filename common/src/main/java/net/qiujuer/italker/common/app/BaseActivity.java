package net.qiujuer.italker.common.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/5/8/008.
 */

public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //在界面未初始化之前调用的初始化窗口
        initWindows();

        if(initArgs(getIntent().getExtras())) {
            //得到界面id 并设置到界面中
            int layId = getContentLayoutId();
            setContentView(layId);
            initWidget();
            initData();
        }else {
            finish();
        }


    }

    /**
     * 初始化窗口
     */
    protected void initWindows(){

    }

    /**
     * 初始化相关参数
     * @param bundle 参数
     *
     * @return如果参数正确返回True，错误返回False
     */
    protected boolean initArgs(Bundle bundle){
        return true;
    }

    //子类必须实现 得到当前界面的资源文件ID
    protected abstract int getContentLayoutId();


    protected void initWidget(){
        ButterKnife.bind(this);
    }

    /**
     * 初始化数据
     */
    protected void initData(){

    }


    @Override
    public boolean onSupportNavigateUp() {
        //当点击界面导航返回时，finish当前界面
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        //得到当前activity下的所有fragment

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        // 判断是否为空
        if(fragments!=null&&fragments.size()>0){
            for(Fragment fragment : fragments){
                //判断是否为我们能够处理的Fragment类型
                if(fragment instanceof net.qiujuer.italker.common.app.BaseFragment){
                    //判断是否拦截了返回按钮
                    if(((BaseFragment) fragment).onBackPressed()){
                        //如果有直接return
                        return;
                    }
                }
            }
        }
        super.onBackPressed();
        finish();
    }
}
