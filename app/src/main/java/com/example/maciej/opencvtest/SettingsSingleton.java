package com.example.maciej.opencvtest;

import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.maciej.opencvtest.CardSuit.Clubs;
import static com.example.maciej.opencvtest.CardSuit.Diamonds;
import static com.example.maciej.opencvtest.CardSuit.Hearts;
import static com.example.maciej.opencvtest.CardSuit.Spades;

/**
 * Created by Trollo on 2017-10-31.
 */

public class SettingsSingleton {
    private static SettingsSingleton ourInstance = new SettingsSingleton();

    static{
        ourInstance = new SettingsSingleton();
        ourInstance.init();
    }

    private File applicationDirectory;
    private File settingsFile;

    private String trickMode;
    private String readingTagMode;
    private long vibrationInterval;
    private long noVibrationInterval;
    private long endOfBitInterval;

    private List<CardSettingsHeader> cardSettingsHeaders;
    private HashMap<CardSettingsHeader, List<CardSettingsItem>> cardSettingsItems;

    private boolean applicationDirectoryIsAvalaible(){
        applicationDirectory = new File(Environment.getExternalStorageDirectory() +
                File.separator + Technologic.getContext().getString(R.string.app_name));
        if (!applicationDirectory.exists()) {
            return applicationDirectory.mkdirs();
        }
        return true;
    }

