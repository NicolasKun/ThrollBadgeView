package im.unicolas.trolltablayout;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import im.unicolas.trollbadgeview.FragmentPagerAdapter;
import im.unicolas.trollbadgeview.LabelView;
import im.unicolas.trollbadgeview.LimitPagerView;
import im.unicolas.trollbadgeview.tablayout.TabLayout;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private static final String TAG = "MainActivity";

    private TabLayout tabLayout;
    private LimitPagerView pagerView;

    private List<String> titles;
    private List<Fragment> fragments;
    private LabelView mLabelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    private void init() {
        titles = new ArrayList<>();
        fragments = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.cm_tab_layout);
        pagerView = (LimitPagerView) findViewById(R.id.cm_pager_view);
        mLabelView = (LabelView) findViewById(R.id.cm_label_view);

        setUpTab();

        //设置角标模式
        mLabelView.setLabelMode(LabelView.LABEL_MODE_IMG);
        mLabelView.setBitmap4Icon(R.mipmap.test_cart);
        mLabelView.setLabelViewVisiable(true);
    }

    private void setUpTab() {
        titles.add("Sans");
        titles.add("Frisk");
        titles.add("Chara");
        titles.add("Temmie");

        for (int i = 0; i < 4; i++) {
            fragments.add(TestFragment.instance(i));
        }

        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.color_grey_800));

        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        pagerView.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(pagerView);

        //setupWithViewPager 后设置如下代码
        for (int i = 0; i < titles.size(); i++) {
            LabelView view = new LabelView(this);
            view.setWordShow(titles.get(i));
            view.setLabelViewVisiable(true);
            if (i == 0)
                view.setWordColor(0xff616161);
            else if (i == 1)
                view.setLabelNum("7");
            else if (i == 2)
                view.setLabelNum("12");
            else if (i == 3)
                view.setLabelNum("99+");

            tabLayout.getTabAt(i).setCustomView(view);
        }

        tabLayout.addOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        LabelView labelView = (LabelView) tab.getCustomView();
        labelView.setWordColor(0xff616161);   //设置文字在选中状态下的颜色
        int position = tab.getPosition();
        switch (position) {
            case 0:
                mLabelView.setLabelNum(null);
                break;
            case 1:
                mLabelView.setLabelNum("7");
                break;
            case 2:
                mLabelView.setLabelNum("12");
                break;
            case 3:
                mLabelView.setLabelNum("99+");
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        LabelView labelView = (LabelView) tab.getCustomView();
        labelView.setWordColor(0xffbdbdbd);   //设置文字在未选中状态下的颜色
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {}

    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

}
