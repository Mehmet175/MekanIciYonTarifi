package com.mac.avmp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mac.avmp.R;
import com.mac.avmp.databinding.AvmListesiCardBinding;
import com.mac.avmp.model.Avm;
import com.mac.avmp.view.AvmMagazaEklemeActivity;

import java.util.List;

public class MainActivityRecyclerAdapter extends RecyclerView.Adapter<MainActivityRecyclerAdapter.MainViewHolder> {

    private Context mContext;
    private List<Avm> avms;

    public MainActivityRecyclerAdapter(Context mContext, List<Avm> avms) {
        this.mContext = mContext;
        this.avms = avms;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.avm_listesi_card, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        holder.onBind(avms.get(position));
    }

    @Override
    public int getItemCount() {
        return avms.size();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {
        AvmListesiCardBinding binding;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
        public void onBind(Avm avm) {
            binding.setAvm(avm);

            binding.imageViewEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, AvmMagazaEklemeActivity.class);
                    intent.putExtra("avm", avm);
                    mContext.startActivity(intent);
                }
            });
        }

    }
}
