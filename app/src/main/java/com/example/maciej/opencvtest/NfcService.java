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
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

/**
 * Created by Trollo on 2017-09-10.
 */

public class NfcService {
    private NfcAdapter nfcAdapter;
    private Context mainContext;

    public NfcService(Context context){
        this.mainContext = context;
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(mainContext);
        if(nfcAdapter != null && nfcAdapter.isEnabled()){
            Toast.makeText(mainContext, "NFC available!", Toast.LENGTH_LONG).show();
        }//TO DO print explicit on interface that app won't work without nfc
        else{
            Toast.makeText(mainContext, "NFC not available!", Toast.LENGTH_LONG).show();
        }
    }

    public void disableForegroundDispatchSystem(){
        nfcAdapter.disableForegroundDispatch((Activity)mainContext);
    }

    public void enableForegroundDispatchSystem(){
        Intent intent = new Intent(mainContext, PhotoModeActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pending = PendingIntent.getActivities(mainContext, 0, new Intent[]{intent}, 0);
        IntentFilter[] intentFilters = new IntentFilter[] {};
        nfcAdapter.enableForegroundDispatch((Activity)mainContext, pending, intentFilters, null);
    }

    public void onNewIntent(Intent intent) {
        if(intent.hasExtra(nfcAdapter.EXTRA_TAG)){
            Parcelable[] parcelables = intent.getParcelableArrayExtra(nfcAdapter.EXTRA_NDEF_MESSAGES);
            if(parcelables != null && parcelables.length > 0) {
                readTextFromMessage((NdefMessage)parcelables[0]);
            }
            else{
                Toast.makeText(mainContext, "No ndfef messages", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void readTextFromMessage(NdefMessage mess) {
        NdefRecord[] records = mess.getRecords();
        if(records != null){
            NdefRecord record = records[0];
            String tagContent = getTextFromNdefRecord(record);
            Toast.makeText(mainContext, tagContent, Toast.LENGTH_SHORT).show();
        }
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

    private void formatTag(Tag tag, NdefMessage mess){
        try{
            NdefFormatable ndef = NdefFormatable.get(tag);
            if(ndef == null){
                Toast.makeText(mainContext, "Tag is not ndef formattable", Toast.LENGTH_SHORT).show();

            }
            else{
                ndef.connect();
                ndef.format(mess);
                ndef.close();
            }
        }
        catch(Exception e){
            Log.e("formatTag", e.getMessage());
        }
    }

    private NdefMessage createNdefMessage(String content){
        NdefRecord record = NdefRecord.createTextRecord(null, content);//tagTextField.getText().toString());
        NdefMessage mess = new NdefMessage(new NdefRecord[]{record});
        return mess;
    }

    private void writeNdefMessage(Tag tag, NdefMessage mess){
        try{
            if(tag == null){
                Toast.makeText(mainContext, "Tag object cannot be null", Toast.LENGTH_SHORT).show();
            }
            else{
                Ndef ndef = Ndef.get(tag);
                if(ndef == null){
                    formatTag(tag, mess);
                }
                else{
                    ndef.connect();
                    if(!ndef.isWritable()){
                        Toast.makeText(mainContext, "Tag is not writable", Toast.LENGTH_SHORT).show();

                        ndef.close();
                        return;
                    }
                    ndef.writeNdefMessage(mess);
                    ndef.close();
                    Toast.makeText(mainContext, "Tag written!", Toast.LENGTH_SHORT).show();

                }

            }
        }
        catch(Exception e){
            Log.e("formatTag", e.getMessage());
        }
    }
}
