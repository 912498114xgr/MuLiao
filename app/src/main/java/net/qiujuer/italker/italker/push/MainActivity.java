package net.qiujuer.italker.italker.push;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import net.qiujuer.genius.ui.Ui;
import net.qiujuer.italker.common.app.BaseActivity;
import net.qiujuer.italker.common.widget.PortraitView;
import net.qiujuer.italker.italker.push.frags.main.ActiveFragment;
import net.qiujuer.italker.italker.push.frags.main.ContactFragment;
import net.qiujuer.italker.italker.push.frags.main.GroupFragment;
import net.qiujuer.italker.italker.push.helper.NavHelper;
import java.util.Objects;
import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener
        ,NavHelper.OnTabChangedListener<Integer>{
    @BindView(R.id.appbar)
    View mLayAppbar;

    @BindView(R.id.im_portrait)
    PortraitView mPortrait;

    @BindView(R.id.txt_title)
    TextView mTitle;

    @BindView(R.id.lay_container)
    FrameLayout mContainer;

    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;
    @BindView(R.id.btn_action)
    FloatingActionButton mAction;

    private NavHelper<Integer> mNavHelper;


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initWidget() {
        super.initWidget();
        mNavHelper = new NavHelper(this,R.id.lay_container,getSupportFragmentManager(),this);
        mNavHelper.add(R.id.action_home,new NavHelper.Tab<Integer>(ActiveFragment.class,R.string.title_home))
                .add(R.id.action_group,new NavHelper.Tab<Integer>(GroupFragment.class,R.string.title_group))
                .add(R.id.action_contact,new NavHelper.Tab<Integer>(ContactFragment.class,R.string.title_contact));

        mNavigation.setOnNavigationItemSelectedListener(this);

        Glide.with(this)
                .load(R.drawable.bg_src_morning)
                .centerCrop()
                .into(new ViewTarget<View, GlideDrawable>(mLayAppbar) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setBackground(resource.getCurrent());
                    }
                });

    }

    @Override
    protected void initData() {
        super.initData();
        //从底部导航中接管我们的menu。然后进行手动的触发第一次点击
        Menu menu = mNavigation.getMenu();
        //触发首次选中Home
        menu.performIdentifierAction(R.id.action_home,0);



    }

    @OnClick(R.id.im_search)
    void onSearchMenuClick() {

    }

    @OnClick(R.id.btn_action)
    void onActionClick() {
    }

    /**
     * 当我们的底部导航被点击的时候触发
     * @param item MenuItem
     * @return 代表我们可以处理这个点击
     */

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

       //转接触发事件到工具类中

        return mNavHelper.performClickMenu(item.getItemId());

    }

    /**
     * NavHelper处理后回调的方法
     * @param newTab
     * @param oldTab
     */
    @Override
    public void onTabChanged(NavHelper.Tab newTab, NavHelper.Tab oldTab) {
        mTitle.setText((Integer) newTab.extra);

        //对浮动按钮进行隐藏与显示的动画
        float transY = 0;
        float rotation = 0;
        if(Objects.equals(newTab.extra,R.string.title_home)){
            transY = Ui.dipToPx(getResources(),76);
        }else {
            if (Objects.equals(newTab.extra,R.string.title_group))
            {

                mAction.setImageResource(R.drawable.ic_group_add);
                rotation = -360;
            }else{
                mAction.setImageResource(R.drawable.ic_contact_add);
                rotation = 360;
            }
        }
        //开始动画
        //旋转，Y轴位移，弹性差值器
        mAction.animate()
                .rotation(rotation)
                .translationY(transY)
                .setInterpolator(new AnticipateOvershootInterpolator(1))
                .setDuration(400)
                .start();
    }
}
