package com.mac.avmp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Avm implements Serializable {
    private String key;
    private String ad;
    private ArrayList<Dugum> avmDugumleri;
    private ArrayList<String> resler;


    public Avm() {
    }

    public Avm(String ad) {
        this.ad = ad;
    }


    public Avm(String key, String ad, ArrayList<Dugum> avmDugumleri) {
        this.key = key;
        this.ad = ad;
        this.avmDugumleri = avmDugumleri;
    }

    public Avm(String key, String ad) {
        this.key = key;
        this.ad = ad;
    }

    public Avm(String key, String ad, ArrayList<Dugum> avmDugumleri, ArrayList<String> resler) {
        this.key = key;
        this.ad = ad;
        this.avmDugumleri = avmDugumleri;
        this.resler = resler;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public ArrayList<Dugum> getAvmDugumleri() {
        return avmDugumleri;
    }

    public void setAvmDugumleri(ArrayList<Dugum> avmDugumleri) {
        this.avmDugumleri = avmDugumleri;
    }

    public void setResler(ArrayList<String> resler) {
        this.resler = resler;
    }

    public ArrayList<String> getResler() {
        return resler;
    }
}

