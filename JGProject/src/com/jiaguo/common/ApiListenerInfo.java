package com.jiaguo.common;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;



public class ApiListenerInfo implements  Parcelable{

	public   void onSuccess(Object obj){
		
	}


	/*public  void onError(int statusCode){
		
	}*/


	@Override
	public int describeContents() {

		return 0;
	}


	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		
		
	}



	
}
