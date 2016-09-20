package com.spiralroam.app.smsvote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ConfigActivity extends ListActivity {
    public static Integer cntPeopleNumber;
    public static Integer maxVotesPerPeople;

    public static String cntPNname = "cntPeopleNumber";
    public static String maxVPname = "maxVotesPerPeople";
    private List<Map<String, Object>> mData;
    MyAdapter adapter;
    SharedPreferences preferrence;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // Create SharedPreferences
        preferrence = getSharedPreferences("SMSVote_setting", 0);
        cntPeopleNumber = (int) preferrence.getLong(cntPNname, 5);
        maxVotesPerPeople = (int) preferrence.getLong(maxVPname, 1);
        // Adapter
        adapter = new MyAdapter(this);
        new ListView(this);
        mData = getData();

        setListAdapter(adapter);

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // Create SharedPreferences
        SharedPreferences preferrence = getSharedPreferences("SMSVote_setting",
                0);

        // Test the SharedPreferrences function
        SMSVoteActivity.maxpeople = (int) preferrence.getLong(cntPNname, 5);
        SMSVoteActivity.maxvotes = (int) preferrence.getLong(maxVPname, 1);

        // Create the vote array from the maxpeople parameter
        SMSVoteActivity.votes = new Integer[SMSVoteActivity.maxpeople + 1];
        SMSVoteActivity.errors = new String[SMSVoteActivity.maxpeople + 1];
        for (int i = 0; i <= SMSVoteActivity.maxpeople; i++) {
            SMSVoteActivity.votes[i] = 0;
            SMSVoteActivity.errors[i] = null;
        }
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 0)
            return new settingEditDialogCnt(ConfigActivity.this);
        else if (id == 1)
            return new settingEditDialogMax(ConfigActivity.this);
        return super.onCreateDialog(id);
    }

    private List<Map<String, Object>> getData() {
        // TODO Auto-generated method stub
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        // set the total vote people
        map = new HashMap<String, Object>();
        map.put("settingName",
                ConfigActivity.this.getText(R.string.cnt_people_number));
        map.put("settingInfo",
                ConfigActivity.this.getText(R.string.cnt_people_number_info));
        map.put("settingValue", ConfigActivity.this.getText(R.string.setting)
                + "\t" + cntPeopleNumber.toString());
        list.add(map);
        // set the max votes if each person
        map = new HashMap<String, Object>();
        map.put("settingName",
                ConfigActivity.this.getText(R.string.votes_per_person));
        map.put("settingInfo",
                ConfigActivity.this.getText(R.string.votes_per_person_info));
        map.put("settingValue", ConfigActivity.this.getText(R.string.setting)
                + "\t" + maxVotesPerPeople.toString());
        list.add(map);
        // set the user mannual
        map = new HashMap<String, Object>();
        map.put("settingName",
                ConfigActivity.this.getText(R.string.manual_title));
        map.put("settingInfo",
                ConfigActivity.this.getText(R.string.manual_info));
        map.put("settingValue", null);
        list.add(map);
        // get pro
        map = new HashMap<String, Object>();
        map.put("settingName",
                ConfigActivity.this.getText(R.string.get_pro));
        map.put("settingInfo",
                ConfigActivity.this.getText(R.string.pro_info));
        map.put("settingValue", null);
        list.add(map);

        return list;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        //	Log.v("MyListView4-click", (String) mData.get(position).get("title"));
    }

    public final class ViewHolder {
        public TextView settingName;
        public TextView settingInfo;
        public Button SettingValue;
    }

    // Define the Dialog windows
    public class settingEditDialogCnt extends Dialog {

        public settingEditDialogCnt(Context context) {
            super(context);
            this.setContentView(R.layout.activity_setting);
            this.setTitle(R.string.setting);

            final EditText textEdit = (EditText) findViewById(R.id.reason_edit);
            Button bt_ensure = (Button) settingEditDialogCnt.this
                    .findViewById(R.id.setting_yes);
            Button bt_cancel = (Button) settingEditDialogCnt.this
                    .findViewById(R.id.setting_cancel);
            textEdit.requestFocus();
            textEdit.setText("");
            bt_cancel.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    settingEditDialogCnt.this.dismiss();
                }

            });
            bt_ensure.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    String getString;
                    getString = textEdit.getText().toString();
                    if (getString.length() != 0) {
                        cntPeopleNumber = Integer.valueOf(getString);
                        preferrence.edit().putLong(cntPNname, cntPeopleNumber)
                                .commit();
                        Intent intent = new Intent(ConfigActivity.this,
                                ConfigActivity.class);
                        startActivity(intent);
                        ConfigActivity.this.finish();
                    }
                    settingEditDialogCnt.this.dismiss();
                }
            });
        }

    }

    public class settingEditDialogMax extends Dialog {

        public settingEditDialogMax(Context context) {
            super(context);
            this.setContentView(R.layout.activity_setting);
            this.setTitle(R.string.setting);

            final EditText textEdit = (EditText) findViewById(R.id.reason_edit);
            Button bt_ensure = (Button) settingEditDialogMax.this
                    .findViewById(R.id.setting_yes);
            Button bt_cancel = (Button) settingEditDialogMax.this
                    .findViewById(R.id.setting_cancel);
            textEdit.requestFocus();
            textEdit.setText("");
            bt_cancel.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    settingEditDialogMax.this.dismiss();
                }

            });
            bt_ensure.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    String getString;
                    getString = textEdit.getText().toString();
                    if (getString.length() != 0) {
                        maxVotesPerPeople = Integer.valueOf(getString);
                        preferrence.edit()
                                .putLong(maxVPname, maxVotesPerPeople).commit();
                        Intent intent = new Intent(ConfigActivity.this,
                                ConfigActivity.class);
                        startActivity(intent);
                        ConfigActivity.this.finish();
                    }
                    settingEditDialogMax.this.dismiss();
                }
            });
        }

    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return mData.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();

                convertView = mInflater.inflate(R.layout.listunit, null);
                holder.settingName = (TextView) convertView
                        .findViewById(R.id.settingName);
                holder.settingInfo = (TextView) convertView
                        .findViewById(R.id.settingInfo);
                holder.SettingValue = (Button) convertView
                        .findViewById(R.id.settingvalue);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // Set up text to those Views in the Layout of List units
            holder.settingName.setText((String) mData.get(position).get(
                    "settingName"));
            holder.settingInfo.setText((String) mData.get(position).get(
                    "settingInfo"));
            holder.SettingValue.setText((String) mData.get(position).get(
                    "settingValue"));
            //remove button of 3 and 2
            if (position == 2 || position == 3)
                holder.SettingValue.setVisibility(View.GONE);


            // Attach OnClickListener to it
            holder.SettingValue.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (position != 2 && position != 3)
                        showDialog(position);
                    // showInfo();
                }
            });

            return convertView;
        }
    }

}
