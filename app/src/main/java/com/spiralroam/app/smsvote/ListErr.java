package com.spiralroam.app.smsvote;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListErr extends Activity {
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// listView = (ListView) findViewById(R.layout.config_activity);
		listView = new ListView(this);
		listView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1, getData()));
		setContentView(listView);
	}

	private List<String> getData() {
		// TODO Auto-generated method stub
		List<String> data = new ArrayList<String>();
		// copy to data
		for (Integer i = 1; i <= SMSVoteActivity.errCounts; i++)
			data.add(SMSVoteActivity.errors[i]);		

		// return data to the ListView
		return data;
	}

	class error {

	}
}