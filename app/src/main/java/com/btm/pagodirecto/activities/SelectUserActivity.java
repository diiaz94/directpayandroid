package com.btm.pagodirecto.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.btm.pagodirecto.R;
import com.btm.pagodirecto.activities.baseActivities.BaseActivity;
import com.btm.pagodirecto.services.ApiService;
import com.btm.pagodirecto.services.ServiceGenerator;
import com.btm.pagodirecto.util.Util;

import butterknife.Bind;

public class SelectUserActivity extends BaseActivity {

    @Bind(R.id.grid)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);
        Util.setActivity(this);

    loadUsers();
    }

    private void loadUsers() {
       /* ServiceGenerator.getService(ApiService.class)
                .locals(map)
                .enqueue(new LocalsCallback() {
                    @Override
                    public void handleSuccess(Response response) {
                        Locals localsResponse = (Locals) response.body();
                        ArrayList<Locals.User> list = (ArrayList<Locals.User>) localsResponse.getLocals();
                        adapter = new ViewPagerAdapter(ResultActivity.this,list,map.get("category"));
                        resultListVp.setAdapter(adapter);
                        resultListVp.setClipToPadding(false);
                        mRootView.post(new Runnable() {
                            @Override
                            public void run() {
                                int w = mRootView.getWidth();
                                Double dMargin = new Double(0.05 * w);
                                Double dPadding = new Double(0.10 * w);
                                //Util.showToastMessage("Original"+ String.valueOf(w)+" porcentaje:"+ String.valueOf(d.intValue()));
                                resultListVp.setPadding(dPadding.intValue(), 0, dPadding.intValue(), 0);
                                resultListVp.setPageMargin(dMargin.intValue());

                            }
                        });
                        //resultList.setVisibility(View.VISIBLE);
                    }*/
    }
}
