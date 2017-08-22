package com.kuro.emojichatkeyboard;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import io.github.rockerhieu.emojicon.EmojiconEditText;

/**
 * Created by nnv on 2017/8/22.
 */

public class ChatToolLayout extends RelativeLayout implements View.OnClickListener,
        SoftKeyboardStateHelper.SoftKeyboardStateListener {

    public static final int LAYOUT_TYPE_HIDE = 0;
    public static final int LAYOUT_TYPE_FACE = 1;
    public static final int LAYOUT_TYPE_MORE = 2;

    private EmojiconEditText etMessage;
    private CheckBox cbFace, cbMore;
    private Button btnSend;
    private ViewPager vpTool;

    private ChatToolAdapter adapter;

    private int layoutType = LAYOUT_TYPE_HIDE;
    private SoftKeyboardStateHelper keyboardStateHelper;
    private int keyboardHeight = -1;

    public ChatToolLayout(Context context) {
        this(context, null);
    }

    public ChatToolLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatToolLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        keyboardHeight = SPUtil.getInteger(getContext(), SPUtil.KEYBOARD_HEIGHT);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_chat_keyboard, null, false);
        addView(view);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        keyboardStateHelper = new SoftKeyboardStateHelper(((Activity) getContext()).getWindow().getDecorView());
        keyboardStateHelper.addSoftKeyboardStateListener(this);
        initWidget();
    }

    private void initWidget() {
        etMessage = findViewById(R.id.et_message);
        cbFace = findViewById(R.id.cb_face);
        cbMore = findViewById(R.id.cb_more);
        btnSend = findViewById(R.id.btn_send);
        vpTool = findViewById(R.id.vp_tool);
        etMessage.setOnClickListener(this);
        cbFace.setOnClickListener(this);
        cbMore.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        etMessage.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnSend.setVisibility(TextUtils.isEmpty(s) ? GONE : VISIBLE);
                cbMore.setVisibility(TextUtils.isEmpty(s) ? VISIBLE : GONE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_message:
                hideLayout();
                break;
            case R.id.cb_face:
                functionClick(LAYOUT_TYPE_FACE);
                break;
            case R.id.cb_more:
                functionClick(LAYOUT_TYPE_MORE);
                break;
            case R.id.btn_send:
                break;
        }
    }

    private void functionClick(int type) {
        if (isShow() && type == layoutType) {
            hideLayout();
            showKeyboard(getContext());
        } else {
            changeLayout(type);
            showLayout();
            cbFace.setChecked(layoutType == LAYOUT_TYPE_FACE);
            cbMore.setChecked(layoutType == LAYOUT_TYPE_MORE);
        }
    }

    private void showKeyboard(Context context) {
        Activity activity = (Activity) context;
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInputFromInputMethod(activity.getCurrentFocus()
                    .getWindowToken(), 0);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void hideKeyboard(Context context) {
        Activity activity = (Activity) context;
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive() && activity.getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                        .getWindowToken(), 0);
            }
        }
    }

    private void changeLayout(int type) {
        layoutType = type;
        AppCompatActivity activity = (AppCompatActivity) getContext();
        adapter = new ChatToolAdapter(activity.getSupportFragmentManager(), LAYOUT_TYPE_FACE);
        vpTool.setAdapter(adapter);
    }

    private void showLayout() {
        hideKeyboard(getContext());
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (vpTool.getHeight() != keyboardHeight) {
                    ViewGroup.LayoutParams layoutParams = vpTool.getLayoutParams();
                    layoutParams.height = keyboardHeight;
                    vpTool.setLayoutParams(layoutParams);
                }
                vpTool.setVisibility(View.VISIBLE);
            }
        }, 50);
    }

    private void hideLayout() {
        vpTool.setVisibility(View.GONE);
        if (cbFace.isChecked()) {
            cbFace.setChecked(false);
        }
        if (cbMore.isChecked()) {
            cbMore.setChecked(false);
        }
    }

    public boolean isShow() {
        return vpTool.getVisibility() == VISIBLE;
    }

    @Override
    public void onSoftKeyboardOpened(int keyboardHeightInPx) {
        SPUtil.putInteger(getContext(), SPUtil.KEYBOARD_HEIGHT, keyboardHeightInPx);
        hideLayout();
    }

    @Override
    public void onSoftKeyboardClosed() {
    }

    public EmojiconEditText getEtMessage() {
        return etMessage;
    }
}
