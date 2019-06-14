package com.example.tangwenyan.map.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
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
import com.example.tangwenyan.map.Activity.CountDownTimerUtils;
import com.example.tangwenyan.map.Login.Change_pwd;
import com.example.tangwenyan.map.Login.Login1;
import com.example.tangwenyan.map.Login.User;
import com.example.tangwenyan.map.Login.register2;
import com.example.tangwenyan.map.MainActivity;
import com.example.tangwenyan.map.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class Phone_Login extends AppCompatActivity implements View.OnClickListener{

    private ImageView qq_login, sina_login, wx_login;   //第三方登录，设置点击弹出事件
    private TextView login_in;  //登录按钮
    private ImageView register; //注册按钮
    private TextView forget_pwd; //忘记密码
    private EditText et_phone;  //用户名
    private EditText pwd;   //密码
    private ImageView back;  //返回按钮
    private TextView phone_login; //手机登录
    private TextInputLayout textInputLayout1; //用户名判断
    private TextView code;  //获取验证码按钮
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_login);
        //Bmob的Application ID
        Bmob.initialize(this,"8ca4d792f5c8b4a700425736be46b2a4");
        initialize();
        initView();
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!RegexUtils.isMobileSimple(s)){
                    textInputLayout1.setError("手机格式错误");
                    textInputLayout1.setErrorEnabled(true);
                }else {
                    textInputLayout1.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initView() { }

    private void initialize() {
        qq_login = ((ImageView) findViewById(R.id.qq_login));
        qq_login.setOnClickListener(this);
        sina_login = ((ImageView) findViewById(R.id.sina_login));
        sina_login.setOnClickListener(this);
        wx_login = ((ImageView) findViewById(R.id.wx_login));
        wx_login.setOnClickListener(this);
        login_in = ((TextView) findViewById(R.id.login_in));
        login_in.setOnClickListener(this);
        register = ((ImageView) findViewById(R.id.register_account));
        register.setOnClickListener(this);
        forget_pwd = ((TextView) findViewById(R.id.tv_forget_password));
        forget_pwd.setOnClickListener(this);
        et_phone = ((EditText) findViewById(R.id.et_phone));
        pwd = ((EditText) findViewById(R.id.et_password));
        back = (ImageView) findViewById(R.id.iv_return);
        back.setOnClickListener(this);
        phone_login = (TextView) findViewById(R.id.phone);
        phone_login.setOnClickListener(this);
        textInputLayout1 = (TextInputLayout) findViewById(R.id.edit1);
        code = (TextView) findViewById(R.id.code);
        code.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.code:
                final String num = et_phone.getText().toString().trim();
                if(!TextUtils.isEmpty(num)) {
                    Log.d("MainActivity",num);
                    BmobSMS.requestSMSCode(num, "twy", new QueryListener<Integer>() {
                        @Override
                        public void done(Integer smsId, BmobException e) {
                            if(e == null) {
                                Toast.makeText(Phone_Login.this,"发送验证码成功", Toast.LENGTH_SHORT).show();
                                // 发送成功进入倒计时
                                countDownTimer = new CountDownTimerUtils(code, 60000, 1000);
                                countDownTimer.start();
                            } else {
                                Toast.makeText(Phone_Login.this,"发送验证码失败：" + e.getMessage() + "\n", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(Phone_Login.this,"手机号为空", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.phone:
                Intent intent11 = new Intent(Phone_Login.this, Login1.class);
                startActivity(intent11);
                break;
            case R.id.iv_return:
                Intent intent10 = new Intent(Phone_Login.this, MainActivity.class);
                startActivity(intent10);
                break;
            case R.id.qq_login:
                Toast.makeText(Phone_Login.this,"QQ登录",Toast.LENGTH_SHORT).show();
                break;
            case R.id.sina_login:
                Toast.makeText(Phone_Login.this,"微博登录",Toast.LENGTH_SHORT).show();
                break;
            case R.id.wx_login:
                Toast.makeText(Phone_Login.this,"微信登录",Toast.LENGTH_SHORT).show();
                break;
            case R.id.login_in:  //点击完成按钮进行发送邮件激活账号
                final String username = et_phone.getText().toString().trim();
                final String password = pwd.getText().toString().trim();
                //System.out.print(username+password);
                if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    Log.d("MainActivity",username+"abc"+password);
                    //登录同时保存其他数据
                    User user = new User();
                    //设置手机号码（必填）
                    user.setMobilePhoneNumber(username);
                    //设置用户名，如果没有传用户名，则默认为手机号码
                    user.setUsername(username);
                    //设置用户密码
                    user.setPassword("");
                    user.signOrLogin(password, new SaveListener<User>() {
                        @Override
                        public void done(User user,BmobException e) {
                            if (e == null) {
                                Toast.makeText(Phone_Login.this,"保存成功：" + user.getUsername(),Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Phone_Login.this, MainActivity.class));
                            } else {
                                Toast.makeText(Phone_Login.this,"保存失败：" + e.getErrorCode() + "-" + e.getMessage() + "\n",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(Phone_Login.this,"手机号或验证码为空",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.register_account:
                Intent intent2 = new Intent(Phone_Login.this, register2.class);
                startActivity(intent2);
                break;
            case R.id.tv_forget_password:
                Intent intent3 = new Intent(Phone_Login.this, Change_pwd.class);
                startActivity(intent3);
                break;
            default:
        }
    }
}