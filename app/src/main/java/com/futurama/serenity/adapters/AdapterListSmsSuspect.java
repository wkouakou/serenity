package com.futurama.serenity.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.futurama.serenity.R;
import com.futurama.serenity.models.Message;
import com.futurama.serenity.models.SmsSuspect;
import com.futurama.serenity.utils.Session;
import com.futurama.serenity.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wilfried on 30/12/2015.
 */
public class AdapterListSmsSuspect extends BaseAdapter {

    private List<SmsSuspect> smsSuspectList;
    LayoutInflater inflater;
    static Context mContext;
    private static Session session;
    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

    public AdapterListSmsSuspect(Context context, List<SmsSuspect> listMessages){
        session = new Session();
        this.smsSuspectList = listMessages;
        this.mContext = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return smsSuspectList.size();
    }

    @Override
    public Object getItem(int i) {
        return smsSuspectList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return smsSuspectList.get(i).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        MyTag holder; // Instanciation de notre enveloppe de données
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_sms_item, null);
            holder = new MyTag(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyTag) convertView.getTag();
        }
        SmsSuspect smsSuspect = smsSuspectList.get(position);

        holder.txtExpediteur.setText(smsSuspect.getNumerosuspect());
        holder.txtMessage.setText(smsSuspect.getMsg());
        holder.txtDate.setText("Réçu le "+Utils.dateToString(smsSuspect.getDatereception(), "dd/MM/yyyy à HH:mm:ss"));
        Log.e("SmsSuspect", smsSuspect.toString());
        return convertView;
    }

    static class MyTag {

        @Bind(R.id.viewExpediteur)
        View viewExpediteur;

        @Bind(R.id.expediteur)
        TextView txtExpediteur;

        @Bind(R.id.txtSms)
        TextView txtMessage;

        @Bind(R.id.txtDateReception)
        TextView txtDate;

        public MyTag(View view){
            ButterKnife.bind(this, view);
        }
    }
}
