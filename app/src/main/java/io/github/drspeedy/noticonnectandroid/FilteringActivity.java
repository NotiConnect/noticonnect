package io.github.drspeedy.noticonnectandroid;

import android.support.v4.app.Fragment;

public class FilteringActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new FilteringFragment();
    }
}
