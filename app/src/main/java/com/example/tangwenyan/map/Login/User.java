package com.example.tangwenyan.map.Login;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class User extends BmobUser {

    private String nickname;    //昵称
    private String gender;     //性别
    private Integer age;        //年龄
    private BmobFile avatar;  //头像

    public User setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public User setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public User setAge(Integer age) {
        this.age = age;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public BmobFile getAvatar() {
        return avatar;
    }

    public User setAvatar(BmobFile avatar) {
        this.avatar = avatar;
        return this;
    }
}
