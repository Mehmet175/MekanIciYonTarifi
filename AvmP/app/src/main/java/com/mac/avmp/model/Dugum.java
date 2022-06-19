package com.mac.avmp.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Dugum implements Serializable {
    private String ad;
    private ArrayList<KomsuDugum> komsuDugums;
    private String photo;

    public Dugum() {
    }

    public Dugum(String ad) {
        this.ad = ad;
    }

    public Dugum(String ad, ArrayList<KomsuDugum> komsuDugums) {
        this.ad = ad;
        this.komsuDugums = komsuDugums;
    }

    public Dugum(String ad, ArrayList<KomsuDugum> komsuDugums, String res) {
        this.ad = ad;
        this.komsuDugums = komsuDugums;
        this.photo = res;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public ArrayList<KomsuDugum> getKomsuDugums() {
        return komsuDugums;
    }

    public void setKomsuDugums(ArrayList<KomsuDugum> komsuDugums) {
        this.komsuDugums = komsuDugums;
    }
}
