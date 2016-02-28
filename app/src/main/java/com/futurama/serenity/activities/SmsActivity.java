package com.futurama.serenity.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.futurama.serenity.MainApplication;
import com.futurama.serenity.R;
import com.futurama.serenity.adapters.AdapterListSmsSuspect;
import com.futurama.serenity.adapters.AdapterTimeline;
import com.futurama.serenity.events.Synchronisation;
import com.futurama.serenity.models.Message;
import com.futurama.serenity.models.SmsSuspect;
import com.futurama.serenity.models.WhiteList;
import com.futurama.serenity.services.ClientService;
import com.futurama.serenity.utils.Config;
import com.futurama.serenity.utils.GenericObjectResult;
import com.futurama.serenity.utils.Session;
import com.futurama.serenity.utils.Utils;
import com.joanzapata.iconify.widget.IconButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SmsActivity extends AppCompatActivity {

    @Bind(R.id.sms_result)
    TextView tvNoResult;

    @Bind(R.id.list_sms)
    ListView lvSms;

    private AdapterListSmsSuspect adapterListSmsSuspect;
    private List<SmsSuspect> smsSuspectList = new ArrayList<>();
    private Session session;
    private Context mContext;
    Dialog smsDialog;
    boolean envoiPosition = false;
    private ProgressDialog progressDialogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        ButterKnife.bind(this);
        session = new Session();
        mContext = MainApplication.getInstance().getApplicationContext();
        initView();
    }

    private void initView(){
        smsSuspectList = SmsSuspect.getSms();
        if(smsSuspectList.size() > 0){
            adapterListSmsSuspect = new AdapterListSmsSuspect(mContext, smsSuspectList);
            lvSms.setAdapter(adapterListSmsSuspect);
            lvSms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                    smsDialog(i, id);
                }
            });
        }
        else{
            tvNoResult.setVisibility(View.VISIBLE);
        }
    }

    public void smsDialog(final int index, long idSmsSuspect){
        final SmsSuspect smsSuspect = SmsSuspect.getById((int) idSmsSuspect);
        smsDialog = new Dialog(this);
        smsDialog.setCanceledOnTouchOutside(true);
        smsDialog.setContentView(R.layout.dialog_sms);
        //appelStatutDialog.getWindow().setBackgroundDrawable(new ColorDrawableResource(R.color.transparent));
        //appelStatutDialog.getActionBar().setBackgroundDrawable();
        //appelStatutDialog.getWindow().setBackgroundDrawableResource(R.color.cardview_light_background);
        smsDialog.setTitle("Numéro suspect");
        smsDialog.show();
        RadioGroup radioGroupPosition = (RadioGroup) smsDialog.findViewById(R.id.position_group);
        final TextView textViewNumero = (TextView) smsDialog.findViewById(R.id.numero);
        IconButton btnAjouterContact = (IconButton) smsDialog.findViewById(R.id.btn_ajouter_contact);
        IconButton btnValider = (IconButton) smsDialog.findViewById(R.id.btn_valider_statut);
        textViewNumero.setText(session.getSharedPref().getString("numero", ""));
        radioGroupPosition.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioposition = radioGroup.getCheckedRadioButtonId();
                switch (radioposition) {
                    case R.id.envoyer_position:
                        envoiPosition = true;
                        break;
                    case R.id.envoyer_position_non:
                        envoiPosition = false;
                        break;
                    default:
                        envoiPosition = false;
                        break;
                }
            }
        });

        btnAjouterContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WhiteList whiteList = new WhiteList();
                whiteList.setNumero(Utils.cleanNumber(smsSuspect.getNumerosuspect()));
                whiteList.save();
                smsSuspect.delete();
                smsSuspectList.remove(index);
                if (smsSuspectList.size() == 0) {
                    tvNoResult.setVisibility(View.VISIBLE);
                }
                adapterListSmsSuspect.notifyDataSetChanged();
                smsDialog.dismiss();
            }
        });

        btnValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSms(smsSuspect, textViewNumero.getText().toString().trim());
                smsDialog.dismiss();
            }
        });
    }

    private void sendSms(SmsSuspect smsSuspect, String numero){
        session.getEditor().putString("numero", numero).commit();
        progressDialogs = ProgressDialog.show(this, null, "Envoi en cours...", false, false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialogs.dismiss();
                Utils.toast("SMS envoyé à Serenity");
            }
        }, 3000);
        Retrofit client = MainApplication.getRetrofit();
        ClientService service = client.create(ClientService.class);
        Call<GenericObjectResult<SmsSuspect>> call = service.transfererMessage(session.getSharedPref().getString("token", "*******************"), session.getSharedPref().getString("uid",""),
                session.getSharedPref().getFloat("latitude", 0), session.getSharedPref().getFloat("longitude", 0), Utils.dateToString(smsSuspect.getDatereception(), Config.DATE_TIME_PATTERN),
                smsSuspect.getNumerosuspect(), numero, smsSuspect.getMsg(), envoiPosition);
        call.enqueue(new Callback<GenericObjectResult<SmsSuspect>>() {
            @Override
            public void onResponse(Response<GenericObjectResult<SmsSuspect>> response) {
                if (response.isSuccess()) {
                    // request successful (status code 200, 201)
                    GenericObjectResult<SmsSuspect> result = response.body();
                    if (result.getTotal() > 0) {
                        //smsSuspect.
                        Log.e("Synchro", result.getRow().toString());
                    }
                    Log.i("RegistrationID", result.toString());
                } else {
                    //request not successful (like 400,401,403 etc)
                    //Handle errors
                    Log.i("Response code", String.valueOf(response.code()));
                    Log.i("Response message", response.message());
                    Log.i("Response header", response.headers().toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
