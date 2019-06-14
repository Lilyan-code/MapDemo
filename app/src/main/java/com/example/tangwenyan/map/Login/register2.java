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

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class register2 extends AppCompatActivity implements View.OnClickListener{

    private ImageView iv_back;  //左上角返回按钮
    private TextView tv_next;  //文本框下方按钮
    private EditText user_name; //用户名
    private EditText password;  //密码
    private EditText password1; //确认密码
    private TextInputLayout textInputLayout3; //确认密码
    private TextInputLayout textInputLayout2;  //密码判断
    private TextInputLayout textInputLayout1; //用户名判断
    private Integer flag = 1;
    private Integer flag1 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_second);
        initialize();
        initView();
        //用户名输入动态判断
        user_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!RegexUtils.isUsername(s)) {
                    flag = 0;
                    textInputLayout1.setError("取值范围为a-z,A-Z,0-9, _ ,汉字，不能以_结尾,用户名必须是6-20位");
                    textInputLayout1.setErrorEnabled(true);
                } else {
                    flag = 1;
                    textInputLayout1.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //密码输入动态判断
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!RegexUtils.isPasswd(s)) {
                    flag1 = 0;
                    textInputLayout2.setError("只能输入6-20个字母、数字、下划线 ");
                    textInputLayout2.setErrorEnabled(true);
                } else {
                    flag1 = 1;
                    textInputLayout2.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //再次输入密码判断
        /*password1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String pwd = password.getText().toString();
                if(!s.equals(pwd)) {
                    textInputLayout3.setError("两次密码不一致");
                    textInputLayout3.setErrorEnabled(true);
                } else {
                    textInputLayout3.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
    }

    private void initView() { }

    private void initialize() {
        textInputLayout2 = (TextInputLayout) findViewById(R.id.edit2);
        textInputLayout1 = (TextInputLayout) findViewById(R.id.edit1);
        textInputLayout3 = (TextInputLayout) findViewById(R.id.edit3);
        iv_back = ((ImageView) findViewById(R.id.iv_back));
        iv_back.setOnClickListener(this);
        tv_next = ((TextView) findViewById(R.id.tv_next));
        tv_next.setOnClickListener(this);
        user_name = ((EditText) findViewById(R.id.user_name));
        password = ((EditText) findViewById(R.id.edittxt_pwd));
        password1 = (EditText) findViewById(R.id.edittxt_pwd1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                Intent intent = new Intent(register2.this,Login1.class);
                startActivity(intent);
                break;
            case R.id.tv_next:
                final String name = user_name.getText().toString();
                final String pwd1 = password1.getText().toString();
                final String pwd = password.getText().toString();
                textInputLayout3.setErrorEnabled(false);
                textInputLayout1.setErrorEnabled(false);
                textInputLayout2.setErrorEnabled(false);
                if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(pwd1)) {
                    if(pwd.equals(pwd1))
                    {
                        Log.d("register2.this",pwd+" "+pwd1);
                        if(flag == 1)
                        {
                            //注册新用户
                            final User user = new User();
                            user.setUsername(name);
                            user.setPassword(pwd);
                            user.signUp(new SaveListener<User>() {
                                @Override
                                public void done(User user, BmobException e) {
                                    if(e == null) {
                                        Toast.makeText(register2.this,"注册成功",Toast.LENGTH_SHORT).show();
                                        Intent intent1 = new Intent(register2.this,register1.class);
                                        startActivity(intent1);
                                    } else {
                                        textInputLayout1.setError("注册失败"+e.getMessage());
                                        textInputLayout1.setErrorEnabled(true);
                                        //Toast.makeText(register2.this,"注册失败"+e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            break;
                        }
                    } else {
                        Log.d("register2.this",pwd+" "+pwd1);
                        textInputLayout3.setError("两次密码不一致");
                        textInputLayout3.setErrorEnabled(true);

                    }
                } else {

                    Toast.makeText(register2.this, "用户名或密码为空",Toast.LENGTH_SHORT).show();
                }
            default:
        }

    }
}
