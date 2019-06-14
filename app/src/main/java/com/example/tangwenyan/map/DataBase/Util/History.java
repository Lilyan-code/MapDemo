package com.example.tangwenyan.map.DataBase.Util;

public class History {
    private int _id;
    private String name;

    public History() {}

    public History(String name) {
        this.name = name;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
