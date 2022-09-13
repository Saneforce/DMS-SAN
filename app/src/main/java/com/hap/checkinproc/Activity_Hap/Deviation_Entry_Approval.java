package com.hap.checkinproc.Activity_Hap;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;

import com.hap.checkinproc.Model_Class.Deviation_Entr_Modal;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.Deviation_Approval_Adapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hap.checkinproc.Activity_Hap.Leave_Request.CheckInfo;

public class Deviation_Entry_Approval extends AppCompatActivity {
    String Scode;
    String Dcode;
    String Rf_code;

    List<Deviation_Entr_Modal> approvalList;
    Gson gson;
    private RecyclerView recyclerView;
    Type userType;
    Common_Class common_class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deviation__entry__approval);
        TextView txtHelp = findViewById(R.id.toolbar_help);
        ImageView imgHome = findViewById(R.id.toolbar_home);
        txtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Help_Activity.class));
            }
        });
        TextView txtErt = findViewById(R.id.toolbar_ert);
        TextView txtPlaySlip = findViewById(R.id.toolbar_play_slip);

        txtErt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ERT.class));
            }
        });
        txtPlaySlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        ObjectAnimator textColorAnim;
        textColorAnim = ObjectAnimator.ofInt(txtErt, "textColor", Color.WHITE, Color.TRANSPARENT);
        textColorAnim.setDuration(500);
        textColorAnim.setEvaluator(new ArgbEvaluator());
        textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
        textColorAnim.setRepeatMode(ValueAnimator.REVERSE);
        textColorAnim.start();
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
                Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
                if (CheckIn == true) {
                    Intent Dashboard = new Intent(getApplicationContext(), Dashboard_Two.class);
                    Dashboard.putExtra("Mode", "CIN");
                    startActivity(Dashboard);
                } else
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));


            }
        });
        recyclerView = findViewById(R.id.deviationentryrecyclerview);
        common_class = new Common_Class(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        common_class.ProgressdialogShow(1, "Deviation Entry Approval");
        Rf_code = Scode;
        gson = new Gson();
        DeviationEntryStatus();

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                common_class.CommonIntentwithFinish(Approvals.class);
            }
        });
    }

    public void DeviationEntryStatus() {
        String routemaster = " {\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Object> mCall = apiInterface.GetTPObject(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode, "vwDeviation", routemaster);

        mCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                // locationList=response.body();
                Log.e("GetCurrentMonth_Values", String.valueOf(response.body().toString()));
                Log.e("TAG_TP_RESPONSE", "response Tp_View: " + new Gson().toJson(response.body()));

                userType = new TypeToken<ArrayList<Deviation_Entr_Modal>>() {
                }.getType();
                approvalList = gson.fromJson(new Gson().toJson(response.body()), userType);

                recyclerView.setAdapter(new Deviation_Approval_Adapter(approvalList, R.layout.deviation_entry_listitem, getApplicationContext(), new AdapterOnClick() {
                    @Override
                    public void onIntentClick(int Name) {
                        Intent intent = new Intent(Deviation_Entry_Approval.this, Deviation_Approval_Reject.class);
                        intent.putExtra("Username", approvalList.get(Name).getFieldForceName());
                        intent.putExtra("Emp_Code", approvalList.get(Name).getEmpCode());
                        intent.putExtra("HQ", approvalList.get(Name).getHQ());
                        intent.putExtra("Designation", approvalList.get(Name).getDesignation());
                        intent.putExtra("Applieddate", approvalList.get(Name).getApplieddate());
                        intent.putExtra("MobileNumber", approvalList.get(Name).getSFMobile());
                        intent.putExtra("deviation_date", approvalList.get(Name).getDeviationDate());
                        intent.putExtra("deviation_type", approvalList.get(Name).getDeviationName());
                        intent.putExtra("Reason", approvalList.get(Name).getReason());
                        intent.putExtra("Sf_Code", approvalList.get(Name).getSfCode());
                        intent.putExtra("deviation_id", String.valueOf(approvalList.get(Name).getDeviationId()));
                        startActivity(intent);
                        finish();
                    }
                }));
                common_class.ProgressdialogShow(2, "On-duty Approval");
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                common_class.ProgressdialogShow(2, "On-duty Approval");
            }
        });

    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    Deviation_Entry_Approval.super.onBackPressed();
                }
            });

    @Override
    public void onBackPressed() {

    }

}