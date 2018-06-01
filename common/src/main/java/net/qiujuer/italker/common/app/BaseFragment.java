package net.qiujuer.italker.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/5/8/008.
 */

public abstract class BaseFragment extends Fragment {

    protected View mRoot;
    protected Unbinder mRootBinder;

    /**
     * 在这里进行初始化界面的操作
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //初始化参数
        initArgs(getArguments());
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(mRoot == null){
            int layId  = getContentLayoutId();
            //初始化当前的根布局，但是不在创建时就添加到container里面
            View root = inflater.inflate(layId,container,false);
            initWidget(root);
            mRoot = root;
        }
        //如果root已经存在
        else {
            //并且root的父布局不为空
            if(mRoot.getParent()!=null){
                //把当前root从其父控件中移除
                ((ViewGroup)mRoot.getParent()).removeView(mRoot);
            }
        }

        return mRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //当view创建完成 初始化数据
        initData();
    }



    /**
     * 得到当前界面的资源Id
     * @return 资源文件Id
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     * @param root
     */
    protected void initWidget(View root){
        mRootBinder =  ButterKnife.bind(this,root);
    }

    /**
     * 初始化相关参数
     * @param bundle 参数
     *
     */
    protected void initArgs(Bundle bundle){

    }

    /**
     * 初始化数据
     */
    protected void initData(){

    }

    /**
     * 返回按键触发时调用
     * @return 返回true代表我已经处理返回逻辑，Activity不用自己finish
     * 返回 false 代表我没有处理返回逻辑，Activity走自己的逻辑
     */
    public boolean onBackPressed(){
        return false;
    }
}
