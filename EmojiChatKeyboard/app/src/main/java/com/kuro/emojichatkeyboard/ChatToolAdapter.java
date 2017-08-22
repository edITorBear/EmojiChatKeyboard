package com.kuro.emojichatkeyboard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import io.github.rockerhieu.emojicon.EmojiconsFragment;

/**
 * Created by nnv on 2017/8/22.
 */

public class ChatToolAdapter extends FragmentStatePagerAdapter {

    private int mMode;

    public ChatToolAdapter(FragmentManager fm, int mode) {
        super(fm);
        this.mMode = mode;
    }

    @Override
    public Fragment getItem(int position) {
        if (mMode == ChatToolLayout.LAYOUT_TYPE_MORE) {
            return new MoreFunFragment();
        }
        if (mMode == ChatToolLayout.LAYOUT_TYPE_FACE) {
            return EmojiconsFragment.newInstance(true);
        }
        return null;
    }

    @Override
    public int getCount() {
        if (mMode == ChatToolLayout.LAYOUT_TYPE_FACE) {
            return 1;
        }
        if (mMode == ChatToolLayout.LAYOUT_TYPE_MORE) {
            return 1;
        }
        return 0;
    }

}
