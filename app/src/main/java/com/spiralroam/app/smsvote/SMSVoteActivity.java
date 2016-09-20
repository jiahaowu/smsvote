package com.spiralroam.app.smsvote;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;


import com.spiralroam.app.smsvote.database.DatabaseHelper;
import com.spiralroam.app.smsvote.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SMSVoteActivity extends Activity implements OnChangedListener {
    public static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    // Define the DataBase Operations;
    public SQLiteDatabase db;
    final public static String db_name = "SMS_Vote.db";
    final public DatabaseHelper DBhelper = new DatabaseHelper(this, db_name,
            null, 1);
    public SlipButton voteButton;
    public SlipButton stopBroadcastButton;
    public static TextView countNumberText;
    public static TextView errorCntText;
    public static TextView remainGold;

    // Define the Button
    public static Button appOfferBtn;
    public static Button resetBtn;
    public static Button configBtn;
    public static Button listResultBtn;
    public static Button listErrBtn;

    // the text shown on the Main activity's Top
    public static Integer counts = 0;
    public static Integer errCounts = 0;
    // if use the array to record the Votes
    public static Integer[] votes = new Integer[30];
    public static String[] errors = new String[30];

    public static int YAxisMax = 5;

    // use max people to record the number of people
    public static String cntPNname = "cntPeopleNumber";
    public static String maxVPname = "maxVotesPerPeople";
    public static Integer maxpeople = 0;
    public static Integer maxvotes = 0;
    // Ads Unit
    public static String adsUnit;
    public static Integer awardPoints = 0;
    public static boolean connection = true;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SMSBroadcastReceiver.func = false;
        SMSBroadcastReceiver.stopBroadcast = false;

        // define the reset Button
        resetBtn = (Button) findViewById(R.id.btn_Restart);
        resetBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (SMSBroadcastReceiver.func)
                    onresetkPressed(SMSVoteActivity.this);
                else {
                    SMSBroadcastReceiver.func = false;
                    SMSBroadcastReceiver.stopBroadcast = false;
                    voteButton.setChecked(false);
                    stopBroadcastButton.setChecked(false);
                    votes = new Integer[maxpeople + 1];
                    errors = new String[maxpeople + 1];
                    for (int i = 0; i <= maxpeople; i++) {
                        errors[i] = null;
                        votes[i] = 0;
                    }
                    errCounts = 0;
                    counts = 0;
                    maxpeople = 5;
                    maxvotes = 1;
                    countNumberText.setText(R.string.countingNumber_initial);
                    errorCntText.setText(R.string.countingNumber_initial);
                }
            }
        });
        // Define the configure Button
        configBtn = (Button) findViewById(R.id.btn_Config);
        configBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (SMSBroadcastReceiver.func == false) {
                    SMSBroadcastReceiver.func = false;
                    SMSBroadcastReceiver.stopBroadcast = false;
                    voteButton.setChecked(false);
                    countNumberText
                            .setText(R.string.countingNumber_initial);
                    errorCntText.setText(R.string.countingNumber_initial);
                    Intent intent = new Intent(SMSVoteActivity.this,
                            ConfigActivity.class);
                    SMSVoteActivity.this.startActivity(intent);
                } else
                    onSettingkPressed(SMSVoteActivity.this);


            }
        });
        // Define the LsitResult Button
        listResultBtn = (Button) findViewById(R.id.Listresluts);
        listResultBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(SMSVoteActivity.this,
                        ListReslut.class);
                SMSVoteActivity.this.startActivity(intent);
            }
        });
        // DEfine listErrBtn
        listErrBtn = (Button) findViewById(R.id.Errors_detail);
        listErrBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(SMSVoteActivity.this, ListErr.class);
                SMSVoteActivity.this.startActivity(intent);
            }
        });

        // Create SharedPreferences
        SharedPreferences preferrence = getSharedPreferences("SMSVote_setting",
                0);

        // Test the SharedPreferrences function
        preferrence.edit().putString("DataBase", db_name).commit();
        maxpeople = (int) preferrence.getLong(cntPNname, 5);
        maxvotes = (int) preferrence.getLong(maxVPname, 1);

        // Create the vote array from the maxpeople parameter
        votes = new Integer[maxpeople + 1];
        errors = new String[maxpeople + 1];
        for (int i = 0; i <= maxpeople; i++) {
            votes[i] = 0;
            errors[i] = null;
        }
        // Create the DB;
        db = DBhelper.getWritableDatabase();

        Button showResultBtn = (Button) findViewById(R.id.Show_the_barchart);

        // Define the TextView of CountNumber
        countNumberText = (TextView) findViewById(R.id.CountingNumber);
        countNumberText.setText(R.string.countingNumber_initial);
        countNumberText.setSingleLine();

        // Define the TextView of error counts
        errorCntText = (TextView) findViewById(R.id.errors_cnt);
        errorCntText.setText(R.string.countingNumber_initial);
        errorCntText.setSingleLine();

        showResultBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                // maxpeople = 5;
                // maxvotes = 6;// for test
                XYMultipleSeriesRenderer renderer = getBarDemoRenderer();
                Intent intent = ChartFactory.getBarChartIntent(
                        SMSVoteActivity.this, getBarDemoDataset(), renderer,
                        Type.DEFAULT);
                SMSVoteActivity.this.startActivity(intent);
            }
        });

        voteButton = (SlipButton) findViewById(R.id.vote_switch_slipButton);
        voteButton.SetOnChangedListener("VoteButton", this);
        // button.setEnabled(false) Set button as not enable
        voteButton.setChecked(false);
        // initialize the button as OFF

        stopBroadcastButton = (SlipButton) findViewById(R.id.stopBroadcast_switch_slipButton);
        stopBroadcastButton.SetOnChangedListener("StopBroadcastButton", this);
        stopBroadcastButton.setChecked(false);

        TextView descript = (TextView) findViewById(R.id.description);
        descript.setText((String) SMSVoteActivity.this
                .getText(R.string.descrip1)
                + maxpeople.toString()
                + (String) SMSVoteActivity.this.getText(R.string.descrip2)
                + maxvotes.toString()
                + (String) SMSVoteActivity.this.getText(R.string.descrip3));
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        TextView descript = (TextView) findViewById(R.id.description);
        descript.setText((String) SMSVoteActivity.this
                .getText(R.string.descrip1)
                + maxpeople.toString()
                + (String) SMSVoteActivity.this.getText(R.string.descrip2)
                + maxvotes.toString()
                + (String) SMSVoteActivity.this.getText(R.string.descrip3));
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        TextView descript = (TextView) findViewById(R.id.description);
        descript.setText((String) SMSVoteActivity.this
                .getText(R.string.descrip1)
                + maxpeople.toString()
                + (String) SMSVoteActivity.this.getText(R.string.descrip2)
                + maxvotes.toString()
                + (String) SMSVoteActivity.this.getText(R.string.descrip3));
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        SMSBroadcastReceiver.func = false;
    }

    // the slip button Pressed Function
    public void OnChanged(String strName, boolean CheckState) {
        if (strName == "VoteButton") {
            if (CheckState) {
                Toast.makeText(SMSVoteActivity.this, strName + " is ON!",
                        Toast.LENGTH_LONG).show();
                SMSBroadcastReceiver.func = true;
                countNumberText.setText(counts.toString());
                errorCntText.setText(errCounts.toString());

            } else {
                Toast.makeText(SMSVoteActivity.this, strName + " is OFF!",
                        Toast.LENGTH_LONG).show();
                SMSBroadcastReceiver.func = false;
                countNumberText.setText(R.string.countingNumber_initial);
                errorCntText.setText(R.string.countingNumber_initial);
            }
        } else if (strName == "StopBroadcastButton") {
            if (CheckState) {
                Toast.makeText(SMSVoteActivity.this, strName + " is ON!",
                        Toast.LENGTH_LONG).show();
                SMSBroadcastReceiver.stopBroadcast = true;
            } else {
                Toast.makeText(SMSVoteActivity.this, strName + " is OFF!",
                        Toast.LENGTH_LONG).show();
                SMSBroadcastReceiver.stopBroadcast = false;
            }
        }
    }

    // Deal with the Back Button Press Action
    public void onBackPressed() {
        onBackPressed_local(this);
    }

    // The Back button Pressed function
    public void onBackPressed_local(final Activity context) {
        Dialog dialog = new AlertDialog.Builder(context)
                .setTitle((String) this.getText(R.string.sys_Warming_Title))
                .setMessage((String) this.getText(R.string.exit_Warming))
                .setPositiveButton((String) this.getText(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                SMSBroadcastReceiver.func = false;
                                counts = 0;
                                // stop the onRecive Listener
                                finish();
                            }
                        })
                .setNeutralButton((String) this.getText(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                return;
                            }
                        }).create(); // Create the Button
        dialog.show(); // Show the Dialog
    }

    // when reset button is down
    public void onresetkPressed(final Activity context) {
        Dialog dialog = new AlertDialog.Builder(context)
                .setTitle((String) this.getText(R.string.sys_Warming_Title))
                .setMessage((String) this.getText(R.string.reset_Warming))
                .setPositiveButton((String) this.getText(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                return;
                            }
                        }).create();

        dialog.show(); // Show the Dialog
    }

    // When the setting button is pressed
    public void onSettingkPressed(final Activity context) {
        Dialog dialog = new AlertDialog.Builder(context)
                .setTitle((String) this.getText(R.string.sys_Warming_Title))
                .setMessage((String) this.getText(R.string.setting_Warming))
                .setPositiveButton((String) this.getText(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                return;
                            }
                        }).create();

        dialog.show(); // Show the Dialog
    }

    // Set the Chart's Parameter
    private XYMultipleSeriesDataset getBarDemoDataset() {

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

        CategorySeries series = new CategorySeries("People No.");
        for (int i = 1; i <= maxpeople; i++)
            series.add(votes[i]);
        dataset.addSeries(series.toXYSeries());
        return dataset;
    }

    public XYMultipleSeriesRenderer getBarDemoRenderer() {

        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        SimpleSeriesRenderer r = new SimpleSeriesRenderer();
        r.setColor(Color.LTGRAY);
        renderer.addSeriesRenderer(r);

        setChartSettings(renderer);
        return renderer;

    }

    private void setChartSettings(XYMultipleSeriesRenderer renderer) {

        renderer.setChartTitle((String) this.getText(R.string.chart_Name));
        renderer.setXTitle((String) this.getText(R.string.x_Axis_Name));
        renderer.setYTitle((String) this.getText(R.string.y_Axis_Name));
        renderer.setXAxisMin(0.5);
        renderer.setXAxisMax(maxpeople + 0.5);
        renderer.setYAxisMin(0);
        renderer.setYAxisMax(YAxisMax);// set to max votes
        renderer.setClickEnabled(true);
        renderer.setBarSpacing(0.1);
        renderer.setInScroll(true);
        renderer.setShowGridY(true);
        renderer.setShowGridX(true);
        renderer.setBackgroundColor(Color.TRANSPARENT);
        renderer.setApplyBackgroundColor(true);
    }
}
