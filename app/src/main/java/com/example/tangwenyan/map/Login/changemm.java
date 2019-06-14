package com.example.tangwenyan.map.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tangwenyan.map.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

//修改密码

public class changemm extends AppCompatActivity implements View.OnClickListener {

    private ImageView button; //返回按钮
    private TextView pwd1;
    private TextView pwd2;
    private TextView ok;  //确认修改按钮
    private TextInputLayout textInputLayout1;
    private TextInputLayout textInputLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changemm);
        final User user = BmobUser.getCurrentUser(User.class);
        init();
        pwd2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!RegexUtils.isPasswd(s)) {
                    textInputLayout2.setError("只能输入6-20个字母、数字、下划线 ");
                    textInputLayout2.setErrorEnabled(true);
                } else {
                    textInputLayout2.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void init() {
        textInputLayout1 = (TextInputLayout) findViewById(R.id.edit1);
        textInputLayout2 = (TextInputLayout) findViewById(R.id.edit2);
        button = (ImageView) findViewById(R.id.iv_back);
        ok = (TextView) findViewById(R.id.tv_next);
        button.setOnClickListener(this);
        ok.setOnClickListener(this);
        pwd1 = (TextView) findViewById(R.id.user_name);
        pwd2 = (TextView) findViewById(R.id.edittxt_pwd);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                Intent intent1 = new Intent(changemm.this, Person_center.class);
                startActivity(intent1);
                break;
            case R.id.tv_next:
                final String Pwd1 = pwd1.getText().toString();
                final String Pwd2 = pwd2.getText().toString();
                if(!TextUtils.isEmpty(Pwd1) && !TextUtils.isEmpty(Pwd2)) {
                    BmobUser.updateCurrentUserPassword(Pwd1, Pwd2, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null) {
                                Toast.makeText(changemm.this,"修改成功",Toast.LENGTH_SHORT).show();
                                Intent intent2 = new Intent(changemm.this, Person_center.class);
                                startActivity(intent2);
                            } else {
                                textInputLayout1.setError("修改失败"+e.getMessage());
                                textInputLayout1.setErrorEnabled(true);
                                //Toast.makeText(changemm.this, "修改失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                   Toast.makeText(changemm.this,"旧密码或新密码为空",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
