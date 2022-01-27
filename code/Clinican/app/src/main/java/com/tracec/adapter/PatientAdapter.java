package com.tracec.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.tracec.R;
import com.tracec.data.Patient;

import java.util.ArrayList;
import java.util.List;

public class PatientAdapter extends BaseAdapter{
    private Context context;
    private List<Patient> list = new ArrayList<Patient>();

    public PatientAdapter(Context context, List<Patient> objects){
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
        view = inflater.inflate(R.layout.patient_list_item, null);
        Patient patient = (Patient)getItem(position);
        TextView tv_patientid=view.findViewById(R.id.tv_patientid);
        tv_patientid.setText("PatientID: "+patient.getPatientid());

        TextView tv_surname = view.findViewById(R.id.tv_surname);
        tv_surname.setText("SurName: "+patient.getSurname());

        TextView tv_givenname = view.findViewById(R.id.tv_givenname);
        tv_givenname.setText("GivenName: "+patient.getGivenname());

        TextView tv_birth = view.findViewById(R.id.tv_birth);
        tv_birth.setText("DateOfBirth: "+patient.getBirth());

        return view;
    }
}
