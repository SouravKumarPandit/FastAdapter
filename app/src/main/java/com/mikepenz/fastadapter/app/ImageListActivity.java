package com.mikepenz.fastadapter.app;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.fastadapter.app.dummy.ImageDummyData;
import com.mikepenz.fastadapter.app.items.ImageItem;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.mikepenz.materialize.MaterializeBuilder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ImageListActivity extends AppCompatActivity {
    //save our FastAdapter
    private FastItemAdapter<ImageItem> mFastItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        findViewById(android.R.id.content).setSystemUiVisibility(findViewById(android.R.id.content).getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.sample_image_list);

        //style our ui
        new MaterializeBuilder().withActivity(this).build();

        //create our FastAdapter which will manage everything
        mFastItemAdapter = new FastItemAdapter<>();

        //configure our fastAdapter
        mFastItemAdapter.setOnClickListener((v, adapter, item, position) -> {
            Toast.makeText(v.getContext(), item.mName, Toast.LENGTH_SHORT).show();
            return false;
        });

        //get our recyclerView and do basic setup
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        //find out how many columns we display
        int columns = getResources().getInteger(R.integer.wall_splash_columns);
        if (columns == 1) {
            //linearLayoutManager for one column
            rv.setLayoutManager(new LinearLayoutManager(this));
        } else {
            //gridLayoutManager for more than one column ;)
            rv.setLayoutManager(new GridLayoutManager(this, columns));
        }
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(mFastItemAdapter);

        //fill with some sample data
        mFastItemAdapter.add(ImageDummyData.getImageItems());

        //restore selections (this has to be done after the items were added
        mFastItemAdapter.withSavedInstanceState(savedInstanceState);

        //a custom OnCreateViewHolder listener class which is used to create the viewHolders
        //we define the listener for the imageLovedContainer here for better performance
        //you can also define the listener within the items bindView method but performance is better if you do it like this
        mFastItemAdapter.addEventHook(new ImageItem.ImageItemHeartClickEvent());

        //set the back arrow in the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the adapter to the bundle
        outState = mFastItemAdapter.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle the click on the back arrow click
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
