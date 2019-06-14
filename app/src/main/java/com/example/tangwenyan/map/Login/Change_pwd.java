package com.example.tangwenyan.map.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tangwenyan.map.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

//找回密码

public class Change_pwd extends AppCompatActivity implements View.OnClickListener{
    private EditText email; //邮箱地址
    private TextView finish; //完成按钮
    private ImageView back; //返回上一步
    private TextInputLayout textInputLayout; //判断邮箱是否格式正确

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pwd1);
        initialize();
        initView();
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!RegexUtils.isEmail(s)){
                    textInputLayout.setError("邮箱格式错误");
                    textInputLayout.setErrorEnabled(true);
                }else {
                    textInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initView() { }

    private void initialize() {
        textInputLayout = (TextInputLayout) findViewById(R.id.edit2);
        back = ((ImageView) findViewById(R.id.iv_back));
        back.setOnClickListener(this);
        finish = ((TextView) findViewById(R.id.tv_next));
        finish.setOnClickListener(this);
        email = ((EditText) findViewById(R.id.email));
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.iv_back:
                Intent intent = new Intent(Change_pwd.this,Login1.class);
                startActivity(intent);
                break;
            case R.id.tv_next:
                final String Email = email.getText().toString();
                if(!TextUtils.isEmpty(Email)) {
                    BmobUser.resetPasswordByEmail(Email, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null) {
                                //textInputLayout.setError("重置密码请求成功，请到"+Email+"邮箱进行密码重置操作");
                                //textInputLayout.setErrorEnabled(true);
                                Toast.makeText(Change_pwd.this,"重置密码请求成功，请到"+Email+"邮箱进行密码重置操作",Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(Change_pwd.this, Login1.class);
                                startActivity(intent1);
                            } else {
                                textInputLayout.setError("发送邮件失败"+e.getMessage());
                                textInputLayout.setErrorEnabled(true);
                                Log.e("BMOB",e.toString());
                                //Toast.makeText(Change_pwd.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    //Toast.makeText(Change_pwd.this,"输入邮箱为空",Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}