    private void createNewSettingsFile(){
        if(settingsFile.exists()){
            settingsFile.delete();
        }
        try {
            settingsFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SettingsSingleton(){}

    private void init(){
        cardSettingsHeaders = new ArrayList<>();
        cardSettingsItems = new HashMap<>();
        if(applicationDirectoryIsAvalaible()){
            settingsFile = new File(applicationDirectory + File.separator + Technologic.getContext().getString(R.string.settingsFileName));
            createBaseSettings();
            try {
                //saveSettings();
                loadSettings();
            }
            catch(Exception e){
                Log.d("json exception", e.getMessage());
            }
        }
    }

    private void createBaseSettings() {
        List<String> myResArrayList = Arrays.asList(Technologic.getContext().getResources().getStringArray(R.array.cardSuits));
        for (String title: myResArrayList) {
            switch(CardSuit.valueOf(title)){
                case Hearts:
                    CardSettingsHeader header1 = new CardSettingsHeader(R.raw.hearts_icon, Hearts);
                    cardSettingsHeaders.add(header1);
                    cardSettingsItems.put(header1, getChildsForHeader(Hearts));
                    break;
                case Clubs:
                    CardSettingsHeader header2 = new CardSettingsHeader(R.raw.clubs_icon, Clubs);
                    cardSettingsHeaders.add(header2);
                    cardSettingsItems.put(header2, getChildsForHeader(Clubs));
                    break;
                case Spades:
                    CardSettingsHeader header3 = new CardSettingsHeader(R.raw.spades_icon, Spades);
                    cardSettingsHeaders.add(header3);
                    cardSettingsItems.put(header3, getChildsForHeader(Spades));
                    break;
                case Diamonds:
                    CardSettingsHeader header4 =  new CardSettingsHeader(R.raw.diamonds_icon, Diamonds);
                    cardSettingsHeaders.add(header4);
                    cardSettingsItems.put(header4, getChildsForHeader(Diamonds));
                    break;
            }
        }
    }

    private List<CardSettingsItem> getChildsForHeader(CardSuit suit) {
        List<CardSettingsItem> itemList = new ArrayList<>();
        boolean[] vibrationPattern;
        for(CardValue v: CardValue.values()){
            //for(CardSuit s: CardSuit.values()){
                vibrationPattern = new boolean[6];
                Card card = new Card(suit,v);
                switch(suit){
                    case Diamonds:
                        vibrationPattern[0] = true;
                        vibrationPattern[1] = true;
                        break;
                    case Hearts:
                        vibrationPattern[0] = false;
                        vibrationPattern[1] = false;
                        break;
                    case Spades:
                        vibrationPattern[0] = true;
                        vibrationPattern[1] = false;
                        break;
                    case Clubs:
                        vibrationPattern[0] = false;
                        vibrationPattern[1] = true;
                        break;
                }
                switch(v){
                    case Ace:
                        vibrationPattern[2] = false;
                        vibrationPattern[3] = false;
                        vibrationPattern[4] = false;
                        vibrationPattern[5] = true;
                        switch(suit){
                            case Diamonds:
                                itemList.add(new CardSettingsItem(R.raw.ace_of_diamonds, card.toString(), vibrationPattern, card));
                                break;
                            case Hearts:
                                itemList.add(new CardSettingsItem(R.raw.ace_of_hearts, card.toString(), vibrationPattern, card));
                                break;
                            case Spades:
                                itemList.add(new CardSettingsItem(R.raw.ace_of_spades, card.toString(), vibrationPattern, card));
                                break;
                            case Clubs:
                                itemList.add(new CardSettingsItem(R.raw.ace_of_clubs, card.toString(), vibrationPattern, card));
                                break;
                        }
                        break;
                    case Two:
                        vibrationPattern[2] = false;
                        vibrationPattern[3] = false;
                        vibrationPattern[4] = true;
                        vibrationPattern[5] = false;
                        switch(suit){
                            case Diamonds:
                                itemList.add(new CardSettingsItem(R.raw.two_of_diamonds, card.toString(), vibrationPattern, card));
                                break;
                            case Hearts:
                                itemList.add(new CardSettingsItem(R.raw.two_of_hearts, card.toString(), vibrationPattern, card));
                                break;
                            case Spades:
                                itemList.add(new CardSettingsItem(R.raw.two_of_spades, card.toString(), vibrationPattern, card));
                                break;
                            case Clubs:
                                itemList.add(new CardSettingsItem(R.raw.two_of_clubs, card.toString(), vibrationPattern, card));
                                break;
                        }
                        break;
                    case Three:
                        vibrationPattern[2] = false;
                        vibrationPattern[3] = false;
                        vibrationPattern[4] = true;
                        vibrationPattern[5] = true;
                        switch(suit){
                            case Diamonds:
                                itemList.add(new CardSettingsItem(R.raw.three_of_diamonds, card.toString(), vibrationPattern, card));
                                break;
                            case Hearts:
                                itemList.add(new CardSettingsItem(R.raw.three_of_hearts, card.toString(), vibrationPattern, card));
                                break;
                            case Spades:
                                itemList.add(new CardSettingsItem(R.raw.three_of_spades, card.toString(), vibrationPattern, card));
                                break;
                            case Clubs:
                                itemList.add(new CardSettingsItem(R.raw.three_of_clubs, card.toString(), vibrationPattern, card));
                                break;
                        }
                        break;
                    case Four:
                        vibrationPattern[2] = false;
                        vibrationPattern[3] = true;
                        vibrationPattern[4] = false;
                        vibrationPattern[5] = false;
                        switch(suit){
                            case Diamonds:
                                itemList.add(new CardSettingsItem(R.raw.four_of_diamonds, card.toString(), vibrationPattern, card));
                                break;
                            case Hearts:
                                itemList.add(new CardSettingsItem(R.raw.four_of_hearts, card.toString(), vibrationPattern, card));
                                break;
                            case Spades:
                                itemList.add(new CardSettingsItem(R.raw.four_of_spades, card.toString(), vibrationPattern, card));
                                break;
                            case Clubs:
                                itemList.add(new CardSettingsItem(R.raw.four_of_clubs, card.toString(), vibrationPattern, card));
                                break;
                        }
                        break;
                    case Five:
                        vibrationPattern[2] = false;
                        vibrationPattern[3] = true;
                        vibrationPattern[4] = false;
                        vibrationPattern[5] = true;
                        switch(suit){
                            case Diamonds:
                                itemList.add(new CardSettingsItem(R.raw.five_of_diamonds, card.toString(), vibrationPattern, card));
                                break;
                            case Hearts:
                                itemList.add(new CardSettingsItem(R.raw.five_of_hearts, card.toString(), vibrationPattern, card));
                                break;
                            case Spades:
                                itemList.add(new CardSettingsItem(R.raw.five_of_spades, card.toString(), vibrationPattern, card));
                                break;
                            case Clubs:
                                itemList.add(new CardSettingsItem(R.raw.five_of_clubs, card.toString(), vibrationPattern, card));
                                break;
                        }
                        break;
                    case Six:
                        vibrationPattern[2] = false;
                        vibrationPattern[3] = true;
                        vibrationPattern[4] = true;
                        vibrationPattern[5] = false;
                        switch(suit){
                            case Diamonds:
                                itemList.add(new CardSettingsItem(R.raw.six_of_diamonds, card.toString(), vibrationPattern, card));
                                break;
                            case Hearts:
                                itemList.add(new CardSettingsItem(R.raw.six_of_hearts, card.toString(), vibrationPattern, card));
                                break;
                            case Spades:
                                itemList.add(new CardSettingsItem(R.raw.six_of_spades, card.toString(), vibrationPattern, card));
                                break;
                            case Clubs:
                                itemList.add(new CardSettingsItem(R.raw.six_of_clubs, card.toString(), vibrationPattern, card));
                                break;
                        }
                        break;
                    case Seven:
                        vibrationPattern[2] = false;
                        vibrationPattern[3] = true;
                        vibrationPattern[4] = true;
                        vibrationPattern[5] = true;
                        switch(suit){
                            case Diamonds:
                                itemList.add(new CardSettingsItem(R.raw.seven_of_diamonds, card.toString(), vibrationPattern, card));
                                break;
                            case Hearts:
                                itemList.add(new CardSettingsItem(R.raw.seven_of_hearts, card.toString(), vibrationPattern, card));
                                break;
                            case Spades:
                                itemList.add(new CardSettingsItem(R.raw.seven_of_spades, card.toString(), vibrationPattern, card));
                                break;
                            case Clubs:
                                itemList.add(new CardSettingsItem(R.raw.seven_of_clubs, card.toString(), vibrationPattern, card));
                                break;
                        }
                        break;
                    case Eight:
                        vibrationPattern[2] = true;
                        vibrationPattern[3] = false;
                        vibrationPattern[4] = false;
                        vibrationPattern[5] = false;
                        switch(suit){
                            case Diamonds:
                                itemList.add(new CardSettingsItem(R.raw.eight_of_diamonds, card.toString(), vibrationPattern, card));
                                break;
                            case Hearts:
                                itemList.add(new CardSettingsItem(R.raw.eight_of_hearts, card.toString(), vibrationPattern, card));
                                break;
                            case Spades:
                                itemList.add(new CardSettingsItem(R.raw.eight_of_spades, card.toString(), vibrationPattern, card));
                                break;
                            case Clubs:
                                itemList.add(new CardSettingsItem(R.raw.eight_of_clubs, card.toString(), vibrationPattern, card));
                                break;
                        }
                        break;
                    case Nine:
                        vibrationPattern[2] = true;
                        vibrationPattern[3] = false;
                        vibrationPattern[4] = false;
                        vibrationPattern[5] = true;
                        switch(suit){
                            case Diamonds:
                                itemList.add(new CardSettingsItem(R.raw.nine_of_diamonds, card.toString(), vibrationPattern, card));
                                break;
                            case Hearts:
                                itemList.add(new CardSettingsItem(R.raw.nine_of_hearts, card.toString(), vibrationPattern, card));
                                break;
                            case Spades:
                                itemList.add(new CardSettingsItem(R.raw.nine_of_spades, card.toString(), vibrationPattern, card));
                                break;
                            case Clubs:
                                itemList.add(new CardSettingsItem(R.raw.nine_of_clubs, card.toString(), vibrationPattern, card));
                                break;
                        }
                        break;
                    case Ten:
                        vibrationPattern[2] = true;
                        vibrationPattern[3] = false;
                        vibrationPattern[4] = true;
                        vibrationPattern[5] = false;
                        switch(suit){
                            case Diamonds:
                                itemList.add(new CardSettingsItem(R.raw.ten_of_diamonds, card.toString(), vibrationPattern, card));
                                break;
                            case Hearts:
                                itemList.add(new CardSettingsItem(R.raw.ten_of_hearts, card.toString(), vibrationPattern, card));
                                break;
                            case Spades:
                                itemList.add(new CardSettingsItem(R.raw.ten_of_spades, card.toString(), vibrationPattern, card));
                                break;
                            case Clubs:
                                itemList.add(new CardSettingsItem(R.raw.ten_of_clubs, card.toString(), vibrationPattern, card));
                                break;
                        }
                        break;
                    case Jack:
                        vibrationPattern[2] = true;
                        vibrationPattern[3] = false;
                        vibrationPattern[4] = true;
                        vibrationPattern[5] = true;
                        switch(suit){
                            case Diamonds:
                                itemList.add(new CardSettingsItem(R.raw.jack_of_diamonds, card.toString(), vibrationPattern, card));
                                break;
                            case Hearts:
                                itemList.add(new CardSettingsItem(R.raw.jack_of_hearts, card.toString(), vibrationPattern, card));
                                break;
                            case Spades:
                                itemList.add(new CardSettingsItem(R.raw.jack_of_spades, card.toString(), vibrationPattern, card));
                                break;
                            case Clubs:
                                itemList.add(new CardSettingsItem(R.raw.jack_of_clubs, card.toString(), vibrationPattern, card));
                                break;
                        }
                        break;
                    case Queen:
                        vibrationPattern[2] = true;
                        vibrationPattern[3] = true;
                        vibrationPattern[4] = false;
                        vibrationPattern[5] = false;
                        switch(suit){
                            case Diamonds:
                                itemList.add(new CardSettingsItem(R.raw.queen_of_diamonds, card.toString(), vibrationPattern, card));
                                break;
                            case Hearts:
                                itemList.add(new CardSettingsItem(R.raw.queen_of_hearts, card.toString(), vibrationPattern, card));
                                break;
                            case Spades:
                                itemList.add(new CardSettingsItem(R.raw.queen_of_spades, card.toString(), vibrationPattern, card));
                                break;
                            case Clubs:
                                itemList.add(new CardSettingsItem(R.raw.queen_of_clubs, card.toString(), vibrationPattern, card));
                                break;
                        }
                        break;
                    case King:
                        vibrationPattern[2] = true;
                        vibrationPattern[3] = true;
                        vibrationPattern[4] = false;
                        vibrationPattern[5] = true;
                        switch(suit){
                            case Diamonds:
                                itemList.add(new CardSettingsItem(R.raw.king_of_diamonds, card.toString(), vibrationPattern, card));
                                break;
                            case Hearts:
                                itemList.add(new CardSettingsItem(R.raw.king_of_hearts, card.toString(), vibrationPattern, card));
                                break;
                            case Spades:
                                itemList.add(new CardSettingsItem(R.raw.king_of_spades, card.toString(), vibrationPattern, card));
                                break;
                            case Clubs:
                                itemList.add(new CardSettingsItem(R.raw.king_of_clubs, card.toString(), vibrationPattern, card));
                                break;
                        }
                        break;
                }
            }

        return itemList;
    }

    private String getSettingsFileJSONData() throws IOException {
        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(settingsFile)));
        String content = "";
        String line;
        while ((line = reader.readLine()) != null)
        {
            content = content + line;
        }
        return content;
    }

