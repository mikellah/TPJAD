package com.example.tpjad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tpjad.R;
import com.example.tpjad.model.Team;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<Team> {
    public ListAdapter(Context context, ArrayList<Team> teamArrayList){
        super(context, R.layout.list_item,teamArrayList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Team team = getItem(position);
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);

        }
        TextView teamName = convertView.findViewById(R.id.teamName);
        TextView teamStatus = convertView.findViewById(R.id.teamStatus);
        TextView teamId = convertView.findViewById(R.id.teamId);

        teamName.setText("Team: " + team.getName());
        teamStatus.setText("Current Status: "+team.getStatus());
        teamId.setText("Team Id: " +team.getId());

        return convertView;
    }
}
