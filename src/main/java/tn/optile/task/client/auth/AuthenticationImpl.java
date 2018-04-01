package tn.optile.task.client.auth;

import java.io.BufferedReader;
import java.io.IOException;

import com.google.inject.Inject;

import tn.optile.task.client.DropboxClientImpl;

public class AuthenticationImpl implements IAuthentication {
	
	private DropboxClientImpl nativeDropbox;
	private BufferedReader bufferedReader;
	
	
	@Inject
	public AuthenticationImpl(DropboxClientImpl nativeDropbox, BufferedReader bufferedReader) {
		this.nativeDropbox =  nativeDropbox;
		this.bufferedReader = bufferedReader;
	}
	
	public String authorize(String appKey, String secretKey) {
		return nativeDropbox.getAuthorizeUrl(appKey, secretKey);
	}

	public String authenticate(String key) {
		return nativeDropbox.authenticate(key).getAccessToken();		
	}
	
	
	public  String readArgument() throws IOException {
		return bufferedReader.readLine();
	}
	
}
