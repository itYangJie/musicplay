package com.ityang.qtmusic.factory;

import android.support.v4.app.Fragment;

import com.ityang.qtmusic.fragment.MyMusicFragment;
import com.ityang.qtmusic.fragment.NetMusicFragment;

/**
 * Created by Administrator on 2016/2/25.
 */
public class FragmentFactory {

    private FragmentFactory(){

    }
    //fragment的缓存
    //private static List<Fragment> fragments = new ArrayList<>();

    /**
     * 返回fragment的工厂方法
     * @param position
     * @return
     */
    public static Fragment getFragment(int position){
        Fragment fragment = null;
        if(position==0){
            fragment = new MyMusicFragment();
        }else if(position==1){
            fragment = new NetMusicFragment();
        }
        return fragment;
    }
}
