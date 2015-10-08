package com.chen.soft.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.chen.soft.R;
import com.chen.soft.adapt.LawBean;
import com.chen.soft.adapt.LawsAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by chenchi_94 on 2015/8/30.
 */
public class FragmentLaw extends BaseFragment implements View.OnClickListener {

    private PullToRefreshListView msgList = null;
    private LawsAdapter adapter;

    private HashMap<String, String> hanzis;
    private List<LawBean> laws;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_law, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d("findMe", "Map create");
        super.onActivityCreated(savedInstanceState);
        new GetNewsTask().execute();
    }

    private void intiView(){

        msgList = (PullToRefreshListView) getView().findViewById(R.id.lawList);
        adapter = new LawsAdapter(getView().getContext(),
                laws);
        initPullToRefreshListView(msgList, adapter);
    }

    private void initPullToRefreshListView(PullToRefreshListView msgList,
                                           BaseAdapter adapter) {
        // TODO Auto-generated method stub
        Log.d("traffic", "create list");
        msgList.setMode(PullToRefreshBase.Mode.BOTH);
        msgList.setAdapter(adapter);
        //loadData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }

    }

    private void initHanziJson(){
        hanzis = new HashMap<>();
        try{
            InputStream is = getResources().openRawResource(R.raw.pinhanzi);
            byte[] buff = new byte[is.available()];
            is.read(buff);
            String json = new String(buff,"utf-8");
            JSONObject jsonObject = new JSONObject(json);
            Iterator<String> keys = jsonObject.keys();
            while(keys.hasNext()){
                String key = keys.next();
                String value = jsonObject.getString(key);
                hanzis.put(key, value);
            }
        }catch (Exception e) {
            Log.d("info", e.toString());
        }
    }

    private void initFileJson(){
        laws = new ArrayList<>();
        try{
            InputStream is = getResources().openRawResource(R.raw.pinfiles);
            byte[] buff = new byte[is.available()];
            is.read(buff);
            String json = new String(buff,"utf-8");
            JSONObject jsonObject = new JSONObject(json);
            Iterator<String> keys = jsonObject.keys();
            while(keys.hasNext()){
                List<LawBean> claws = new ArrayList<>();
                String key = keys.next();
                LawBean law = new LawBean(key, hanzis.get(key));
                String value = jsonObject.getString(key);
                JSONObject cjsonObject = new JSONObject(value);
                Iterator<String> ckeys = cjsonObject.keys();
                while(ckeys.hasNext()) {
                    String ckey = ckeys.next();
                    String cvalue = cjsonObject.getString(ckey);
                    claws.add(new LawBean(ckey, cvalue, key));
                }
                //Log.d("info", "len: " + claws.size());
                law.setLawChilds(claws);
                laws.add(law);
            }
        }catch (Exception e) {
            Log.d("info", e.toString());
        }
    }

    class GetNewsTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            initHanziJson();//
            initFileJson();
            return -1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case -1:
                    break;
                case 0:
                    Toast.makeText(getActivity(), "JSON数据解析出错", Toast.LENGTH_SHORT)
                            .show();
                    break;
            }
            intiView();
        }

    }
}
