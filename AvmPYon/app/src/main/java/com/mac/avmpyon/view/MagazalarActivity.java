package com.mac.avmpyon.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.mac.avmpyon.R;
import com.mac.avmpyon.adapters.MagazalarRvAdapter;
import com.mac.avmpyon.databinding.ActivityMagazalarBinding;
import com.mac.avmpyon.model.Avm;
import com.mac.avmpyon.model.Dugum;

import java.util.ArrayList;

public class MagazalarActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ActivityMagazalarBinding binding;
    private Avm avm;
    private ArrayList<Dugum> magazalar, magazalarYdek;

    private MagazalarRvAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_magazalar);

        avm = (Avm) getIntent().getSerializableExtra("avm");

        toolbarAyar();
        rvAyar();
    }

    private void rvAyar() {
        magazalar = (ArrayList<Dugum>) avm.getAvmDugumleri().clone();
        adapter = new MagazalarRvAdapter(this, magazalar);
        binding.rvMagazalar.setHasFixedSize(true);
        binding.rvMagazalar.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        binding.rvMagazalar.setAdapter(adapter);
    }

    private void toolbarAyar() {
        setSupportActionBar(binding.toolbarMagazalar);
        binding.toolbarMagazalar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_new_24);
        binding.toolbarMagazalar.setNavigationOnClickListener(x -> onBackPressed());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.magaza_detay_search_menu, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public boolean onQueryTextChange(String newText) {
        magazalar.clear();
        for (Dugum d : avm.getAvmDugumleri()) {
            if (d.getAd().toLowerCase().contains(newText.toLowerCase())) {
                magazalar.add(d);
                adapter.notiftDataSet__();
            }

        }
        return true;
    }
}