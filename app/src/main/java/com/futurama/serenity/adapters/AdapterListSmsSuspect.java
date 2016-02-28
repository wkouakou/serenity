package com.futurama.serenity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.futurama.serenity.R;
import com.futurama.serenity.models.SmsSuspect;
import com.futurama.serenity.utils.Session;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by wilfried on 30/12/2015.
 */
public class AdapterListSmsSuspect extends BaseAdapter {

    private List<SmsSuspect> smsSuspectList;
    LayoutInflater inflater;
    static Context mContext;
    private static Session session;
    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

    public AdapterListSmsSuspect(Context context, List<SmsSuspect> smsSuspectList){
        session = new Session();
        this.smsSuspectList = smsSuspectList;
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
            convertView = inflater.inflate(R.layout.timeline_item, null);
            //holder = new MyTag(convertView);
            //convertView.setTag(holder);
        } else {
            holder = (MyTag) convertView.getTag();
        }

        /*StringBuilder detailNumero = new StringBuilder();
        StringBuilder detailAppel = new StringBuilder();
        detailNumero.append(contactAttrib.getContact().getCel());

        if(contactAttrib.getTentative() > 0){
            detailNumero.append(" / ");
            detailNumero.append(contactAttrib.getTentative());
            detailNumero.append(" tent.");
        }

        if(contactAttrib.getDateAppel() != null){
            detailAppel.append(Utils.dateToString(contactAttrib.getDateAppel(), "dd/MM/yyyy"));
        }

        if(!contactAttrib.getDuree().isEmpty() && contactAttrib.getDuree() != "0" && contactAttrib.getDuree() != null){
            detailAppel.append(" - ");
            try {
                detailAppel.append(df.format(new Date(Integer.valueOf(contactAttrib.getDuree())*1000L)));
            }
            catch (Exception ex){
                Log.e("ErreurDateFormat", ex.getMessage());
                detailAppel.append(contactAttrib.getDuree());
            }
        }

        holder.txtNom.setText(contactAttrib.getContact().getName());
        holder.txtContact.setText(detailNumero.toString());
        holder.txtDetail.setText(detailAppel.toString());
        holder.contactAttrib = contactAttrib;
        holder.choixNumero.setChecked(true);*/

        return convertView;
    }

    static class MyTag {

        /*@Bind(R.id.sms_nom)
        TextView txtNom;

        @Bind(R.id.sms_contact)
        TextView txtContact;

        @Bind(R.id.sms_detail)
        TextView txtDetail;

        @Bind(R.id.sms_choix)
        CheckBox choixNumero;

        ContactAttrib contactAttrib;

        public MyTag(View view){
            ButterKnife.bind(this, view);
        }*/
    }
}