    private List<CardSettingsItem> loadItemsForHeader(CardSettingsHeader header) throws ParseException, JSONException, IOException {
        JSONArray settingsData = new JSONArray(getSettingsFileJSONData());
        List<CardSettingsItem> groupList = new ArrayList<>();

        for(int i = 0; i < settingsData.length(); i++){
            groupList.clear();
            JSONObject settingsGroup = settingsData.getJSONObject(i);
            JSONArray settingsItems;
            try {
                settingsItems = settingsGroup.getJSONArray(header.getHeaderNameText());
            }
            catch(Exception e){
                continue;
            }
            for(int j = 0; j < settingsItems.length(); j++) {
                JSONObject settingsItem = settingsItems.getJSONObject(j);
                int iconId = settingsItem.getInt(Technologic.getContext().getString(R.string.jsonItemIconId));
                String name = settingsItem.getString(Technologic.getContext().getString(R.string.jsonItemName));
                boolean[] vibrationPattern = new boolean[6];
                JSONArray vibrationPatternJSON = settingsItem.getJSONArray(Technologic.getContext().getString(R.string.jsonItemVibrationPattern));
                for (int k = 0; k < vibrationPatternJSON.length(); k++) {
                    vibrationPattern[k] = vibrationPatternJSON.getBoolean(k);
                }
                String cardName = settingsItem.getString(Technologic.getContext().getString(R.string.jsonItemCardId));
                String cardSuit = cardName.split(" of ")[1];
                String cardValue = cardName.split(" of ")[0];
                Card cardId = new Card(CardSuit.valueOf(cardSuit), CardValue.valueOf(cardValue));
                CardSettingsItem cardSettingsItem = new CardSettingsItem(iconId, name, vibrationPattern, cardId);
                groupList.add(cardSettingsItem);
            }
            break;
        }
        return groupList;
    }

