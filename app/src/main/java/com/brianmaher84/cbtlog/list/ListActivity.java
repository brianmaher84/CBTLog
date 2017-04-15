package com.brianmaher84.cbtlog.list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.brianmaher84.cbtlog.db.ActivityDBHelper;
import com.brianmaher84.cbtlog.R;

public class ListActivity extends AppCompatActivity {

    public static final String SITUATION_ID = "SITUATION_ID";
    public static final int RESULT_OK = 1;
    private ListView listView;
    private SimpleCursorAdapter cursorAdapter;
    private ActivityDBHelper dbHelper;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;
        dbHelper = new ActivityDBHelper(this);

        final Cursor cursor = dbHelper.getAllActivities();
        String [] columns = getColumns();
        int [] widgets = getWidgets();

        cursorAdapter = new SimpleCursorAdapter(this, R.layout.situation_info,
                cursor, columns, widgets, 0);
        listView = (ListView)findViewById(R.id.situationList);
        listView.setAdapter(cursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                Cursor itemCursor = (Cursor) ListActivity.this.listView.getItemAtPosition(position);
                int situationId = itemCursor.getInt(itemCursor.getColumnIndex(ActivityDBHelper.COLUMN_ID));
                Intent intent = new Intent(context, CreateOrEditActivity.class);
                intent.putExtra(SITUATION_ID, situationId);
                startActivityForResult(intent, RESULT_OK);
            }
        });
    }

    private int[] getWidgets() {
        return new int[] {
                R.id.situationID,
                R.id.situation
        };
    }

    @NonNull
    private String[] getColumns() {
        return new String[] {
                ActivityDBHelper.COLUMN_ID,
                ActivityDBHelper.COLUMN_SITUATION
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case RESULT_OK:
                final Cursor cursor = dbHelper.getAllActivities();
                Cursor oldCursor = cursorAdapter.swapCursor(cursor);
                cursorAdapter.notifyDataSetChanged();
                oldCursor.close();
                break;

        }
    }

}
