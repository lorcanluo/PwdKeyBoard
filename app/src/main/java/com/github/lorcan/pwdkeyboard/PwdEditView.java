package com.github.lorcan.pwdkeyboard;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.view.View.OnTouchListener;

public class PwdEditView extends EditText implements OnTouchListener {
	private Activity activity;
	private KeyboardUtil keyboadrUtil;

	public PwdEditView(Context context, AttributeSet attrs) {
		super(context, attrs);
		activity = (Activity) context;
		hideSystemKeyboard();
		setOnTouchListener(this);

		//forbid copy fun.
		setLongClickable(false);
	}

	public void initKeyboardView(KeyboardUtil keyUtil) {
		keyboadrUtil = keyUtil;
	}

	// hide system keyboard
	private void hideSystemKeyboard() {
		if (android.os.Build.VERSION.SDK_INT <= 10) {
			setInputType(InputType.TYPE_NULL);
		} else {
			activity.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			try {
				Class<EditText> cls = EditText.class;
				Method setShowSoftInputOnFocus;
				setShowSoftInputOnFocus = cls.getMethod(
						"setShowSoftInputOnFocus", boolean.class);
				setShowSoftInputOnFocus.setAccessible(true);
				setShowSoftInputOnFocus.invoke(this, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (keyboadrUtil != null) {
			keyboadrUtil.showKeyboard();
		}
		return false;
	}

}
