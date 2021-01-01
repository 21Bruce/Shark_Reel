package com.dc.shark_reel_t5;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.core.content.FileProvider;
import androidx.viewpager2.widget.ViewPager2;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import com.dc.shark_reel_t5.ui.main.SectionsPagerAdapter;

import java.io.File;
import java.io.FileOutputStream;

import static com.dc.shark_reel_t5.ui.main.Hooks.*;


import com.google.android.material.tabs.TabLayoutMediator;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "mainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter;
        ViewPager2 viewPager = findViewById(R.id.view_pager);
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {if(position < sectionsPagerAdapter.getItemCount()){
                tab.setText("Hook " + (position+1));
                }
                }

        ).attach();

        FloatingActionButton export = findViewById(R.id.export);
        FloatingActionButton add = findViewById(R.id.add);
        FloatingActionButton delete = findViewById(R.id.delete);
        FloatingActionButton clear = findViewById(R.id.clear);

        //create first hook
        sectionsPagerAdapter.addHookFrag(tabLayout);
        addData();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sectionsPagerAdapter.addHookFrag(tabLayout);
                tabLayout.selectTab(tabLayout.getTabAt(tabLayout.getTabCount()-1));
                addData();

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData(tabLayout.getSelectedTabPosition());
                sectionsPagerAdapter.delHookFrag(tabLayout.getSelectedTabPosition(), tabLayout, true);

                if(tabLayout.getTabCount() == 0){
                    delete.post(new Runnable() {
                        @Override
                        public void run() {
                            add.performClick();
                        }
                    });
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int position = tabLayout.getSelectedTabPosition();
                deleteData(position);
                addData(position);
                sectionsPagerAdapter.delHookFrag(position, tabLayout, false);
                try{
                    sectionsPagerAdapter.addHookFrag(position, tabLayout, false);

                    clear.post(new Runnable() {
                        @Override
                        public void run() {
                            tabLayout.selectTab(tabLayout.getTabAt(position));
                        }
                    });
                } catch(Exception e){
                    Log.w(TAG, "WARNING, Main Thread is doing too much");
                    sectionsPagerAdapter.addHookFrag(position, tabLayout, false);

                    clear.post(new Runnable() {
                        @Override
                        public void run() {
                            tabLayout.selectTab(tabLayout.getTabAt(position));
                        }
                    });
                }

            }
        });




        //when click run code to export data to CSV
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //data type to store CSV, not stringBuilder is used
                //this is because it can store both commas and spaces, both critical to CSVs
                StringBuilder data = new StringBuilder();


                //add the column names from Hooks
                for (int index = 0; index < getDataTypeLength(); index++) {

                    if (index != 0) {
                        data.append(",");
                    }

                    data.append(getDataType(index));
                }

                //add the elements from each hook
                int i = 0;
                while (i == 0 || i < getHookAmount()-1) {

                    data.append("\n" + sectionsPagerAdapter.getTitle(i));
                    for (int j = 0; j < getCategoriesLength(i); j++) {

                        data.append("," + getData(i, j));
                    }

                    i++;
                }

                try {
                    //savefile into device
                    FileOutputStream out = openFileOutput("data.csv", Context.MODE_PRIVATE);
                    out.write(data.toString().getBytes());
                    out.close();

                    //name file, give location, approve for exporting, and export
                    Context context = getApplicationContext();
                    File fileLocation = new File(getFilesDir(), "data.csv");
                    Uri path = FileProvider.getUriForFile(context, "com.example.exportcsv.fileprovider", fileLocation);
                    Intent fileIntent = new Intent(Intent.ACTION_SEND);
                    fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
                    fileIntent.putExtra(Intent.EXTRA_STREAM, path);
                    fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    fileIntent.setData(path);
                    startActivity(Intent.createChooser(fileIntent, "send mail"));

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "ERROR, data csv not received or formatted incorrectly");
                }

                //clear all hook fragment and adapter data
                sectionsPagerAdapter.clearHooks(tabLayout);
                clearData();

                //click add once everything has loaded, uses post to run on main thread
                add.post(new Runnable(){
                    @Override
                    public void run() {
                        add.performClick();
                    }
                });

            }

        });


    }

}