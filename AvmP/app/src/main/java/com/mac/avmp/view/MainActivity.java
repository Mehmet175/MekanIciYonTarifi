package com.mac.avmp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.mac.avmp.R;
import com.mac.avmp.adapter.MainActivityRecyclerAdapter;
import com.mac.avmp.databinding.ActivityMainBinding;
import com.mac.avmp.model.Avm;
import com.mac.avmp.viewmodel.MainActivityViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainActivityViewModel viewModel;

    private MainActivityRecyclerAdapter recyclerAdapter;
    private ArrayList<Avm> rvVerileri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setActivity(this);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        viewModelListener();
        rvSettings();
    }

    private void rvSettings() {
        rvVerileri = new ArrayList<>();
        recyclerAdapter = new MainActivityRecyclerAdapter(this, rvVerileri);
        binding.rvAvmListesi.setHasFixedSize(false);
        binding.rvAvmListesi.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        binding.rvAvmListesi.setAdapter(recyclerAdapter);
        new ItemTouchHelper(callback).attachToRecyclerView(binding.rvAvmListesi);
    }

    private void viewModelListener() {
        showProgress();
        viewModel.getAvm();
        viewModel.getAvmsMutableData().observe(this, new Observer<ArrayList<Avm>>() {
            @Override
            public void onChanged(ArrayList<Avm> avms) {
                rvVerileri.clear();

                for (int i = avms.size() - 1; i >= 0; i--) {
                    Avm avm = avms.get(i);
                    rvVerileri.add(avm);
                }
                recyclerAdapter.notifyDataSetChanged();
                hideProgress();
            }
        });

        viewModel.getHataMutableData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Snackbar.make(binding.getRoot(), "Hata: " + s, Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    public void fabClick() {
        startActivity(new Intent(
                getApplicationContext(), AvmEklemeActivity.class
        ));
    }

    private void showProgress() {
        binding.rvAvmListesi.setVisibility(View.INVISIBLE);
        binding.fabAvmOlustur.setVisibility(View.INVISIBLE);
        binding.progressBarMainActivity.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        binding.rvAvmListesi.setVisibility(View.VISIBLE);
        binding.fabAvmOlustur.setVisibility(View.VISIBLE);
        binding.progressBarMainActivity.setVisibility(View.INVISIBLE);
    }

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            View view = getLayoutInflater().inflate(R.layout.avm_silme_aler, null, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setView(view);
            builder.setPositiveButton("Sil", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (((CheckBox) view.findViewById(R.id.checkBoxKontrol1)).isChecked()
                            && ((CheckBox) view.findViewById(R.id.checkBoxKontrol2)).isChecked()) {
                        viewModel.silAvm(rvVerileri.get(viewHolder.getLayoutPosition()).getKey());
                    } else {
                        Snackbar.make(binding.getRoot(), "Lütfen, tüm koşulları kabul edip tekrar deneyiniz...", Snackbar.LENGTH_SHORT).show();
                        recyclerAdapter.notifyDataSetChanged();
                    }
                }
            }).setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    recyclerAdapter.notifyDataSetChanged();
                }
            });
            builder.create().show();

        }
    };
}