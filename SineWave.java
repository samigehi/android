// creating sine wave 
/*
 * Created by by Sumeet Gehi 
 * sumitgehi@gmail.com
 * an implementation of wave in android
 */

package com.example.sumitandroid;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
	
	SurfaceView surface;
	SurfaceHolder sHolder;
	private Timer mTimer;
	private MyTimerTask mTimerTask;
	int y_axis[], centerY, oldX, oldY, CurrentX;
	int w,h;
	int speed=1;
	boolean isRunning=false;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		surface = (SurfaceView) this.findViewById(R.id.surface1);
		final LinearLayout ls=(LinearLayout)findViewById(R.id.layout);
		sHolder = surface.getHolder();
		//mTimer = new Timer();
		//mTimerTask = new MyTimerTask();
		surface.setZOrderOnTop(true);
		sHolder.setFormat(PixelFormat.TRANSPARENT);

		final ViewTreeObserver observer = ls.getViewTreeObserver();
		observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				Log.d("oncreate", "ls " + ls.getHeight());
				w=ls.getWidth();
				h = ls.getHeight();
				centerY = h / 2;
				y_axis = new int[w];
				for (int i = 1; i < y_axis.length; i++) {
					y_axis[i - 1] = centerY
							- (int) (100 * Math.sin(i * 2 * Math.PI / 180));
				}
				//mTimer.schedule(mTimerTask, 0, 1);
				Log.d("oncreate", "done "+w +" "+h);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					ls.getViewTreeObserver().removeOnGlobalLayoutListener(this);
					Log.d("oncreate", "remove ");
					
				} else {
					ls.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				}
				// observer.removeGlobalOnLayoutListener(this);
			}
		});
		//Log.d("canvas-oncreate", "cY "+centerY + " yx "+y_axis.length + " sH "+ls.getWidth());
		//init_sine();
		//ls.setOnTouchListener(this);
		final Button b = (Button)findViewById(R.id.button1);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isRunning)
				{
					mTimer.cancel();
					mTimerTask.cancel();
					isRunning = false;
					b.setText("Start!!!");
				}
				else
				{
				mTimerTask = new MyTimerTask();
				mTimer = new Timer();
				mTimer.schedule(mTimerTask, 0, 1);
				isRunning= true;
				b.setText("Stop!!!");
				}

			}
		});
		//mTimer.schedule(mTimerTask, 0, 1);

	}
	
	/*@Override
	public boolean onTouch(View v, MotionEvent event) {
		float x = event.getX(), y = event.getY();
		switch (event.getAction() & MotionEvent.ACTION_MASK) {

		case MotionEvent.ACTION_DOWN:
			Log.d("touch down", "X " + x + " Y " + y);
			mTimer = new Timer();
			mTimer.schedule(mTimerTask, 0, 1);
			break;

		case MotionEvent.ACTION_MOVE:
			//Log.d("touch move", "X " + x + " Y " + y);
			break;

		case MotionEvent.ACTION_UP:
			  mTimer.cancel();
			Log.d("touch up", "X " + x + " Y " + y);
			break;

		default:
			break;
		}
		return true;
	}*/
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		mTimer.cancel();
		//v2.cancel();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mTimer.cancel();
		//v2.cancel();
	}
	
	class MyTimerTask extends TimerTask {
		@Override
		public void run() {
			SimpleDraw(CurrentX);
			CurrentX+=speed;
			
			if (CurrentX == y_axis.length-1) {
				ClearDraw();
				CurrentX = 0;
				oldY = centerY;
			}
		}
	}

	private void SimpleDraw(int length) {
		if (length == 0)
			oldX = 0;
			Canvas canvas = sHolder.lockCanvas(new Rect(oldX, 0, oldX + length,
				getWindowManager().getDefaultDisplay().getHeight()));
		//Canvas canvas = sHolder.lockCanvas(new Rect(oldX, 0, oldX + length,
		//		surface.getHeight()));
	
		//Log.i("Canvas:",
			//	String.valueOf(oldX) + "," + String.valueOf(oldX + length) +" ,l"+length);

		Paint mPaint = new Paint();
		mPaint.setColor(Color.GREEN);
		//mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		//mPaint.setStrokeJoin(Paint.Join.ROUND);
		//mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(2);

		int y = 0;
		for (int i = oldX + 1; i < length; i++) {
			try {
				y = y_axis[i - 1];
				canvas.drawLine(oldX, oldY, i, y, mPaint);
				oldX = i;
				oldY = y;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		sHolder.unlockCanvasAndPost(canvas);
	}

	private void ClearDraw() {
		Canvas canvas = sHolder.lockCanvas(null);
		canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
		//surface.setZOrderOnTop(true);
		//sHolder.setFormat(PixelFormat.TRANSPARENT);
		//canvas.clearRect( 0 , 0 , canvas.getWidth(), canvas.getHeight() );
		sHolder.unlockCanvasAndPost(canvas);
	}
}
