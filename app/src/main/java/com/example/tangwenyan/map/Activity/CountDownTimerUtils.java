package com.example.tangwenyan.map.Activity;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.example.tangwenyan.map.R;

public class CountDownTimerUtils extends CountDownTimer {
    private TextView mTextView;

    /**
     * @param textView     The TextView
     *
     *
     * @param millisInFuture  The number of millis in the future from the call
     *             to {@link #start()} until the countdown is done and {@link #onFinish()}
     *             is called.
     * @param countDownInterval The interval along the way to receiver
     *             {@link #onTick(long)} callbacks.
     */
    public CountDownTimerUtils(TextView textView, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mTextView = textView;
    }

    /**
     * 倒计时期间会调用
     * @param millisUntilFinished
     */
    @Override
    public void onTick(long millisUntilFinished) {
        mTextView.setClickable(false); //设置不可点击
        mTextView.setText(millisUntilFinished / 1000 + "s"); //设置倒计时时间
        mTextView.setBackgroundResource(R.drawable.shape_verify_btn_press); //设置按钮为灰色，这时是不能点击的

        SpannableString spannableString = new SpannableString(mTextView.getText().toString()); //获取按钮上的文字
        ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
        /**
         * public void setSpan(Object what, int start, int end, int flags) {
         * 主要是start跟end，start是起始位置,无论中英文，都算一个。
         * 从0开始计算起。end是结束位置，所以处理的文字，包含开始位置，但不包含结束位置。
         */
        spannableString.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//将倒计时的时间设置为红色
        mTextView.setText(spannableString);
    }

    /**
     * 倒计时完成后调用
     */
    @Override
    public void onFinish() {
        mTextView.setText("重新获取验证码");
        mTextView.setClickable(true);//重新获得点击
        mTextView.setBackgroundResource(R.drawable.shape_verify_btn_normal); //还原背景色
    }
}

