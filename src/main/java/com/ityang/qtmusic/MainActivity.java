package com.ityang.qtmusic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ityang.qtmusic.factory.FragmentFactory;
import com.ityang.qtmusic.fragment.MyMusicFragment;
import com.ityang.qtmusic.fragment.NetMusicFragment;
import com.ityang.qtmusic.service.PlayService;

public class MainActivity extends BaseActivity {
    private TabLayout tabLayout;
    private ViewPager pager;
    private MyPagerAdapter adapter;

    private final Handler handler = new Handler();
    private MyMusicFragment myMusicFragment;
    private NetMusicFragment netMusicFragment;
    private SharedPreferences sp;
    private int playMode;
    public Toolbar toolBar;
    public ImageButton mFabButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeBlue);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolBar =
                (Toolbar) findViewById(R.id.toolbar);
        toolBar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolBar);
        // Get a support ActionBar corresponding to this toolbar


        // Enable the Up button
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mFabButton = (ImageButton) findViewById(R.id.fab);
        mFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "定位到当前播放歌曲位置", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                myMusicFragment.affirmPosition();
            }
        });
        sp = getSharedPreferences("config", MODE_PRIVATE);
        //获取播放模式
        playMode = sp.getInt("playMode", 0);

        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);

        tabLayout.setupWithViewPager(pager);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if(position==1){
                    mFabButton.setVisibility(View.GONE);
                }else {
                    mFabButton.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_love:
                startActivity(new Intent(this, LikeMusicActivity.class));
                break;
            case R.id.action_exit:
                //退出应用
                unBindService();
                stopService(new Intent(this,PlayService.class));
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 绑定成功后将playservice注入fragment
     *
     * @param playService
     */
    @Override
    protected void serviceBinderSuccess(PlayService playService) {
        myMusicFragment.bindSuccess(playService);
        //设置播放模式
        playService.setPlayMode(playMode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //保存播放模式
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("playMode", playService.getPlayMode());
        editor.commit();
    }

    public PlayService getPlayService() {
        return playService;
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private final String[] TITLES = {"我的音乐", "网络推荐"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                if (myMusicFragment == null) {
                    myMusicFragment = (MyMusicFragment) FragmentFactory.getFragment(position);
                }
                return myMusicFragment;
            } else if (position == 1) {
                if (netMusicFragment == null) {
                    netMusicFragment = (NetMusicFragment) FragmentFactory.getFragment(position);
                }
                return netMusicFragment;
            }
            return null;
        }
    }

    /*
   按两次back键退出
    */
    long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000)  //System.currentTimeMillis()无论何时调用，肯定大于2000
            {
                Toast.makeText(this, "再按一次退出千听音乐", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
