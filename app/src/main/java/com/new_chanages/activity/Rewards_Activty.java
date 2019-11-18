package com.new_chanages.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.contus.residemenu.MenuActivity;
import com.new_chanages.Rewards_viewpager_Adapter;
import com.polls.polls.R;

public class Rewards_Activty extends AppCompatActivity implements View.OnClickListener {


    private ViewPager mViewPager;

    private String[] titles = new String[]{"Rewards", "Redeem"};

    private TabLayout tabLayout;
    ImageView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards__activty);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Set up the ViewPager with the sections adapter.
        back_button    = findViewById(R.id.back_button);
        tabLayout =  findViewById(R.id.tabs);
        mViewPager =  findViewById(R.id.viewpager);
        boolean is_from_redeem=false;
      if(getIntent().getExtras()!=null)
      {
           is_from_redeem =getIntent().getExtras().getBoolean("is_from_redeem",false);
      }
        mViewPager.setAdapter(new Rewards_viewpager_Adapter(getSupportFragmentManager(), titles));
        if(is_from_redeem)
        {
            mViewPager.setCurrentItem(1);
        }
        tabLayout.setupWithViewPager(mViewPager);

        back_button.setOnClickListener(this);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==500&&resultCode==Activity.RESULT_OK)
        {
            mViewPager.setAdapter(new Rewards_viewpager_Adapter(getSupportFragmentManager(), titles));
            mViewPager.setCurrentItem(1);
            tabLayout.setupWithViewPager(mViewPager);
        }
    }

  /*
   Commented By Nikita
   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rewards__activty, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_button:
                Intent intent = new Intent(Rewards_Activty.this, MenuActivity.class);
                startActivity(intent);
                //finish();
                break;
        }
    }
}