    private void loadHeaders() throws ParseException, IOException, JSONException {
        for (CardSuit suit: CardSuit.values()) {
            CardSettingsHeader header = new CardSettingsHeader(0, suit);
            switch(suit){
                case Hearts:
                    header = new CardSettingsHeader(R.raw.hearts_icon, Hearts);
                    break;
                case Clubs:
                    header = new CardSettingsHeader(R.raw.clubs_icon, Clubs);
                    break;
                case Spades:
                    header = new CardSettingsHeader(R.raw.spades_icon, Spades);
                    break;
                case Diamonds:
                    header = new CardSettingsHeader(R.raw.diamonds_icon, Diamonds);
                    break;
            }
            cardSettingsHeaders.add(header);
            cardSettingsItems.put(header, loadItemsForHeader(header));
        }
    }

    public void saveSettings() throws JSONException, IOException {
        createNewSettingsFile();
        JSONArray settingsData = new JSONArray();
        JSONObject settingsGroup;
        JSONArray settingsItems;
        JSONObject settingsItem;
        JSONArray vibrationPattern;

        for (Map.Entry<CardSettingsHeader, List<CardSettingsItem>> entry: cardSettingsItems.entrySet()) {
            settingsGroup = new JSONObject();
            settingsItems = new JSONArray();
            for(CardSettingsItem item: entry.getValue()){
                settingsItem = new JSONObject();
                settingsItem.put(Technologic.getContext().getString(R.string.jsonItemCardId), item.getId());
                settingsItem.put(Technologic.getContext().getString(R.string.jsonItemIconId), item.getItemIconResourceId());
                settingsItem.put(Technologic.getContext().getString(R.string.jsonItemName), item.getItemName());
                vibrationPattern = new JSONArray();
                for(int i = 0; i < item.getVibrationPattern().length; i++) {
                    vibrationPattern.put(item.getVibrationPattern()[i]);
                }
                settingsItem.put(Technologic.getContext().getString(R.string.jsonItemVibrationPattern), vibrationPattern);
                settingsItems.put(settingsItem);
            }
            settingsGroup.put(entry.getKey().getHeaderNameText(), settingsItems);
            settingsData.put(settingsGroup);
        }
        FileOutputStream fos = new FileOutputStream(settingsFile);
        fos.write(settingsData.toString().getBytes());
        fos.close();
    }

