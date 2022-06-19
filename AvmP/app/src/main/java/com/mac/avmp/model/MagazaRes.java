package com.mac.avmp.model;

import android.net.Uri;

import java.io.File;
import java.io.Serializable;

public class MagazaRes implements Serializable {
    private String name;
    private Uri res;

    public MagazaRes() {
    }

    public MagazaRes(String name, Uri res) {
        this.name = name;
        this.res = res;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getRes() {
        return res;
    }

    public void setRes(Uri res) {
        this.res = res;
    }
}
