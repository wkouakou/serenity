package com.futurama.serenity.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.futurama.serenity.R;
import com.futurama.serenity.models.Message;
import com.futurama.serenity.utils.Session;
import com.futurama.serenity.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wilfried on 30/12/2015.
 */
public class AdapterTimeline extends BaseAdapter {

    private List<Message> messageList;
    LayoutInflater inflater;
    static Context mContext;
    private static Session session;
    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

    public AdapterTimeline(Context context, List<Message> listMessages){
        session = new Session();
        this.messageList = listMessages;
        this.mContext = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int i) {
        return messageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return messageList.get(i).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        MyTag holder; // Instanciation de notre enveloppe de données
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.timeline_item, null);
            holder = new MyTag(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyTag) convertView.getTag();
        }
        Message message = messageList.get(position);
        String libelle = "Alerte", couleurFont = "#008080"; //Default alerte
        switch(message.getType()){
            case 1 :
                libelle = "Alerte";
                couleurFont = "#008080";
                break;
            case 2 :
                libelle = "Campagne de sensibilisation";
                couleurFont = "#008080";
                break;
            case 3 :
                libelle = "Astuce";
                couleurFont = "#008080";
                break;
            default:
                libelle = "Alerte";
                couleurFont = "#008080";
                break;
        }

        holder.txtLibelle.setText(libelle);
        //holder.viewLibelle.setBackgroundColor(Color.parseColor(couleurFont));
        holder.txtMessage.setText(message.getMsg());
        holder.txtDate.setText("Publié le "+Utils.dateToString(message.getDateenvoi(), "dd/MM/yyyy à HH:mm:ss"));

        return convertView;
    }

    static class MyTag {

        @Bind(R.id.viewLibelle)
        View viewLibelle;

        @Bind(R.id.libelle)
        TextView txtLibelle;

        @Bind(R.id.txtMessage)
        TextView txtMessage;

        @Bind(R.id.txtDate)
        TextView txtDate;

        public MyTag(View view){
            ButterKnife.bind(this, view);
        }
    }
}
