package com.mac.avmp.view;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mac.avmp.R;
import com.mac.avmp.databinding.ActivityAvmMagazaEklemeBinding;
import com.mac.avmp.model.Avm;
import com.mac.avmp.model.Dugum;
import com.mac.avmp.model.MagazaRes;
import com.mac.avmp.model.MagazaResimEklemeYArdimciModel;
import com.mac.avmp.viewmodel.AvmMagazaEklemeActivityViewModel;
import com.mac.avmp.viewmodel.ViewModelStatus;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class AvmMagazaEklemeActivity extends AppCompatActivity {

    private ActivityAvmMagazaEklemeBinding binding;
    private Avm avm;
    private ArrayList<View> views;
    private ArrayList<MagazaResimEklemeYArdimciModel> viewImageS;
    private ArrayList<MagazaResimEklemeYArdimciModel> resler;
    AvmMagazaEklemeActivityViewModel viewModel;

    ProgressDialog _dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_avm_magaza_ekleme);
        binding.setActivity(this);
        viewModel = new ViewModelProvider(this).get(AvmMagazaEklemeActivityViewModel.class);


        viewImageS = new ArrayList<>();

        avm = (Avm) getIntent().getSerializableExtra("avm");
        toolbarSettings();

        views = new ArrayList<>();
        resler = new ArrayList<>();

        oncekiVerileriDoldurma();
        viewModelListener();
    }

    private void viewModelListener() {
        viewModel.getInsertCheck().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals(ViewModelStatus.basarili)) {
                    hideDialog();
                    Intent intent = new Intent(getApplicationContext(), KomsuBelirlemeActivity.class);
                    intent.putExtra("dugums", dugums);
                    intent.putExtra("avm", avm);
                    startActivity(intent);

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                    builder.setTitle("Hata");
                    builder.setMessage("Resimler yüklenirken hata oluştu...");
                    builder.setPositiveButton("Tamam", null);
                    builder.create().show();
                }
            }
        });
    }

    private void oncekiVerileriDoldurma() {
        if (avm.getAvmDugumleri() != null && avm.getAvmDugumleri().size() != 0) {
            for (Dugum d : avm.getAvmDugumleri()) {
                View view = getLayoutInflater().inflate(R.layout.magaza_onu_ekleme, null, false);
                EditText editTextMagazaAdi = view.findViewById(R.id.editTextMagazaAdi);
                editTextMagazaAdi.setText(d.getAd());
                binding.linearLayoutMagazalar.addView(view);
                views.add(view);

                ((ImageView)view.findViewById(R.id.imageViewResimSec)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageView = (ImageView) v;
                        activityResultLauncher.launch("image/*");
                    }
                });
            }
        }
    }

    private void toolbarSettings() {
        binding.toolbarAvmEkle.setTitle("");
        binding.toolbarAvmEkle.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        setSupportActionBar(binding.toolbarAvmEkle);
        binding.toolbarAvmEkle.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.avm_ekleme_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_ok) {
            dugums();
            if (dugums.size() != 0) {
                if (ress.size() == 0) {
                    Intent intent = new Intent(getApplicationContext(), KomsuBelirlemeActivity.class);
                    intent.putExtra("dugums", dugums);
                    intent.putExtra("avm", avm);
                    startActivity(intent);
                } else {
                    showDialog("Resimler yükleniyor...");
                    viewModel.resimleriEkle(ress);
                }
            } else {
                Toast.makeText(getApplicationContext(), "Önce mağaza ekleyiniz...", Toast.LENGTH_SHORT).show();
            }
        } else {
        }
        return super.onOptionsItemSelected(item);
    }

    ArrayList<MagazaRes> ress = new ArrayList<>();
    ArrayList<Dugum> dugums = new ArrayList<>();

    private ArrayList<Dugum> dugums() {
        for (View v : views) {
            Dugum dugum = new Dugum();

            EditText editText = v.findViewById(R.id.editTextMagazaAdi);
            dugum.setAd(editText.getText().toString());

            ImageView imageView = v.findViewById(R.id.imageViewResimSec);
            MagazaResimEklemeYArdimciModel secilenRes = resAl(imageView);
            if (secilenRes != null) {
                String resAdı = UUID.randomUUID().toString();
                ress.add(new MagazaRes(resAdı, secilenRes.getResimUri()));
                dugum.setPhoto(resAdı);
            }
            dugums.add(dugum);
        }
        return dugums;
    }


    private MagazaResimEklemeYArdimciModel resAl(ImageView imageView){
        for (MagazaResimEklemeYArdimciModel m : resler) {
            if (m.getView() == imageView) {
                return m;
            }
        }
        return null;
    }

    ImageView imageView;
    ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            if (result != null) {
                imageView.setImageURI(result);
                resler.add(new MagazaResimEklemeYArdimciModel(imageView, result));
            }
        }
    });

    public void fabClickMagazaEkle() {
        View view = getLayoutInflater().inflate(R.layout.magaza_onu_ekleme, null, false);
        binding.linearLayoutMagazalar.addView(view);
        ((ImageView)view.findViewById(R.id.imageViewResimSec)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView = (ImageView) v;
                activityResultLauncher.launch("image/*");
            }
        });
        views.add(view);
    }

    void showDialog(String message){
        _dialog = new ProgressDialog(this);
        _dialog.setMessage(message);
        _dialog.show();
    }
    void hideDialog() {
        if (_dialog != null && _dialog.isShowing()) {
            _dialog.hide();
        }
    }

}