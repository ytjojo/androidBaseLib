package com.kerkr.edu.textView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //删除线
 *
 *Paint.UNDERLINE_TEXT_FLAG 下划线 可以测试其他的自己
 * Created by sreay on 14-9-16.
 */
public class DiscardTextView extends TextView {
	private int offset;
	private int width;
	private int height;
	private Paint paint;

	public DiscardTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public DiscardTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DiscardTextView(Context context) {
		super(context);
		init();
	}

	private void init() {
		setGravity(Gravity.CENTER);
		paint = new Paint();
		paint.setColor(Color.parseColor("#666666"));
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL);
		paint.setStrokeWidth(2);
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		width = getMeasuredWidth();
		height = getMeasuredHeight();
		offset = height / 2;
		canvas.drawLine(0, offset, width, offset, paint);
	}
}
