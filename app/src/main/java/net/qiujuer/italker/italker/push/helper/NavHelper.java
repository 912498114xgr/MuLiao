package net.qiujuer.italker.italker.push.helper;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
/**
 * Created by Administrator on 2018/5/13/013.
 * 解决对fragment的调度与重用问题
 * 达到最优的切换
 */

public class NavHelper<T> {
    //所有的tab集合
    private final SparseArray<Tab<T>> tabs = new SparseArray();
    //管理器
    private final FragmentManager fragmentManager;
    //放置的位置id
    private final int containerId;
    //上下文少不了
    private final Context context;
    //回调接口
    private OnTabChangedListener<T> listener;

    //存储一个当前选中的菜单
    private Tab<T> currentTab;

    public NavHelper(Context context, int containerId, FragmentManager fragmentManager, OnTabChangedListener<T> listener) {
        this.context = context;
        this.containerId = containerId;
        this.fragmentManager = fragmentManager;
        this.listener = listener;
    }

    //TODO 第四步 添加菜单的方法

    /**
     * 添加对应的菜单id
     *
     * @param menuId 菜单的Id
     * @param tab    Tab
     */
    public NavHelper<T> add(int menuId, Tab<T> tab) {
        tabs.put(menuId, tab);
        return this;

    }
    //TODO 第五步

    /**
     * 获取当前显示的Tab
     *
     * @return
     */
    public Tab<T> getCurrentTab() {
        return currentTab;
    }


    /**
     * 执行点击菜单的操作
     *
     * @param menuId 菜单的Id
     * @return 是否能够处理这个点击
     */
    //TODO 第一步 MainActivity点击事件触发回调到工具类的这个方法
    public boolean performClickMenu(int menuId) {

        //集合中寻找点击的菜单对应的Tab 如果有则进行处理
        Tab<T> tab = tabs.get(menuId);
        if (tab != null) {
            return true;
        }

        return false;
    }
    //TODO 第五步

    /**
     * 进行真实的tab选择操作
     *
     * @param tab
     */
    private void doSelect(Tab<T> tab) {
        Tab<T> oldTab = null;
        if (currentTab != null) {
            oldTab = currentTab;
            if (oldTab == tab) {
                //如果说当前的tab就是点击的tab那么直接返回
                notifyReselect(tab);
                return;
            }
        }
        currentTab = tab;
        doTabChanged(currentTab, oldTab);
    }

    //TODO 第七步
    private void doTabChanged(Tab<T> newTab, Tab<T> oldTab) {

        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (oldTab != null) {
            if (oldTab.fragment != null) {
                //从界面中移除原来的fragment
                ft.detach(oldTab.fragment);
            }
        }


        if (newTab != null) {
            if (newTab.fragment == null) {
                Fragment fragment = Fragment.instantiate(context, newTab.clx.getName(), null);
                //缓存起来
                newTab.fragment = fragment;
                //提交到fragmentManager
                ft.add(containerId, fragment, newTab.clx.getName());

            } else {
                //从FragmentManager的缓存空间中重新加载到界面中
                ft.attach(newTab.fragment);
            }
        }

        ft.commit();
        //通知回调
        notifyTabSelect(newTab, oldTab);

    }
    //TODO 第八步
    /**
     * 回调我们的监听器
     *
     * @param newTab
     * @param oldTab
     */
    private void notifyTabSelect(Tab<T> newTab, Tab<T> oldTab) {

        if (listener != null) {

            listener.onTabChanged(newTab, oldTab);

        }
    }

    //TODO 第六步
    private void notifyReselect(Tab<T> tab) {


    }

    //TODO 第二步 集合列表里放置的菜单类

    /**
     * 我们所有的Tab菜单基础
     *
     * @param <T> 泛型的额外参数
     */
    public static class Tab<T> {

        public Tab(Class<?> clx, T extra) {
            this.clx = clx;
            this.extra = extra;
        }

        //Fragment对应的class信息
        public Class<?> clx;
        //额外的字段，用户自己设定需要使用
        public T extra;

        //内部缓存的对应的fragment,private外部无法使
        Fragment fragment;

    }
    //TODO 第三步 定义一个接口菜单切换的接口回调

    /**
     * 定义事件处理完成后的回掉接口
     *
     * @param <T>
     */
    public interface OnTabChangedListener<T> {
        void onTabChanged(Tab<T> newTab, Tab<T> oldTab);

    }


}
