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

import com.example.tangwenyan.map.MainActivity;
import com.example.tangwenyan.map.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class register1 extends AppCompatActivity implements View.OnClickListener{

    //声明各id的变量
    private ImageView iv_back;  //左上角返回按钮
    private TextView tv_next;  //输入框下面按钮
    private EditText edittxt_email; //输入框内容
    private TextView skip; //跳过
    private TextInputLayout textInputLayout; //判断邮箱是否格式正确

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_first);
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
        skip = (TextView) findViewById(R.id.skip);
        skip.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                Intent intent = new Intent(register1.this,register2.class);
                startActivity(intent);
                break;
            case R.id.skip:
                Intent intent3 = new Intent(register1.this, MainActivity.class);
                startActivity(intent3);
                break;
            case R.id.tv_next:  //点击完成按钮进行发送邮件激活账号
                final String email = edittxt_email.getText().toString();
                final User user = BmobUser.getCurrentUser(User.class);
                textInputLayout.setErrorEnabled(false);

                if(!TextUtils.isEmpty(email)) {
                    String ID = user.getObjectId();
                    user.setEmail(email);
                    user.update(ID, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null) {
                                Toast.makeText(register1.this, "添加邮箱成功",Toast.LENGTH_SHORT).show();
                            } else {
                                textInputLayout.setError("添加邮箱失败"+e.getMessage());
                                textInputLayout.setErrorEnabled(true);
                            }
                        }
                    });
                } else {
                    textInputLayout.setError("邮箱为空");
                    textInputLayout.setErrorEnabled(true);
                }
                break;
            default:
        }
    }
}
