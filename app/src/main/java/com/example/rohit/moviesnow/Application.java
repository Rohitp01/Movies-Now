package com.example.rohit.moviesnow;


import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;
/*This class is as need of Parse SDK*/
public class Application extends android.app.Application {

	private static final String TAG = Application.class.getSimpleName();

	@Override
	public void onCreate() {
		super.onCreate();

		Parse.enableLocalDatastore(getApplicationContext());
		ParseUser.enableAutomaticUser();
		Parse.initialize(this);
		ParseACL.setDefaultACL(new ParseACL(), true);

	}
}
