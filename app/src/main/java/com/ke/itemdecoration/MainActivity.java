package com.ke.itemdecoration;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;


import com.ke.library.StickyDecoration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        final List<NameBean> nameBeans = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            if (i < 10) {
                nameBeans.add(new NameBean("0" + i));
            } else {
                nameBeans.add(new NameBean(String.valueOf(i)));
            }
        }




        StickyDecoration stickyDecoration = new StickyDecoration
                .Builder(new StickyDecoration.GroupCallback() {
            @NonNull
            @Override
            public String getGroupName(int position) {
                return nameBeans.get(position).getGroupName();
            }
        })
                .setGroupBackgroundColor(Color.parseColor("#FFFF0000"))
                .setTextSize(getResources().getDimensionPixelSize(R.dimen.group_text_size))
                .setGroupHeight((int) getResources().getDimension(R.dimen.group_height))
                .setTextColor(Color.WHITE)
                .setTextMargin(getResources().getDimensionPixelOffset(R.dimen.group_text_margin))
                .build();

        recyclerView.addItemDecoration(stickyDecoration);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));


        recyclerView.setAdapter(new Adapter(nameBeans));
    }
}
