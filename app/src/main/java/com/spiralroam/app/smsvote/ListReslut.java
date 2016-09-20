package com.spiralroam.app.smsvote;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListReslut extends Activity {
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

		Pair[] list = new Pair[SMSVoteActivity.maxpeople + 1];

		// Integer[] sortArr = new Integer[SMSVoteActivity.maxpeople + 1];
		for (int i = 0; i <= SMSVoteActivity.maxpeople; i++)
			list[i] = new Pair(SMSVoteActivity.votes[i], i);

		// Cmp
		bubblesort(list);
		// copy to data
		for (Integer i = 1; i <= SMSVoteActivity.maxpeople; i++) {
			data.add(ListReslut.this.getText(R.string.list_words_head)
					+ i.toString() + list[i].listText);
		}
		// return data to the ListView
		return data;
	}

	class Pair {

		private Integer votesNum;
		private Integer indexNum;
		public String listText;

		public Pair(Integer votesNumber, Integer indexNumber) {
			this.votesNum = votesNumber;
			this.indexNum = indexNumber;
			this.listText = ListReslut.this.getText(R.string.list_words_1)
					+ indexNum.toString()
					+ ListReslut.this.getText(R.string.list_words_2)
					+ votesNum.toString()
					+ ListReslut.this.getText(R.string.vote_unit);
		}

	}

	static void bubblesort(Pair[] a) {

		Pair temp;

		for (int i = 1; i < a.length; ++i) {
			for (int j = a.length - 1; j > i; --j) {
				if (a[j].votesNum > a[j - 1].votesNum) {
					temp = a[j];
					a[j] = a[j - 1];
					a[j - 1] = temp;

				}

			}

		}

	}
}