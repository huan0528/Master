package com.tracec.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tracec.R;
import com.tracec.data.Record;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RecordAdapter extends BaseAdapter {
    private Context context;
    private List<Record> list = new ArrayList<>();

    public RecordAdapter(Context context, List<Record> objects){
        super();
        this.context = context;
        this.list = objects;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.record_list_item, null);
        Record record = (Record)getItem(position);
        TextView tv_starttime = view.findViewById(R.id.tv_starttime);
        tv_starttime.setText(record.getStarttime());
        TextView tv_endtime = view.findViewById(R.id.tv_endtime);
        tv_endtime.setText(record.getEndtime());
        TextView tv_stay = view.findViewById(R.id.tv_stay);
        int minutes = getGapMinutes(record.getStarttime(),record.getEndtime());
        if(minutes==0){
            tv_stay.setText("--");
        }else {
            tv_stay.setText(minutes + "");
        }
        return view;
    }

    private static int getGapMinutes(String startDate, String endDate) {
        if(endDate.equals("")){
            return 0;
        }else {
            long start = 0;
            long end = 0;
            try {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                start = df.parse(startDate).getTime();
                end = df.parse(endDate).getTime();
            } catch (Exception e) {
                e.printStackTrace();
            }
            int minutes = (int) ((end - start) / (1000 * 60));
            return minutes;
        }
    }
}
