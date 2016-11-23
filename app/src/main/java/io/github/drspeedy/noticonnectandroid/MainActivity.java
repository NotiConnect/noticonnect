package io.github.drspeedy.noticonnectandroid;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.github.drspeedy.noticonnectandroid.services.NotiConnectService;

public class MainActivity extends FragmentActivity {
    // constants
    private int LOGIN_REQUEST_CODE = 1;

    // ui elements
    private TextView mServiceStatusLabel;
    private Button mStartServiceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // wire up the ui elements
        mServiceStatusLabel = (TextView) findViewById(R.id.activity_main_service_status_label);
        mStartServiceButton = (Button) findViewById(R.id.activity_main_start_service_button);
        mStartServiceButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        launchAccessibilitySettings();
                        updateUI();
                    }
                }
        );

        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivityForResult(loginIntent, LOGIN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    updateUI();
                    break;
                default:
                    //
                    break;
            }
        }

    }

    private boolean serviceRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (service.service.getClassName() == NotiConnectService.class.getName()) {
                return true;
            }
        }
        return false;
    }

    private void updateUI()
    {
        if (serviceRunning()) {
            mServiceStatusLabel.setText(R.string.activity_main_service_running);
            mStartServiceButton.setVisibility(View.GONE);
        } else {
            mServiceStatusLabel.setText(R.string.activity_main_service_stopped);
            mStartServiceButton.setVisibility(View.VISIBLE);
        }
    }

    private void launchAccessibilitySettings() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startService(intent);
    }
}
