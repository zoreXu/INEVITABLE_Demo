package edu.nuc.sporthealth;

import android.app.Fragment;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;
import java.util.PropertyResourceBundle;

import edu.nuc.sporthealth.fragment.MainFragment;
import edu.nuc.sporthealth.menu.DrawerAdapter;
import edu.nuc.sporthealth.menu.DrawerItem;
import edu.nuc.sporthealth.menu.SimpleItem;
import edu.nuc.sporthealth.menu.SpaceItem;

/**
 *
 */

public class HomeActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    private static final int POS_HOME = 0;
    private static final int POS_RUN = 1;
    private static final int POS_DISCOVERY = 2;
    private static final int POS_PLAN = 3;
    private static final int POS_DATA = 4;
    private static final int POS_SHARE = 5;
    private static final int P0S_SETTING = 6;
    private static final int P0S_PROBLEM = 7;
    private static final int POS_LOGOUT = 9; //item空一行,所有没8

    private String[] screenTitles;
    private Drawable[] screenIcons;

    private Toolbar toolbar;
    private SlidingRootNav slidingRootNav;
    private DrawerAdapter adapter;
    private RecyclerView list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //设置toolbar

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)  //初始菜单打开/关闭状态。默认== false
                .withContentClickableWhenMenuOpened(false)  //默认true
                .withSavedState(savedInstanceState) //如果调用方法，布局将恢复其打开/关闭状态
                .withMenuLayout(R.layout.menu_left_drawer_layout)
                .inject();

        screenIcons = loadScreenIcons();    //item图片
        screenTitles = loadScreenTitles();  //item标题

        adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_HOME).setChecked(true),
                createItemFor(POS_RUN),
                createItemFor(POS_DISCOVERY),
                createItemFor(POS_PLAN),
                createItemFor(POS_DATA),
                createItemFor(POS_SHARE),
                createItemFor(P0S_SETTING),
                createItemFor(P0S_PROBLEM),
                //new SpaceItem(48),  //间隔
                createItemFor(POS_LOGOUT)));
        adapter.setListener(this);  //将监听对象传入适配器中

        list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);  //阻止recyclerview滚动
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_HOME); //默认选择home
    }

    /**
     * @function 监听item
     * @param position
     */
    @Override
    public void onItemSelected(int position) {
        if (position == POS_LOGOUT) {   //如果是exit item就退出
            finish();   //调用finish关闭应用
        }
        //不是LOGOUT
        slidingRootNav.closeMenu(); //先关闭menu
        //获得一个fragment
        Fragment selectedScreen = MainFragment.createFor(screenTitles[position]);
        //将fragment传入到showFragment
        showFragment(selectedScreen);
    }

    /**
     * @function 显示fragment
     * @param fragment
     */
    private void showFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    /**
     * @function 每个item初始化图片和文本的颜色,可以修改item显示的文本颜色
     * @param position
     * @return
     */
    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                //将颜色的int传入simpleitem中
                //.withIconTint(color(R.color.textColorSecondary))
                //.withTextTint(color(R.color.textColorPrimary))
                //.withSelectedIconTint(color(R.color.colorAccent))
                //.withSelectedTextTint(color(R.color.colorAccent));
                //换成橘色
                .withIconTint(color(R.color.color_FF8C00))
                .withTextTint(color(R.color.color_FF8C00))
                .withSelectedIconTint(color(R.color.colorAccent))
                .withSelectedTextTint(color(R.color.colorAccent));
    }

    /**
     * @function 加载Item的标题
     * @return  String
     */
    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    /**
     * @function 加载Item的图片
     * @return  icons
     */
    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }
}
