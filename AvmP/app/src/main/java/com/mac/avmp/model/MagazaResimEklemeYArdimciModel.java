package com.mac.avmp.model;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import java.io.Serializable;

public class MagazaResimEklemeYArdimciModel implements Serializable {
    private ImageView view;
    private Uri resimUri;

    public MagazaResimEklemeYArdimciModel() {
    }

    public MagazaResimEklemeYArdimciModel(ImageView view, Uri resimUri) {
        this.view = view;
        this.resimUri = resimUri;
    }

    public ImageView getView() {
        return view;
    }

    public void setView(ImageView view) {
        this.view = view;
    }

    public Uri getResimUri() {
        return resimUri;
    }

    public void setResimUri(Uri resimUri) {
        this.resimUri = resimUri;
    }
}
