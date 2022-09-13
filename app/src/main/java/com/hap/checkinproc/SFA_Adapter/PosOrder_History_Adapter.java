package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.TodayPrimOrdActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PosOrder_History_Adapter extends RecyclerView.Adapter<PosOrder_History_Adapter.MyViewHolder> {

    Context context;
    JSONArray mDate;
    AdapterOnClick mAdapterOnClick;
    String mResponse;

    public PosOrder_History_Adapter(Context context, JSONArray mDate, String mResponse, AdapterOnClick mAdapterOnClick) {
        this.context = context;
        this.mDate = mDate;
        this.mAdapterOnClick = mAdapterOnClick;
        this.mResponse = mResponse;
    }

    @NonNull
    @Override
    public PosOrder_History_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.posorder_history_recyclerview, null, false);

        return new PosOrder_History_Adapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(PosOrder_History_Adapter.MyViewHolder holder, int position) {
        try {
            JSONObject obj = mDate.getJSONObject(position);
            holder.txtOrderDate.setText("" + obj.getString("Order_Date"));
            holder.txtOrderID.setText(obj.getString("OrderNo"));
            holder.txtValue.setText("" + new DecimalFormat("##0.00").format(Double.parseDouble(obj.getString("Order_Value"))));
            holder.Itemcountinvoice.setText(obj.getString("Status"));

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapterOnClick.onIntentClick(position);
                }
            });


        } catch (Exception e) {
            Log.v("primAdapter:", e.getMessage());
        }
    }


    @Override
    public int getItemCount() {
        return mDate.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderDate, txtOrderID, txtValue, Itemcountinvoice;
        LinearLayout linearLayout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderID = itemView.findViewById(R.id.txt_order);
            txtOrderDate = itemView.findViewById(R.id.txt_date);
            txtValue = itemView.findViewById(R.id.txt_total);
            linearLayout = itemView.findViewById(R.id.row_report);
            Itemcountinvoice = itemView.findViewById(R.id.Itemcountinvoice);


        }
    }
}
