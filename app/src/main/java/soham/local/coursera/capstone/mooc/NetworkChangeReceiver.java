package soham.local.coursera.capstone.mooc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/*
    Network change Broadcast receiver class.
    calls a function of interface class implemented at call site.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    private OnNetworkChangeListener _listener;


    /*
        Without this there were errors.
     */
    public NetworkChangeReceiver(){
        super();
    }


    /*
    Constructor to initialze the implemented interface class.
     */
    public NetworkChangeReceiver(OnNetworkChangeListener listener){
        super();
        this._listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if(this._listener!=null)
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                this._listener.onChange(true); // calling the function to perform desired task
            } else {
                this._listener.onChange(false);
            }
    }

    /*
        The interface class acting as a callback
     */
    public interface OnNetworkChangeListener{
        public void onChange(boolean state);
    }

}
