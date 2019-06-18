package com.example.apple.myapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.apple.myapplication.R;
import com.example.apple.myapplication.adapter.RecyclerViewAdapter;
import com.example.apple.myapplication.widget.LinearItemDecoration;

/**
 * 测试自定义的recyclerView分割线
 *
 * @author bonepeople
 */
public class ItemDecorationActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_decoration);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new LinearItemDecoration(5).setColor(0xFF666699));
        recyclerView.setAdapter(new RecyclerViewAdapter());
    }
}
