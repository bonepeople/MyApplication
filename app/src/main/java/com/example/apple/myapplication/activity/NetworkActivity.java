package com.example.apple.myapplication.activity;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.apple.myapplication.R;
import com.example.apple.myapplication.utils.ToastUtil;

public class NetworkActivity extends AppCompatActivity {
    private TextView textView_content;
    private NetworkCallbackImpl networkCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        textView_content = findViewById(R.id.textView_content);
        findViewById(R.id.button_network).setOnClickListener(v -> {
            if (isNetworkAvailable()) {
                ToastUtil.showToast("当前网络可用");
            } else {
                ToastUtil.showToast("当前网络不可用");
            }
        });

        initData();
    }

    private void initData() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkRequest request = new NetworkRequest.Builder().build();
        manager.registerNetworkCallback(request, networkCallback = new NetworkCallbackImpl());
    }

    @Override
    protected void onDestroy() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        manager.unregisterNetworkCallback(networkCallback);
        super.onDestroy();
    }

    private boolean isNetworkAvailable() {
        try {
            ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            Network[] nets = manager.getAllNetworks();
            for (Network net : nets) {
                NetworkInfo info = manager.getNetworkInfo(net);
                if (info.isConnected())
                    return true;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public class NetworkCallbackImpl extends ConnectivityManager.NetworkCallback {

        @Override
        public void onAvailable(Network network) {
            super.onAvailable(network);
            Log.e("bone", "网络连接了");
            runOnUiThread(() -> textView_content.setText("网络连接了"));
        }

        @Override
        public void onLost(Network network) {
            super.onLost(network);
            Log.e("bone", "网络断开了");
            runOnUiThread(() -> textView_content.setText("网络断开了"));
        }

        @Override
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities);
            if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.e("bone", "wifi网络已连接");
                    runOnUiThread(() -> textView_content.setText("wifi网络已连接"));
                } else {
                    Log.e("bone", "移动网络已连接");
                    runOnUiThread(() -> textView_content.setText("移动网络已连接"));
                }
            }
        }
    }
}
