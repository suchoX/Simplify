package com.ns3.simplify.others;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;
import android.widget.Toast;

import com.ns3.simplify.NsdChatActivity;

/**
 * Created by ASUS on 12-May-16.
 */
public class NsdHelper {

    Context mContext;

    NsdManager mNsdManager;
    //ResolveListener, DiscoveryListener and RegistrationListener are inner class
    NsdManager.ResolveListener mResolveListener;
    NsdManager.DiscoveryListener mDiscoveryListener;
    NsdManager.RegistrationListener mRegistrationListener;

    public static final String SERVICE_TYPE = "_http._tcp.";

    public static final String TAG = "NsdHelper";
    public String mServiceName;

    public static boolean flag;

    NsdServiceInfo mService;


    public NsdHelper(Context context) {
        mContext = context;
        flag = false;
        mNsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
    }

    public void initializeDiscoveryListener() {
        mDiscoveryListener = new NsdManager.DiscoveryListener() {

            @Override
            public void onDiscoveryStarted(String regType) {
                Log.d(TAG, "Service discovery started");
                Toast.makeText(mContext, "Discovery started", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                Log.d(TAG, "Service discovery success" + service);
                Toast.makeText(mContext, "Service discovery success" + service, Toast.LENGTH_SHORT).show();
                if (!service.getServiceType().equals(SERVICE_TYPE)) {
                    Log.d(TAG, "Unknown Service Type: " + service.getServiceType());
                    Toast.makeText(mContext, "Unknown Service Type: " + service.getServiceType(), Toast.LENGTH_SHORT).show();
                } else if ((service.getServiceName().equals(mServiceName)) && flag) {
                    Log.d(TAG, "Same machine: " + mServiceName);
                    Toast.makeText(mContext, "Same machine: " + mServiceName, Toast.LENGTH_SHORT).show();
                } else if (service.getServiceName().equals(mServiceName)) {
                    Log.e(TAG,"resolving");
                    mNsdManager.resolveService(service, mResolveListener);
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                Log.e(TAG, "Service lost: " + service);
                Toast.makeText(mContext, "Service lost: " + service, Toast.LENGTH_SHORT).show();
                if (mService == service) {
                    mService = null;
                }
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i(TAG, "Discovery stopped: " + serviceType);
                Toast.makeText(mContext, "Discovery stopped: " + serviceType, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                Toast.makeText(mContext, "Discovery start failed: Error code:" + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                Toast.makeText(mContext, "Discovery stop failed: Error code:" + errorCode, Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void initializeNsd() {
        mResolveListener = new NsdManager.ResolveListener() {

            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.e(TAG, "Resolve failed" + errorCode);
                Toast.makeText(mContext, "Resolve failed" + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                Log.d(TAG, "Resolve Succeeded. " + serviceInfo);
                Toast.makeText(mContext, "Resolve Succeeded. " + serviceInfo, Toast.LENGTH_SHORT).show();
                if (serviceInfo.getServiceName().equals(mServiceName) && flag) {
                    Log.d(TAG, "Same IP.");
                    Toast.makeText(mContext, "Same IP.", Toast.LENGTH_SHORT).show();
                    return;
                }
                mService = serviceInfo;

                final NsdChatActivity ac = (NsdChatActivity)mContext;
                ac.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //show the join button
                        ac.setVisibilityButton(true);
                    }
                });
                //ac.setVisibilityButton(true);
            }
        };
    }

    //Detect the success or failure of service registration and unregistration
    public void initializeRegistrationListener() {
        mRegistrationListener = new NsdManager.RegistrationListener() {

            @Override
            public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
                mServiceName = NsdServiceInfo.getServiceName();
                flag = true;
                Log.d(TAG, "Service registered: " + mServiceName);
                Toast.makeText(mContext, "Service registered: " + mServiceName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRegistrationFailed(NsdServiceInfo arg0, int arg1) {
                Log.d(TAG, "Service registration failed: " + arg1);
                Toast.makeText(mContext,"Service registration failed: " + arg1, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo arg0) {
                Log.d(TAG, "Service unregistered: " + arg0.getServiceName());
                Toast.makeText(mContext, "Service unregistered: " + arg0.getServiceName(), Toast.LENGTH_SHORT).show();
                flag = false;
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.d(TAG, "Service unregistration failed: " + errorCode);
                Toast.makeText(mContext, "Service unregistration failed: " + errorCode, Toast.LENGTH_SHORT).show();
            }

        };
    }

    //Registering the service on the local network
    public void registerService(int port) {
        tearDown();  // Cancel any previous registration request
        initializeRegistrationListener();
        NsdServiceInfo serviceInfo = new NsdServiceInfo();
        serviceInfo.setPort(port);
        mServiceName = NsdChatActivity.mServiceName;
        serviceInfo.setServiceName(mServiceName);
        serviceInfo.setServiceType(SERVICE_TYPE);
        Toast.makeText(mContext, "Service registered at port: " + port,Toast.LENGTH_SHORT).show();
        mNsdManager.registerService(
                serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);

    }

    public void discoverServices() {
        stopDiscovery();  // Cancel any existing discovery request
        mServiceName = NsdChatActivity.mServiceName;
        initializeDiscoveryListener();
        mNsdManager.discoverServices(
                SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
    }

    public void stopDiscovery() {
        if (mDiscoveryListener != null) {
            mNsdManager.stopServiceDiscovery(mDiscoveryListener);
            mDiscoveryListener = null;
        }
    }

    public NsdServiceInfo getChosenServiceInfo() {
        return mService;
    }

    public void tearDown() {
        if (mRegistrationListener != null) {
            mNsdManager.unregisterService(mRegistrationListener);
            mRegistrationListener = null;
        }
    }
}
