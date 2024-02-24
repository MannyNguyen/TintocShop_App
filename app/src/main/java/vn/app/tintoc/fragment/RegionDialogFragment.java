package vn.app.tintoc.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import vn.app.tintoc.R;
import vn.app.tintoc.adapter.RegionAdapter;
import vn.app.tintoc.adapter.RegionRecyclerAdapter;
import vn.app.tintoc.config.Async;
import vn.app.tintoc.config.CmmFunc;
import vn.app.tintoc.helper.APIHelper;
import vn.app.tintoc.model.RegionObj;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 10/18/2017.
 */

public class RegionDialogFragment extends BaseFragment implements View.OnClickListener {
    //region Var
    ImageView ivClose;
    TextView txtRegionType;
    RecyclerView recyclerRegion;
    FrameLayout frameRegion;
    int defaultSenderDistrictID = -1;
    int defaultReceiverDistrictID = -1;
    public static List<RegionObj> regionObjList = new ArrayList<>();
    public static List<RegionObj> regionObjSenderList = new ArrayList<>();

    public static List<RegionObj> regionAddressList = new ArrayList<>();
    //endregion

    //region newInstance
    public static RegionDialogFragment newInstance(int type, int idCity, int idDistrict, int idWard) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putInt("idCity", idCity);
        args.putInt("idDistrict", idDistrict);
        args.putInt("idWard", idWard);
        RegionDialogFragment fragment = new RegionDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
    //endregion

    //region Init
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            return view;
        }
        view = inflater.inflate(R.layout.region_dialogfragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (!isLoad) {
            ivClose = view.findViewById(R.id.iv_close);
            ivClose.setOnClickListener(this);
            txtRegionType = view.findViewById(R.id.txt_region_type);
            recyclerRegion = view.findViewById(R.id.recycler_region);
            frameRegion = view.findViewById(R.id.frame_region);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerRegion.setLayoutManager(layoutManager);

            new GetCity().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            frameRegion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Nothing
                }
            });

            isLoad = true;
        }
    }
    //endregion

    //region Event
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                ivClose.setOnClickListener(null);
                if (regionObjList.size() > 0) {
                    regionObjList.remove(regionObjList.size() - 1);
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            ivClose.setOnClickListener(RegionDialogFragment.this);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
    }
    //endregion

    //region API
    class GetCity extends Async {
        List<RegionObj> items = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
//                int typeRegion = (int) params[0];
//                int cityId = (int) params[1];
//                int districtId = (int) params[2];
                defaultSenderDistrictID = -1;
                defaultReceiverDistrictID = -1;
                int type = 1;
                txtRegionType.setText(R.string.city);
                if (getArguments().getInt("idCity") > 0) {
                    type = 2;
                    txtRegionType.setText(R.string.district);
                }
                if (getArguments().getInt("idDistrict") > 0) {
                    type = 3;
                    items.add(0, new RegionObj(0, getString(R.string.no_ward), 0));
                    txtRegionType.setText(R.string.ward);
                }
                String value = APIHelper.getRegionAPI(type, getArguments().getInt("idCity"), getArguments().getInt("idDistrict"));
                jsonObject = new JSONObject(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonObject;

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try {
                int code = jsonObject.getInt("code");
                if (code == 1) {
                    List<RegionObj> arr = (List<RegionObj>) CmmFunc.tryParseList(jsonObject.getString("data"),
                            RegionObj.class);
                    for (RegionObj object : arr) {
                        items.add(object);
                    }

                    if (getArguments().getInt("type") == 1) {
                        RegionRecyclerAdapter regionAdapter = new RegionRecyclerAdapter(getArguments().getInt("type"), items, regionObjSenderList, RegionDialogFragment.this,
                                recyclerRegion);
                        recyclerRegion.setAdapter(regionAdapter);
                    }
                    if (getArguments().getInt("type") == 2) {
                        RegionRecyclerAdapter regionAdapter = new RegionRecyclerAdapter(getArguments().getInt("type"), items, regionObjList, RegionDialogFragment.this,
                                recyclerRegion);
                        recyclerRegion.setAdapter(regionAdapter);
                    }
                    if (getArguments().getInt("type") == 3) {
                        RegionRecyclerAdapter regionAdapter = new RegionRecyclerAdapter(getArguments().getInt("type"), items, regionAddressList, RegionDialogFragment.this,
                                recyclerRegion);
                        recyclerRegion.setAdapter(regionAdapter);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                hideProgress();
            }
        }
    }
    //endregion
}
