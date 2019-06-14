package com.example.tangwenyan.map.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tangwenyan.map.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class GiveEmail extends AppCompatActivity implements View.OnClickListener{

    //声明各id的变量
    private ImageView iv_back;  //左上角返回按钮
    private TextView tv_next;  //输入框下面按钮
    private EditText edittxt_email; //输入框内容
    private TextInputLayout textInputLayout; //验证邮箱格式
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.give_email);
        initialize();
        initView();
        edittxt_email.addTextChangedListener(new TextWatcher() {
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
        iv_back = ((ImageView) findViewById(R.id.iv_back));
        iv_back.setOnClickListener(this);
        tv_next = ((TextView) findViewById(R.id.tv_next));
        tv_next.setOnClickListener(this);
        edittxt_email = ((EditText) findViewById(R.id.email));
        edittxt_email.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                Intent intent = new Intent(GiveEmail.this,Person_center.class);
                startActivity(intent);
                break;
            case R.id.tv_next:  //点击完成按钮进行发送邮件激活账号
                final String email = edittxt_email.getText().toString();
                final User user = BmobUser.getCurrentUser(User.class);
                String ID = user.getObjectId();
                user.setEmail(email);
                user.update(ID, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e == null) {
                            Toast.makeText(GiveEmail.this, "绑定邮箱成功",Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(GiveEmail.this,Person_center.class);
                            startActivity(intent1);
                            /*BmobUser.requestEmailVerify(email, new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e == null) {
                                        Toast.makeText(GiveEmail.this,"请求验证邮件成功，请到"+email+"邮箱中进行激活账户。",Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.e("BMOB",e.toString());
                                        Toast.makeText(GiveEmail.this,"邮箱激活不成功"+e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });*/
                        } else {
                            Toast.makeText(GiveEmail.this,"绑定邮箱失败"+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                if(!TextUtils.isEmpty(email)) {

                } else {
                    Toast.makeText(GiveEmail.this,"邮箱为空",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
}
