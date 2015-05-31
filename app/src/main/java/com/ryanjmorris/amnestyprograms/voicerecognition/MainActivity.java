package com.ryanjmorris.amnestyprograms.voicerecognition;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity
{

    private static final int REQUEST_RECOGNIZE = 100;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv = new TextView(this);
        setContentView(tv);
        //setContentView(R.layout.activity_main);

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Tell me your name!");

        try
        {
            startActivityForResult(intent, REQUEST_RECOGNIZE);
        }
        catch (ActivityNotFoundException e)
        {
            //If no recoginizer is found, it will attempt to install it.
            showDownloadingDialog();
        }
    }

    public void showDownloadingDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Not Available");
        builder.setMessage("There is no recognizition application installed." +
                "Would you like to download one?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Download, for example, Google Voice Search
                Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                marketIntent.setData(Uri.parse("market://details?" +
                        "id=com.google.android.voicesearch"));
            }

        });
        builder.setNegativeButton("No", null);
        builder.create().show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_RECOGNIZE && resultCode == Activity.RESULT_OK)
        {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            StringBuilder sb = new StringBuilder();
            for (String piece : matches)
            {
                sb.append(piece);
                sb.append('\n');
            }
            tv.setText(sb.toString());
        }
        else
        {
            Toast.makeText(this, "Operation Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}
