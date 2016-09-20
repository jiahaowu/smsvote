package com.spiralroam.app.smsvote;

import java.sql.Date;
import java.text.SimpleDateFormat;

import com.spiralroam.app.smsvote.SMSVoteActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSBroadcastReceiver extends BroadcastReceiver {
	public static String ACTION = "android.provider.Telephony.SMS_RECEIVED";
	public static String dateFormat = "yyyy-MM-dd HH:mm:ss";
	public static boolean func = false;// for control the Listener's behavior
	public static boolean stopBroadcast = false;
	// for control the Broadcast system
	public static boolean anomyous = true;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (func) {
			if (intent.getAction().equals(ACTION)) {
				SMSVoteActivity.counts++;
				SMSVoteActivity.countNumberText
						.setText((CharSequence) SMSVoteActivity.counts
								.toString());

				StringBuffer SMSAddress = new StringBuffer();
				StringBuffer SMSContent = new StringBuffer();
				StringBuffer SMSTime = new StringBuffer();

				Bundle bundle = intent.getExtras();
				if (bundle != null) {
					Object[] pdusObjects = (Object[]) bundle.get("pdus");
					SmsMessage[] messages = new SmsMessage[pdusObjects.length];

					for (int i = 0; i < pdusObjects.length; i++) {
						messages[i] = SmsMessage
								.createFromPdu((byte[]) pdusObjects[i]);
					}

					for (SmsMessage message : messages) {
						SMSAddress.append(message
								.getDisplayOriginatingAddress());
						SMSContent.append(message.getDisplayMessageBody());
						Date date = new Date(message.getTimestampMillis());
						SimpleDateFormat format = new SimpleDateFormat(
								dateFormat);
						SMSTime.append(format.format(date));
					}
					// Toast for test
					Toast toast = Toast.makeText(context, "The SMS Content:"
							+ messages[0].getMessageBody(), Toast.LENGTH_LONG);
					toast.show();
					// update Votes informations
					String contentsStr = messages[0].getMessageBody();
					//String smsAddresse = messages[0].getOriginatingAddress();
					String smsAddresse = messages[0].getOriginatingAddress();
					// String smsSender = messages[0]
					// .getDisplayOriginatingAddress();

					// filter
					int pos = 0;
					boolean flag = true;
					Integer cnt = SMSVoteActivity.maxvotes + 1;
					int number = 0;
					// counting the votes of the election
					// SMSVoteActivity.errCounts used to counting the errors in
					// these votes
					while (pos < contentsStr.length() && cnt > 1) {
						if (contentsStr.charAt(pos) > '9'
								|| contentsStr.charAt(pos) < '0') {
							if (number <= SMSVoteActivity.maxpeople) {
								SMSVoteActivity.votes[number]++;

								if (SMSVoteActivity.YAxisMax <= SMSVoteActivity.votes[number])
									SMSVoteActivity.YAxisMax = SMSVoteActivity.votes[number] + 3;
							}
							if (number != 0
									&& number <= SMSVoteActivity.maxpeople) {
								flag = false;
								cnt--;
							}
							number = 0;

						} else {
							number *= 10;
							number += contentsStr.charAt(pos) - '0';
						}
						pos++;
					}
					if (number <= SMSVoteActivity.maxpeople) {
						SMSVoteActivity.votes[number]++;

						if (SMSVoteActivity.YAxisMax <= SMSVoteActivity.votes[number])
							SMSVoteActivity.YAxisMax = SMSVoteActivity.votes[number] + 3;
					}
					if (number != 0 && number <= SMSVoteActivity.maxpeople) {
						cnt--;
						flag = false;
					}

					// if there is none people got the vote,this vote We regard
					// it as an error votes to record later
					if (flag) {
						SMSVoteActivity.errCounts++;
						// change the TextView in the main Activity
						SMSVoteActivity.errorCntText
								.setText(SMSVoteActivity.errCounts.toString());
						// Record the error detail
						//Resources.getSystem().getText(R.string.errName).toString()
						SMSVoteActivity.errors[SMSVoteActivity.errCounts] = "From:"
								+ smsAddresse
								+ "\n"
								+ "Detail:" + contentsStr;
					}
					// if error don't stop the broadcast
					// Stop the Broadcast outlet
					if (stopBroadcast && flag == false) {
						this.abortBroadcast();
					}
					// update anonymous informations
					if (anomyous == false) {
						recordIt();
					}
				}
			}

		}
	}

	private void recordIt() {
		// TODO record this vote
		return;
	}
}
