package com.mac.avmpyon.model;

import java.io.Serializable;

public class KomsuDugum implements Serializable {
    private String komsuDugumAd;
    private int mesafe;
    private int kerteriz;

    public KomsuDugum() {
    }

    public KomsuDugum(String komsuDugumAd, int mesafe, int kerteriz) {
        this.komsuDugumAd = komsuDugumAd;
        this.mesafe = mesafe;
        this.kerteriz = kerteriz;
    }

    public String getKomsuDugumAd() {
        return komsuDugumAd;
    }

    public int getMesafe() {
        return mesafe;
    }

    public int getKerteriz() {
        return kerteriz;
    }

    public void setKomsuDugumAd(String komsuDugumAd) {
        this.komsuDugumAd = komsuDugumAd;
    }

    public void setMesafe(int mesafe) {
        this.mesafe = mesafe;
    }

    public void setKerteriz(int kerteriz) {
        this.kerteriz = kerteriz;
    }

}

