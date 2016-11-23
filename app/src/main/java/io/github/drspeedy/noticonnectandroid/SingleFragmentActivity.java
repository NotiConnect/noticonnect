package io.github.drspeedy.noticonnectandroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public abstract
class SingleFragmentActivity
        extends AppCompatActivity
{
    // public methods
    @Override
    public
    void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_fragment );

        FragmentManager
                fragmentManager
                = getSupportFragmentManager();

        Fragment
                fragment
                = fragmentManager.findFragmentById( R.id.fragment_container );

        if ( fragment
                == null )
        {
            fragment
                    = createFragment();
            fragmentManager
                    .beginTransaction()
                    .add(
                            R.id.fragment_container,
                            fragment
                    )
                    .commit();
        }
    }

    // protected methods
    protected abstract
    Fragment createFragment();
}
