package com.mai.greendaox.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mai.annotate.ManyToMany;
import com.mai.annotate.OneToMany;
import com.mai.greendaox.R;

/**
 * Created by mai on 16/7/24.
 */
public class HomeActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.bt_base).setOnClickListener(this);
        findViewById(R.id.bt_one_one).setOnClickListener(this);
        findViewById(R.id.bt_one_many).setOnClickListener(this);
        findViewById(R.id.bt_many_many).setOnClickListener(this);
        findViewById(R.id.bt_cascade).setOnClickListener(this);
        findViewById(R.id.bt_lazy).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_base:
                nextActivity(BasisActivity.class);
                break;
            case R.id.bt_one_one:
                nextActivity(OneToOneActivity.class);
                break;
            case R.id.bt_one_many:
                nextActivity(OneToManyActivity.class);
                break;
            case R.id.bt_many_many:
                nextActivity(ManyToManyActivity.class);
                break;
            case R.id.bt_cascade:
                nextActivity(CascadeActivity.class);
                break;
            case R.id.bt_lazy:
                nextActivity(LazyActivity.class);
                break;
        }
    }

    private void nextActivity(Class activity){
        startActivity(new Intent(HomeActivity.this, activity));
    }
}
