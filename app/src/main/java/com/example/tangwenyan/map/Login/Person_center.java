package com.example.tangwenyan.map.Login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
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


public class Person_center extends AppCompatActivity implements View.OnClickListener {
    private String[] sexArry = new String[]{ "女", "男"};// 性别选择
    private TextView et_name,birthday;
    private TextView sex;
    private ImageView back;  //左上角返回按钮
    private Context context=this;
    private String ID;
    private TextView login_out;
    private TextView change_pwd;  //修改密码按钮
    private TextView give_emial; //绑定邮箱
    private String email;
    private String email_v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_center);
        init();
        final User user = BmobUser.getCurrentUser(User.class);
        ID = user.getObjectId();
        email = user.getEmail();
        email_v = String.valueOf(user.getEmailVerified());
        String username = user.getNickname();
        String gender = user.getGender();
        Integer age =  user.getAge();
        Log.d("Person_center",username+gender+age+ID+" "+email+email_v);
        if(gender != null)
            sex.setText(gender+"");
        if(username != null)
            et_name.setText(username+"");
        if(age != null)
            birthday.setText(String.valueOf(age));
    }

    private void init() {
        sex = ((TextView) findViewById(R.id.tv_sex));
        et_name = ((TextView) findViewById(R.id.et_name));
        birthday = ((TextView) findViewById(R.id.tv_birthday));
        back = ((ImageView) findViewById(R.id.iv_return));
        login_out = (TextView) findViewById(R.id.login_out);
        change_pwd = (TextView) findViewById(R.id.change_pwd);
        give_emial = (TextView) findViewById(R.id.give_email);
        give_emial.setOnClickListener(this);
        change_pwd.setOnClickListener(this);
        login_out.setOnClickListener(this);
        sex.setOnClickListener(this);
        et_name.setOnClickListener(this);
        birthday.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.give_email:
                if(email == null)
                {
                    Intent intent0 = new Intent(Person_center.this,GiveEmail.class);
                    startActivity(intent0);
                } else if(email_v == "false"){
                    BmobUser.requestEmailVerify(email, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(Person_center.this, "您已绑定邮箱，未激活，请前往" + email + "邮箱中进行激活账户。", Toast.LENGTH_LONG).show();
                            } else {
                                Log.e("BMOB", e.toString());
                                Toast.makeText(Person_center.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(Person_center.this,"目前您已绑定邮箱且激活"+email,Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_sex:
                showSexChooseDialog();
                break;
            case R.id.et_name:
                onCreateNameDialog();
                break;
            case R.id.tv_birthday:
                showbrithdayDialog();
                break;
            case R.id.iv_return:
                Intent intent = new Intent(Person_center.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.login_out:
                //退出登录
                BmobUser.logOut();
                Intent intent1 = new Intent(Person_center.this, MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.change_pwd:
                Intent intent2 = new Intent(Person_center.this, changemm.class);
                startActivity(intent2);
                break;
        }
    }

    //年龄设置
    private void showbrithdayDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View nameView = layoutInflater.inflate(R.layout.set_birthday, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // 使用setView()方法将布局显示到dialog
        alertDialogBuilder.setView(nameView);

        final EditText userInput = (EditText) nameView.findViewById(R.id.changebir_edit);
        final TextView name = (TextView) findViewById(R.id.tv_birthday);
        // 设置Dialog按钮
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // 获取edittext的内容,显示到textview
                                String age = userInput.getText().toString();
                                name.setText(age);
                                User user = new User();
                                user.setAge(Integer.parseInt(age));
                                user.update(ID, new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if(e == null) {
                                            Toast.makeText(Person_center.this,"success",Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(Person_center.this,"fail"+e.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }


    //性别设置
    private void showSexChooseDialog() {
        AlertDialog.Builder builder3 = new AlertDialog.Builder(this);// 自定义对话框
        builder3.setSingleChoiceItems(sexArry, 0, new DialogInterface.OnClickListener() {// 2默认的选中

            @Override
            public void onClick(DialogInterface dialog, int which) {// which是被选中的位置
                // showToast(which+"");
                String gender = sexArry[which];
                sex.setText(gender);
                User user = new User();
                user.setGender(gender);
                user.update(ID, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e == null) {
                            Toast.makeText(Person_center.this,"success",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Person_center.this,"fail"+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();// 随便点击一个item消失对话框，不用点击确认取消
            }
        });
        builder3.show();// 让弹出框显示
    }

    //昵称设置
    private void onCreateNameDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View nameView = layoutInflater.inflate(R.layout.set_name, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // 使用setView()方法将布局显示到dialog
        alertDialogBuilder.setView(nameView);

        final EditText userInput = (EditText) nameView.findViewById(R.id.changename_edit);
        final TextView name = (TextView) findViewById(R.id.et_name);


        // 设置Dialog按钮
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // 获取edittext的内容,显示到textview
                                String newname = userInput.getText().toString();
                                name.setText(newname);
                                User user = new User();
                                user.setNickname(newname);
                                user.update(ID, new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if(e == null) {
                                            Toast.makeText(Person_center.this,"success",Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(Person_center.this,"fail"+e.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}