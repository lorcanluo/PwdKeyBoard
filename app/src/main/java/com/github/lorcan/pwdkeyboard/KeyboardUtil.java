package com.github.lorcan.pwdkeyboard;

import java.util.List;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

public class KeyboardUtil {
	private Context ctx;
	private KeyboardView keyboardView;
	private Keyboard keyEnglish;// 字母键盘
	private Keyboard keySymbol;// 数字键盘1
	private Keyboard keySymbolShift;// 数字键盘2
	public boolean isnun = false;// 是否数据键盘
	public boolean isupper = false;// 是否大写

	private EditText ed;

	public KeyboardUtil(KeyboardView keyView, Context ctx, EditText edit) {
		this.ctx = ctx;
		this.ed = edit;
		keyEnglish = new Keyboard(ctx, R.xml.english);
		keySymbol = new Keyboard(ctx, R.xml.symbols);
		keySymbolShift = new Keyboard(ctx, R.xml.symbols_shift);
		keyboardView = keyView;
		keyboardView.setKeyboard(keyEnglish);
		keyboardView.setEnabled(true);
		keyboardView.setPreviewEnabled(false);
		keyboardView.setOnKeyboardActionListener(listener);
	}

	private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
		@Override
		public void swipeUp() {
		}

		@Override
		public void swipeRight() {
		}

		@Override
		public void swipeLeft() {
		}

		@Override
		public void swipeDown() {
		}

		@Override
		public void onText(CharSequence text) {
		}

		@Override
		public void onRelease(int primaryCode) {
			
		}

		@Override
		public void onPress(int primaryCode) {
		}

		@Override
		public void onKey(int primaryCode, int[] keyCodes) {
			Editable editable = ed.getText();
			int start = ed.getSelectionStart();
			if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
				hideKeyboard();
			} else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 刪除
				if (editable != null && editable.length() > 0) {
					if (start > 0) {
						editable.delete(start - 1, start);
					}
				}

			} else if (primaryCode == Keyboard.KEYCODE_SHIFT) {// 大小写切
				changeKey();
				keyboardView.setKeyboard(keyEnglish);
			} else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {// 数字键盘切换
				if (isnun) {
					isnun = false;
					keyboardView.setKeyboard(keyEnglish);
				} else {
					isnun = true;
					keyboardView.setKeyboard(keySymbol);
				}
			} else if (primaryCode == 57419) { // go left
				if (start > 0) {
					ed.setSelection(start - 1);
				}
			} else if (primaryCode == 57421) { // go right
				if (start < ed.length()) {
					ed.setSelection(start + 1);
				}
			} else if (primaryCode == -102) {
				keyboardView.setKeyboard(keySymbolShift);
			} else if (primaryCode == -202) {
				keyboardView.setKeyboard(keySymbol);
			} else {
				editable.insert(start, Character.toString((char) primaryCode));
			}
		}
	};

	/**
	 * 键盘大小写切
	 */
	private void changeKey() {
		List<Key> keylist = keyEnglish.getKeys();
		if (isupper) {// 大写切换小写
			isupper = false;
			for (Key key : keylist) {
				if (key.label != null && isword(key.label.toString())) {
					key.label = key.label.toString().toLowerCase();
					key.codes[0] = key.codes[0] + 32;
				}
			}
		} else {// 小写切换大写
			isupper = true;
			for (Key key : keylist) {
				if (key.label != null && isword(key.label.toString())) {
					key.label = key.label.toString().toUpperCase();
					key.codes[0] = key.codes[0] - 32;
				}
			}
		}
	}

	public void showKeyboard() {
		int visibility = keyboardView.getVisibility();
		if (visibility == View.GONE || visibility == View.INVISIBLE) {
			keyboardView.setVisibility(View.VISIBLE);
		}
	}

	public void hideKeyboard() {
		int visibility = keyboardView.getVisibility();
		if (visibility == View.VISIBLE) {
			keyboardView.setVisibility(View.INVISIBLE);
		}
	}

	private boolean isword(String str) {
		String wordstr = "abcdefghijklmnopqrstuvwxyz";
		if (wordstr.indexOf(str.toLowerCase()) > -1) {
			return true;
		}
		return false;
	}

}
