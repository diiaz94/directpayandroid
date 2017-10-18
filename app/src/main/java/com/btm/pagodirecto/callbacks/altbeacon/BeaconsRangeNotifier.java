package com.btm.pagodirecto.callbacks.altbeacon;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.btm.pagodirecto.services.ServiceGenerator;
import com.google.gson.Gson;
import com.btm.pagodirecto.domain.beacons.Beacons;
import com.btm.pagodirecto.util.Util;
import com.btm.pagodirecto.R;
import com.btm.pagodirecto.activities.baseActivities.BeaconScanner;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.androidannotations.annotations.EBean;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by ealvarado on 04/06/17.
 */

@EBean(scope = EBean.Scope.Singleton)
public class BeaconsRangeNotifier implements RangeNotifier {

    private static final String TAG = "BeaconsRangeNotifier";
    private static final double MAX_REGION_DISTANCE = 3;
    private Beacons currentBeacon;

    private List<Beacons> beaconList = new ArrayList<Beacons>();

    JSONObject myNotificationObject = new JSONObject();

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beaconsCollection, Region region) {
        // Look for listPromotions detected in this single scan
        Log.d("beacons", String.valueOf(beaconsCollection.size()));
        try {
            for (Beacon beacon : beaconsCollection) {
                //Log.d("beacon info:: name::", String.valueOf(beacon.getParserIdentifier()) + " :: Distance:: " + String.valueOf(beacon.getDistance()));
                currentBeacon = getBeaconFromList(beacon, beaconList);

                if (currentBeacon != null) {
                    beaconSendingLogic(currentBeacon);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            Util.sendEmail(e, "didRangeBeaconsInRegion");
        }
    }

    private void  beaconSendingLogic(Beacons beacon){
        //Do something with current beacon
        try {
            Log.d("beacon distance:: ", "idBeacon: "+String.valueOf(beacon.getBeaconId())+ " :: " +String.valueOf(beacon.getAccurateDistance()));
            if (beacon.getDistance() <= MAX_REGION_DISTANCE) {  // IN Region < 3 m
                Log.d("beacons", "YES REGISTERED");
                beacon.setInStatus();
            }else{ // OUT Region > 3 m
                if(beacon.getInsideRegion()) {
                    beacon.setOutStatus();
                    beacon.setArrive(false);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            Util.sendEmail(e, "beaconSendingLogicByApiRest");
        }
    }

    private Beacons getBeaconFromList(Beacon beacon, List<Beacons> list){
        int beaconIndex = 0;
        try {
            beaconIndex = getBeaconIndexFromList(beacon, beaconList);
            // If not in List, then add it to the ListPromotion List
            if (beaconIndex == -1) {
                beaconList.add(new Beacons());
                beaconIndex = beaconList.size() - 1;
            }
            // Get the beacon from the List and update the values

            if(beaconIndex >= 0) {
                return beaconList.get(beaconIndex).updateBeaconValues(beacon);
            }
            return null;
        }catch (Exception e) {
            e.printStackTrace();
            Util.sendEmail(e, "getBeaconFromList");
            return null;
        }
    }

    private int getBeaconIndexFromList(Beacon beacon, List<Beacons> list){
        String idBeacon = "";
        int index = 0;
        try {
            for (int i = 0; i < beaconList.size(); i++) {
                idBeacon =  beacon.getId1().toString().substring(2) + beacon.getId2().toString().substring(2);
                if(idBeacon !=  null) {
                    index = i;
                    if (beaconList.get(i).getUniqueId().equalsIgnoreCase(idBeacon)) {
                        return i;
                    }
                }
            }
            return -1;
        }catch (Exception e) {
            e.printStackTrace();
            Gson gson = new Gson();
            String jsonBeacon = gson.toJson(beacon);
            String jsonBeaconList = gson.toJson(beaconList);

            String msgModule = "getBeaconIndexFromList:: "+ idBeacon +
                    ":: beaconList::  " + index + " :: " + jsonBeaconList +
                    " :: Beacon String:: " + jsonBeacon;
            Util.sendEmail(e, msgModule);
            return -2;
        }
    }

    private void showDialogMessage(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(Util.getContext());
        builder.setTitle("Functionality limited");
        builder.setMessage("Since location access has not been granted, this app will not be able to discover listPromotions when in the background.");
        builder.show();
    }

    /*
    @Override
    public void didEnterRegion(Region region) {
        Log.d(TAG, "entered region.  starting ranging");
        setRegionsAtBoot();
        mBeaconManager.setRangeNotifier(this);
    }

    @Override
    public void didExitRegion(Region region) {
        // do nothing. Ranging should work further
    }

    // Set regions to monitor at boot/startup
    private void setRegionsAtBoot(){

        for (Iterator<Beacon> iterator = beacons.iterator(); iterator.hasNext();) {

            try {
                mBeaconManager.startRangingBeaconsInRegion(new Region(simpleBeacon.getId(), listB));
            } catch (RemoteException e) {
            }
        }
    }
    */

    /*/Search in list of notifications and show after some time
    private boolean continueExecute(BeaconInfo beacon) {
        String promotionId = String.valueOf(beacon.getPromotions().get(0).getId());

        Calendar c = Calendar.getInstance();
        Log.i("continueExecute::","Current time:: " + c.getTime());

        long myCurrentPromotionTime;

        try {
            if (myNotificationObject.isNull(promotionId)){
                myNotificationObject.put(promotionId, c.getTimeInMillis());
                return true;
            }else{
                myCurrentPromotionTime = myNotificationObject.getLong(promotionId);
                Long intervalTime = c.getTimeInMillis() - myCurrentPromotionTime;
                if(intervalTime > Util.getTimeForNotification()){
                    myNotificationObject.put(promotionId, c.getTimeInMillis());
                    return true;
                }else{
                    return false;
                }
            }
        }catch (JSONException j){
            Log.e("continueExecute:: ",j.getMessage());
            return true;
        }
    }

    private void sendBeaconMessage(Beacons beacon){
        ServiceGenerator
            .getService(BeaconsService.class)
                .sendBeaconsMessage(beaconMessage.update(beacon))
                .enqueue(beaconsMessageCallback);
    }

    private BeaconInfo getBeaconInfo(Beacons beacon){
        Response<BeaconInfo> response = null;
        Call<BeaconInfo> call = ServiceGenerator
                .getService(BeaconsService.class)
                .getBeaconInfo(beacon.getProviderId(),beacon.getBeaconId());
        try {
            response = call.execute();
            Log.d("beacons", String.valueOf(response.code()));
            if(response.isSuccessful()) {
                return response.body();
            }
        } catch (IOException e) {
            //TODO: FAILED ROUTINE
        }
        return null;
        }

    private boolean isRegistered(BeaconInfo beaconInfo){
        if(beaconInfo!=null){
            return true;
        }
        return false;
    }

    private boolean isRegistered(Beacons beacon) {
        Response<BeaconInfo> response = null;
        Call<BeaconInfo> call = ServiceGenerator
                            .getService(BeaconsService.class)
                            .getBeaconInfo(beacon.getProviderId(),beacon.getBeaconId());
        try {
            response = call.execute();
            Log.d("beacons", String.valueOf(response.code()));
            if(response.isSuccessful()){
                BeaconInfo beaconInfo  = response.body();
                if(beaconInfo.isActivated()){
                    Log.d("beacons","IS REGISTERED! " + beaconInfo.getId());
                    currentBeacon.setId(beaconInfo.getId());
                    return true;

                }
                else {
                    return false;
                }
            }
            else {
                //TODO: Error routine
                return false;
            }

        } catch (IOException e) {
            //TODO: FAILED ROUTINE

        }
        return false;
    }

    private void getBeaconPromotions(BeaconInfo beaconInfo, Beacons beacon){

        try {

            List<Promotion> beaconPromotions = beaconInfo.getPromotions();
            if (beaconPromotions.size() > 0) {
                //if (continueExecute(beaconInfo)) { UnComment to include timer
                if (beacon.getInsideRegion()) {
                    if (!beacon.isArrive()) {
                        sendBeaconMessage(beacon);
                        beacon.setArrive(true);
                        //if (continueExecute(beaconInfo)) {
                        for (Promotion promotion : beaconPromotions) {
                            sendNotification(promotion);
                            try {
                                if (((BeaconScanner) Util.getActivity()).isVisible()) {
                                    Log.d("beacons", "VISIBLE");
                                    if (Util.getActivity() instanceof MainCustomer) {
                                        Log.d("getBeaconPromotions::", " Show Promotion, beaconInfo: " + beaconInfo.toString());

                                        Util.replaceFragment(((MainCustomer) Util.getActivity()).getSupportFragmentManager(),
                                                PromotionDetailFragment.newInstance(promotion.getId(), true),
                                                R.id.fragment_customer_container);
                                    }
                                }
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                                e.printStackTrace();
                                Util.sendEmail(e, "getBeaconPromotions: For promotion");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Util.sendEmail(e, "getBeaconPromotions");
        }
    }

   /*
   private void getBeaconPromotions(Beacons beacon) {
        Response<BeaconPromotions> response = null;
        Log.d("beacons", String.valueOf(beacon.getId()));
       //TODO: Use only getBeaconInfo for all data: beacon-promotion-listPromotions
        Call<BeaconPromotions> call = ServiceGenerator
                            .getService(BeaconsService.class)
                            .getBeaconPromotions(beacon.getId());
        try {
            response = call.execute();
            if(response.isSuccessful()){;
                BeaconPromotions beaconPromotions = response.body();
                if(beaconPromotions.getPromotions().size() >0) {
                    if(((BeaconScanner) Util.getActivity()).isVisible()) {
                        Log.d("beacons","VISIBLE");
                        if (Util.getActivity() instanceof LoginActivityManager) {
                            //Util.goToActivity(Util.getActivity(), Promotion_.class, beaconPromotions);
                        }
                    }
                    else {
                        Log.d("beacons","NOT VISIBLE");
                        //sendNotification(beaconPromotions);
                    }
                }
            }
            else {
                //TODO: Error routine
            }

        } catch (IOException e) {
            //TODO: FAILED ROUTINE

        }
    }
    */
    /*



    private void sendNotification( Promotion beaconPromotion){

        try {
            Bitmap bitMapImage = getBitmapFromURL(beaconPromotion.getImage().getUrl());
            Context context = Util.getContext();
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            //Coupons coupon = beaconPromotions.get(0).getCoupons().get(0);

            // Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(Util.getActivity(), MainCustomer_.class);
            resultIntent.putExtra(Util.INTENT_EXTRA_PROMOTIONS, beaconPromotion.getId());
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            int requestID = (int) System.currentTimeMillis();
            PendingIntent resultPendingIntent = PendingIntent.getActivity(
                    context, requestID, resultIntent, 0);

            NotificationCompat.Builder Builder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_sale)
                            .setContentTitle(beaconPromotion.getTitle())
                            .setContentText(beaconPromotion.getDescription())
                            .setAutoCancel(true)
                            .setContentIntent(resultPendingIntent)
                            .setWhen(System.currentTimeMillis()).setTicker(beaconPromotion.getTitle())
                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                            .setSound(soundUri);

            //.setLargeIcon(bitMapImage)

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            //notificationManager.notify(1, builder.build());

            //notificationManager.notify(random.nextInt(100), builder.build());
            if (Util.bluetoothIsEnabled()) {
                notificationManager.notify(
                        Integer.parseInt(String.valueOf(beaconPromotion.getId())),
                        Builder.build());
            }

        } catch (Exception e) {
            e.printStackTrace();
            Util.sendEmail(e, "sendNotification");
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }*/


}
