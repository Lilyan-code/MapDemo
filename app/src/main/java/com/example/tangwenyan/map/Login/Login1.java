package com.example.tangwenyan.map.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tangwenyan.map.Activity.Phone_Login;
import com.example.tangwenyan.map.MainActivity;
import com.example.tangwenyan.map.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.UpdateListener;

public class Login1 extends AppCompatActivity implements View.OnClickListener{

    private ImageView qq_login, sina_login, wx_login;   //第三方登录，设置点击弹出事件
    private TextView login_in;  //登录按钮
    private ImageView register; //注册按钮
    private TextView forget_pwd; //忘记密码
    private EditText et_phone;  //用户名
    private EditText pwd;   //密码
    private ImageView back;  //返回按钮
    private TextView phone_login; //手机登录
    private TextInputLayout textInputLayout2;  //密码判断
    private TextInputLayout textInputLayout1; //用户名判断

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        //Bmob的Application ID
        Bmob.initialize(this,"8ca4d792f5c8b4a700425736be46b2a4");
        initialize();
        initView();
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
        textInputLayout2 = (TextInputLayout) findViewById(R.id.edit2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_return:
                Intent intent10 = new Intent(Login1.this, MainActivity.class);
                startActivity(intent10);
                break;
            case R.id.phone:
                Intent intent11 = new Intent(Login1.this, Phone_Login.class);
                startActivity(intent11);
                break;
            case R.id.qq_login:
                Toast.makeText(Login1.this,"QQ登录",Toast.LENGTH_SHORT).show();
                break;
            case R.id.sina_login:
                Toast.makeText(Login1.this,"微博登录",Toast.LENGTH_SHORT).show();
                break;
            case R.id.wx_login:
                Toast.makeText(Login1.this,"微信登录",Toast.LENGTH_SHORT).show();
                break;
            case R.id.login_in:  //点击完成按钮进行发送邮件激活账号
                final String username = et_phone.getText().toString().trim();
                final String password = pwd.getText().toString().trim();
                textInputLayout1.setErrorEnabled(false);
                textInputLayout2.setErrorEnabled(false);
                //System.out.print(username+password);
                if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    BmobUser.loginByAccount(username,password, new LogInListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if(e ==  null ){
                                //试想获得当前已登录用户的信息 ****亲测没有效果，isLogin红色
                                /*if(BmobUser.isLogin()){
                                    BmobUser user1 = BmobUser.getCurrentUser(User.class);
                                    Toast.makeText(Login1.this,"登录成功"+user1.getUsername,Toast.LENGTH_SHORT).show();
                                }*/
                                //下面这个方法可以获取到
                                BmobUser bmobuser = BmobUser.getCurrentUser();
                                String name = bmobuser.getUsername();
                                /*if (bmobuser != null) {
                                    Log.d("Login1",name);

                                    //Toast.makeText(Login1.this, "已经登录：" + bmobuser.getUsername()+bmobuser.getObjectId(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(Login1.this,"尚未登录",Toast.LENGTH_SHORT).show();
                                }*/
                                Intent intent = new Intent(Login1.this,MainActivity.class);
                                startActivity(intent);
                            } else {
                                // Log.e("BMOB", e.toString());
                                textInputLayout1.setError("登录失败"+e.getMessage());
                                textInputLayout1.setErrorEnabled(true);
                                //Toast.makeText(Login1.this,"登录失败"+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(Login1.this,"用户名或密码为空",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.register_account:
                Intent intent2 = new Intent(Login1.this,register2.class);
                startActivity(intent2);
                break;
            case R.id.tv_forget_password:
                Intent intent3 = new Intent(Login1.this,Change_pwd.class);
                startActivity(intent3);
                break;
            default:
        }
    }
}