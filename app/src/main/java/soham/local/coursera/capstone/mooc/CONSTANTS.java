package soham.local.coursera.capstone.mooc;


import android.util.Log;


/*
Constant file or values that are accessed accross the app
 */

final public class CONSTANTS {
    final public static String DATABASE_NAME = "QrGenerator";
    final public static String TABLE_QR_HISTORY = "qr_history";

    final private static String _QR_API_URL = "https://api.qrserver.com/v1/create-qr-code/" ;

    public static String GenerateApi(int Size,String Text){

        String s = ( _QR_API_URL +"?size="+Size+"x"+Size+"&format=png"+"&data="+Text)
                        .replace("\t","%20")
                        .replace(" ","%20");
        return s;
    }


}
