package com.example.maciej.opencvtest;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.TagLostException;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Trollo on 2017-09-10.
 */

public class NfcService /*implements NfcAdapter.OnTagRemovedListener - required API > 24*/{
    private NfcAdapter nfcAdapter;
    private Tag tag;
    private Context context;

    public NfcService(Context context){
        this.context = context;
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(context);
    }

    public boolean isNfcAvalaible(){
        return (nfcAdapter != null && nfcAdapter.isEnabled());
    }

    public void disableForegroundDispatchSystem(){
        nfcAdapter.disableForegroundDispatch((Activity)context);
    }

    public void enableForegroundDispatchSystem(){
        Intent intent = new Intent(context, context.getClass()).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pending = PendingIntent.getActivities(context, 0, new Intent[]{intent}, 0);
        IntentFilter[] intentFilters = new IntentFilter[] {};
        nfcAdapter.enableForegroundDispatch((Activity)context, pending, intentFilters, null);
    }

    public boolean setNfcTag(Intent intent) {
        if(intent.hasExtra(NfcAdapter.EXTRA_TAG)){
            tag = getTag(intent);
            return true;
        }
        return false;
    }

    public boolean writeToNfcTag(String content){
        return writeNdefMessage(content);
    }

    public String readFromNfcTag(){
        return readTextFromTag();
    }

    private Tag getTag(Intent intent) {
        return intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
    }

    private String readTextFromTag() {
        Ndef ndef = Ndef.get(tag);
        if(ndef == null) return null;
        NdefMessage ndefMessage;
        try {
            ndef.connect();
            ndefMessage = ndef.getNdefMessage();
        }
        catch(Exception ex){
            return null;
        }
        NdefRecord[] records = ndefMessage.getRecords();
        if(records != null && records.length > 0){
            NdefRecord record = records[0];
            return getTextFromNdefRecord(record);
        }
        return null;
    }

    private String getTextFromNdefRecord(NdefRecord record) {
        String tagContent = null;
        try{
            byte[] payload = record.getPayload();
            String textEncoding = "UTF-8";
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, languageSize + 1,
                    payload.length - languageSize - 1, textEncoding);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return tagContent;
    }

    private NdefMessage createNdefMessage(String content){
        NdefRecord record = NdefRecord.createTextRecord(null, content);
        return new NdefMessage(record);
    }

    private boolean writeNdefMessage( String content){
        try{
            if(tag == null){
                return false;
            }
            else{
                Ndef ndef = Ndef.get(tag);
                NdefMessage mess = createNdefMessage(content);
                if(ndef == null){
                    return false;
                }
                else{
                    ndef.connect();
                    if(!ndef.isWritable()){
                        ndef.close();
                        return false;
                    }
                    ndef.writeNdefMessage(mess);
                    ndef.close();
                    return true;
                }
            }
        }
        catch(Exception e){
            return false;
        }
    }
}
