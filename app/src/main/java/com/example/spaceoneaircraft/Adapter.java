package com.example.spaceoneaircraft;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> implements Filterable {

    List<Aircrafts> aircrafts, aircraftsFilter;
    private Context context;
    private RecyclerViewClickListener mListener;
    CustomFilter filter;

    public Adapter(List<Aircrafts> aircrafts, Context context, RecyclerViewClickListener listener) {
        this.aircrafts = aircrafts;
        this.aircraftsFilter = aircrafts;
        this.context = context;
        this.mListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view, mListener);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder( Adapter.MyViewHolder holder, int position) {
        holder.mName.setText(aircrafts.get(position).getName());
        holder.mManufacturer.setText(aircrafts.get(position).getManufacturer());
        holder.mManufacturingYear.setText(aircrafts.get(position).getManufacturingYear());


    }

    @Override
    public int getItemCount() {
        return aircrafts.size();
    }

    @Override
    public Filter getFilter() {
        if (filter==null) {
        filter= new CustomFilter((ArrayList<Aircrafts>) aircraftsFilter,this);

    }
        return filter;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RecyclerViewClickListener mListener;
        private TextView mName, mManufacturer, mManufacturingYear;
        private RelativeLayout mRowContainer;

        public MyViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            mName = itemView.findViewById(R.id.name);
            mManufacturer = itemView.findViewById(R.id.manufacturer);
            mManufacturingYear = itemView.findViewById(R.id.manufacturing_year);
            mRowContainer = itemView.findViewById(R.id.row_container);

            mListener = listener;
            mRowContainer.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.row_container:
                    mListener.onRowClick(mRowContainer, getAdapterPosition());
                    break;
                default:
                    break;
            }
        }
    }

    public interface RecyclerViewClickListener {
        void onRowClick(View view, int position);
    }
}