    public void loadSettings() throws ParseException, JSONException, IOException {
        cardSettingsHeaders.clear();
        cardSettingsItems.clear();
        loadHeaders();
    }

    public static SettingsSingleton getInstance(){
        return ourInstance;
    }

    public String getTrickMode(){
        return trickMode;
    }

    public void setTrickMode(String trickMode){
        this.trickMode = trickMode;
    }

    public boolean isReadingTagModeAutomatic() {
        return readingTagMode.equals("Automatic");
    }

    public void setReadingTagMode(String readingTagMode) {
        this.readingTagMode = readingTagMode;
    }

    public long getVibrationInterval() {
        return vibrationInterval;
    }

    public void setVibrationInterval(long vibrationInterval) {
        this.vibrationInterval = vibrationInterval;
    }

    public long getNoVibrationInterval() {
        return noVibrationInterval;
    }

    public void setNoVibrationInterval(long noVibrationInterval) {
        this.noVibrationInterval = noVibrationInterval;
    }

    public long getEndOfBitInterval() {
        return endOfBitInterval;
    }

    public void setEndOfBitInterval(long endOfBitInterval) {
        this.endOfBitInterval = endOfBitInterval;
    }

    public File getApplicationDirectory() {
        return applicationDirectory;
    }

    public File getSettingsFile() {
        return settingsFile;
    }

    public List<CardSettingsHeader> getCardSettingsHeaders() {
        return cardSettingsHeaders;
    }

    public HashMap<CardSettingsHeader, List<CardSettingsItem>> getCardSettingsItems() {
        return cardSettingsItems;
    }

    public void setCardSettingsItems(HashMap<CardSettingsHeader, List<CardSettingsItem>> cardSettingsItems) {
        this.cardSettingsItems = cardSettingsItems;
    }

    public boolean[] getVibrationPatternOfCardName(String name){
        for (Map.Entry<CardSettingsHeader, List<CardSettingsItem>> entry: cardSettingsItems.entrySet()) {
            for(CardSettingsItem item: entry.getValue()) {
                if(item.getItemName().equalsIgnoreCase(name)){
                    return item.getVibrationPattern();
                }
            }
        }
        return new boolean[6];
    }
}
