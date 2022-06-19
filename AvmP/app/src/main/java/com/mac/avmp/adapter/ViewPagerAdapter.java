package com.mac.avmp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mac.avmp.R;

import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder> {

    private Context mContext;
    private List<Uri> resler;

    public ViewPagerAdapter(Context mContext, List<Uri> resler) {
        this.mContext = mContext;
        this.resler = resler;
    }

    @NonNull
    @Override
    public ViewPagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewPagerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_pager_tasarimi, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerViewHolder holder, int position) {
        holder.onBind(resler.get(position));
    }

    @Override
    public int getItemCount() {
        return resler.size();
    }

    public class ViewPagerViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;

        public ViewPagerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewSecilenRes);
        }

        private void onBind(Uri uri) {
            imageView.setImageURI(uri);
        }
    }
}
