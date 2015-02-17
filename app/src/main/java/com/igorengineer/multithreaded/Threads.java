package com.igorengineer.multithreaded;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.igorengineer.multithreaded.R.id.list;
import static com.igorengineer.multithreaded.R.layout.activity_threads;

/**
 * Waits for user to click on buttons
 */
public class Threads extends ActionBarActivity implements View.OnClickListener {

    private ProgressBar mProgress;
    private int mProgressStatus = 0;

    /**
     * Creates 3 buttons: Load, Save, and Clear list
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(activity_threads);

        mProgress = (ProgressBar) findViewById(R.id.progressBar);

        Button one = (Button) findViewById(R.id.buttonCreate);
        one.setOnClickListener(this); // calling onClick() method
        Button two = (Button) findViewById(R.id.buttonLoad);
        two.setOnClickListener(this);
        Button three = (Button) findViewById(R.id.buttonClear);
        three.setOnClickListener(this);
    }

    /**
     * Defines what each button does
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.buttonCreate:
                (new Thread() {
                    @Override
                    public void run() {
                        try {
                            FileOutputStream fos = openFileOutput("numbers", Context.MODE_PRIVATE);
                            String temp;
                            for (int i = 1; i < 10; ++i) {
                                temp = i + "\n";
                                fos.write(temp.getBytes());
                                mProgress.setProgress((++mProgressStatus) * 10);
                                Thread.sleep(250);
                            }
                            mProgress.setProgress((mProgressStatus = 0));
                            fos.close();
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                break;

            case R.id.buttonLoad:
                try {
                    BufferedReader inputReader = new BufferedReader(new InputStreamReader(openFileInput("numbers")));
                    String inputString;
                    List<String> items = new ArrayList<>();
                    while ((inputString = inputReader.readLine()) != null) {
                        items.add(inputString);
                        mProgress.setProgress((++mProgressStatus) * 10);
                        Thread.sleep(250);
                    }
                    mProgress.setProgress((mProgressStatus = 0));
                    ListView myList = (ListView) findViewById(list);
                    myList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items));
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.buttonClear:
                ListView myList = (ListView) findViewById(list);
                myList.setAdapter(null);
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_threads, menu);
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
}
