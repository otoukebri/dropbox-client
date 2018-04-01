package tn.optile.task.client.auth;

import java.io.IOException;


public interface IAuthentication {
	
	/**
	 * Invokes IDropboxClient services implementations to process authentication using application	key and secret code
	 * @param appKey : Dropbox	application	key
	 * @param secretKey: Dropbox application secret code	
	 * @return authorize URL
	 */
	public String authorize(String appKey, String secretKey);
	
	/**
	 * Invokes IDropboxClient services implementations to process authentication using the provided URL
	 * @param key
	 * @return
	 */
	public String authenticate(String key);

	/**
	 * Reads token provided from user
	 * @return
	 * @throws IOException
	 */
	public String readArgument() throws IOException;
}
