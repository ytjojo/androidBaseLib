package com.kerkr.edu.editView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.text.Selection;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MotionEvent;
import android.widget.EditText;


public class CopyableEditText extends EditText {

	private static final int WIDTH = 0;
	private static final int HEIGHT = 0;

	@Override
	public void setCursorVisible(boolean visible) {
		super.setCursorVisible(visible);
	}

	public CopyableEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private int off; // �ַ��ƫ��ֵ

	public CopyableEditText(Context context, Activity activity) {
		super(context);
		initialize();
	}

	@Override
	public void setHighlightColor(int color) {
		super.setHighlightColor(color);
	}

	private void initialize() {
		// setGravity(Gravity.TOP);
		// setBackgroundColor(Color.GRAY);
	}

	@Override
	protected void onCreateContextMenu(ContextMenu menu) {
	}

	@Override
	public boolean getDefaultEditable() {
		return false;// �Ƿ�ɱ༭
	}

	Layout layout;
	int line = 0;
	int curOff;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		layout = getLayout();
		switch (action) {
		case MotionEvent.ACTION_DOWN:// ����ʱ
			setCursorVisible(true);
			Log.d("debug", "MyEditText: getRawX()" + event.getRawX()
					+ "getRawY()" + event.getRawY() + "getX()" + event.getX()
					+ "getY()" + event.getX());
			line = layout.getLineForVertical(getScrollY() + (int) event.getY());// �õ���ֱ�����ϵ�����ֵ�������Ǵ�������Y���ϵ�ƫ����
			off = layout.getOffsetForHorizontal(line, (int) event.getX());// �õ�ĳһ��ˮƽ�����ϵ�ƫ����������ֱ��Ǹ�������ֵ�ʹ������ڸ���X����(��)
			// ��ƫ��������ֵ���ݸ����ϵ����ֵĶ��ٶ�仯�������Ǻ����ϵ����ش�С��
			// event.getX()��X���ϵĴ�����λ�ã� event.getY()��Y���ϵĴ�����λ��
			Log.d("debug", "Down : getX is " + event.getX() + " | getY is "
					+ event.getY() + "\n | line is " + line + " | off is "
					+ off);
			// Selection.setSelection(getEditableText(), off);
			Log.d("debug",
					"start:" + Selection.getSelectionStart(getEditableText())
							+ "end:"
							+ Selection.getSelectionEnd(getEditableText()));

			startLeft = (int) (event.getX() - WIDTH / 2);
			startRight = (int) (event.getX() + WIDTH / 2);
			startTop = (int) (event.getY() - HEIGHT / 2);
			startBottom = (int) (event.getY() + HEIGHT / 2);

			break;
		case MotionEvent.ACTION_MOVE:// �ƶ�ʱ
			line = layout.getLineForVertical(getScrollY() + (int) event.getY());// �õ���ֱ�����ϵ�����ֵ�������Ǵ�������Y���ϵ�ƫ����
			curOff = layout.getOffsetForHorizontal(line, (int) event.getX());// �õ�ĳһ��ˮƽ�����ϵ�ƫ����������ֱ��Ǹ�������ֵ�ʹ������ڸ���X����(��)
			// ��ƫ��������ֵ���ݸ����ϵ����ֵĶ��ٶ�仯�������Ǻ����ϵ����ش�С��
			Log.d("debug", "Up : getX is " + event.getX() + " | getY is "
					+ event.getY() + "\n | line is " + line + " | curOff is "
					+ curOff);
			Selection.setSelection(getEditableText(), off, curOff);

			endLeft = (int) (event.getX() - WIDTH / 2);
			endRight = (int) (event.getX() + WIDTH / 2);
			endTop = (int) (event.getY() - HEIGHT / 2);
			endBottom = (int) (event.getY() + HEIGHT / 2);

			// ���˼·�����ƶ�ѡ�����з����㲥�����ݹ�ȥ����λ�ò���
			Intent intent = new Intent(CHOOSE_ACTION);

			intent.putExtra("startLeft", startLeft);
			intent.putExtra("startRight", startRight);
			intent.putExtra("startTop", startTop);
			intent.putExtra("startBottom", startBottom);

			intent.putExtra("endLeft", endLeft);
			intent.putExtra("endRight", endRight);
			intent.putExtra("endTop", endTop);
			intent.putExtra("endBottom", endBottom);

			intent.putExtra("off", off);
			intent.putExtra("curOff", curOff);

			getContext().getApplicationContext().sendBroadcast(intent);

			break;
		case MotionEvent.ACTION_UP:// �ɿ�ʱ
			setCursorVisible(false);
			Log.d("debug",
					"start:" + Selection.getSelectionStart(getEditableText())
							+ "end:"
							+ Selection.getSelectionEnd(getEditableText()));
			break;
		}
		return true;
	}

	public final static String CHOOSE_ACTION = "com.test.COPYTEXT";

	private int startLeft = 0;
	private int startRight = 0;
	private int startTop = 0;
	private int startBottom = 0;

	private int endLeft = 0;
	private int endRight = 0;
	private int endTop = 0;
	private int endBottom = 0;

}
