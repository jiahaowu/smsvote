package com.spiralroam.app.smsvote;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;


public class SlipButton extends View implements OnTouchListener {
	private String strName;
	private boolean enabled = true;
	public boolean flag = false;// set the initial state
	public boolean NowChoose = false;
	// record the present switch is on or off[true for on]
	private boolean OnSlip = false;
	// record the user wither slip
	public float DownX = 0f, NowX = 0f;
	// when press the axis of X,when done the axis of X,if NowX>100,set the On
	// Background.else off Background
	private Rect Btn_On, Btn_Off;
	// the on state and the off state the cursor's Rect

	private boolean isChgLsnOn = false;
	private OnChangedListener ChgLsn;
	private Bitmap bg_on, bg_off, slip_btn;

	public SlipButton(Context context) {
		super(context);
		init();
	}

	public SlipButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public void setChecked(boolean fl) {
		if (fl) {
			flag = true;
			NowChoose = true;
			NowX = 80;
		} else {
			flag = false;
			NowChoose = false;
			NowX = 0;
		}
	}

	public void setEnabled(boolean b) {
		if (b) {
			enabled = true;
		} else {
			enabled = false;
		}
	}

	private void init() {// Initial
		// Load the Picture resource
		bg_on = BitmapFactory.decodeResource(getResources(), R.drawable.on_btn);
		bg_off = BitmapFactory.decodeResource(getResources(),
				R.drawable.off_btn);
		slip_btn = BitmapFactory.decodeResource(getResources(),
				R.drawable.white_btn);
		// get the Rect data that we need
		Btn_On = new Rect(0, 0, slip_btn.getWidth(), slip_btn.getHeight());
		Btn_Off = new Rect(bg_off.getWidth() - slip_btn.getWidth(), 0,
				bg_off.getWidth(), slip_btn.getHeight());
		setOnTouchListener(this);// Set up Listener or Override the OnTouchEvent
	}

	@Override
	public boolean isInEditMode() {
		// TODO Auto-generated method stub
		return super.isInEditMode();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// Function to draw
		super.onDraw(canvas);
		Matrix matrix = new Matrix();
		Paint paint = new Paint();
		float x;
		{
			if (flag) {
				NowX = 80;
				flag = false;
			}// bg_on.getWidth()=71
			if (NowX < (bg_on.getWidth() / 2))
				// when slip to the Lead half and the tail half we have
				// different background and here we define the judgment
				canvas.drawBitmap(bg_off, matrix, paint);
			// draw the off background
			else
				canvas.drawBitmap(bg_on, matrix, paint);
			// draw the on background

			if (OnSlip)// judge whether in the slip state
			{
				if (NowX >= bg_on.getWidth())
					// Important,judge the cursor if in the range of the
					// bar,don't let the cursor out of the range
					x = bg_on.getWidth() - slip_btn.getWidth() / 2;
				// minus half of the length of the bar
				else
					x = NowX - slip_btn.getWidth() / 2;
			} else {// not Slip state
				if (NowChoose)// set the cursor position according to the switch
								// state
					x = Btn_Off.left;
				else
					x = Btn_On.left;
			}
			if (x < 0)// judge the Exception of cursor's position
				x = 0;
			else if (x > bg_on.getWidth() - slip_btn.getWidth())
				x = bg_on.getWidth() - slip_btn.getWidth();
			canvas.drawBitmap(slip_btn, x, 0, paint);
			// draw the cursor
		}
	}

	public boolean onTouch(View v, MotionEvent event) {
		if (!enabled) {
			return false;
		}
		switch (event.getAction())// execute the code according to user's action
		{
		case MotionEvent.ACTION_MOVE:// Slip
			NowX = event.getX();
			break;
		case MotionEvent.ACTION_DOWN:// PressDown
			if (event.getX() > bg_on.getWidth()
					|| event.getY() > bg_on.getHeight())
				return false;
			OnSlip = true;
			DownX = event.getX();
			NowX = DownX;
			break;
		case MotionEvent.ACTION_UP:// Release
			OnSlip = false;
			boolean LastChoose = NowChoose;
			if (event.getX() >= (bg_on.getWidth() / 2))
				NowChoose = true;
			else
				NowChoose = false;
			if (isChgLsnOn && (LastChoose != NowChoose))
				// if set up the Listener,use the other method
				ChgLsn.OnChanged(strName, NowChoose);
			break;
		default:

		}
		invalidate();// painting widget
		return true;
	}

	public void SetOnChangedListener(String name, OnChangedListener l) {
		// set up the Listener used when the state changed
		strName = name;
		isChgLsnOn = true;
		ChgLsn = l;
	}
}