package com.brianmaher84.cbtlog.list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.brianmaher84.cbtlog.db.ActivityDBHelper;
import com.brianmaher84.cbtlog.R;

import java.util.List;

public class CreateOrEditActivity extends AppCompatActivity {

    private ActivityDBHelper dbHelper;
    private EditText situationEditText;
    private EditText feelingsEditText;
    private EditText thoughtsEditText;
    private int situationId;
    private Intent resultIntent;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_edit);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        situationId = getIntent().getIntExtra(ListActivity.SITUATION_ID, 0);

        setContentView(R.layout.activity_create_or_edit);
        situationEditText = (EditText) findViewById(R.id.editSitution);
        feelingsEditText = (EditText) findViewById(R.id.editFeelings);
        thoughtsEditText = (EditText) findViewById(R.id.editThoughts);


        dbHelper = new ActivityDBHelper(this);

        if(situationId > 0) {
            Cursor rs = dbHelper.getActivity(situationId);
            rs.moveToFirst();
            String situation = rs.getString(rs.getColumnIndex(ActivityDBHelper.COLUMN_SITUATION));
            String feelings = rs.getString(rs.getColumnIndex(ActivityDBHelper.COLUMN_FEELINGS));
            String thoughts = rs.getString(rs.getColumnIndex(ActivityDBHelper.COLUMN_THOUGHTS));
            if (!rs.isClosed()) {
                rs.close();
            }

            situationEditText.setEnabled(true);
            situationEditText.setFocusableInTouchMode(true);
            situationEditText.setClickable(true);

            feelingsEditText.setEnabled(true);
            feelingsEditText.setFocusableInTouchMode(true);
            feelingsEditText.setClickable(true);

            thoughtsEditText.setEnabled(true);
            thoughtsEditText.setFocusableInTouchMode(true);
            thoughtsEditText.setClickable(true);

            situationEditText.setText(situation);
            feelingsEditText.setText( feelings);
            thoughtsEditText.setText(thoughts);

            FloatingActionButton fabSave = (FloatingActionButton) findViewById(R.id.fabSave);
            fabSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    persistActivity(view);
                }
            });

            FloatingActionButton fabDelete = (FloatingActionButton) findViewById(R.id.fabDelete);
            fabDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dbHelper.deleteActivity(situationId);
                    Snackbar.make(view, "Deleted!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    finishActivity();

                }
            });
        }
    }

    public void persistActivity(View view) {
        if(situationId > 0) {
            if(dbHelper.updateActivity(situationId, situationEditText.getText().toString(),
                    feelingsEditText.getText().toString(),
                    thoughtsEditText.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Acitivy Update Successful", Toast.LENGTH_SHORT).show();
                finishActivity();
            }
            else {
                Toast.makeText(getApplicationContext(), "Activity Update Failed", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            if(dbHelper.insertActivity(situationEditText.getText().toString(),
                    feelingsEditText.getText().toString(),
                    thoughtsEditText.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Activity Inserted", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Could not Insert Activity", Toast.LENGTH_SHORT).show();
            }
            Snackbar.make(view, "Updated!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            finishActivity();
        }
    }

    public void finishActivity() {

        resultIntent = new Intent(context, ListActivity.class);
        setResult(ListActivity.RESULT_OK, resultIntent);
        finish();
    }
}
