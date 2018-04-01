package tn.optile.task.client;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.FullAccount;

public interface IDropboxClient {
	
	/**
	 * Process authentication using application	key and secret code
	 * 
	 * @param appKey
	 * @param secretKey
	 * @return
	 */
	String getAuthorizeUrl(String appKey, String secretKey);
	
	/**
	 * Process authentication using the provided url
	 * 
	 * @param code
	 * @return
	 */
	DbxAuthFinish authenticate(String code);
	
	/**
	 * Retrieves account informations that contains details about user's account
	 * 
	 * @param token
	 * @param locate
	 * @return
	 * @throws DbxApiException
	 * @throws DbxException
	 */
	FullAccount getUserAccount(String token, String locate) throws DbxApiException, DbxException;
	
	/**
	 * Initializes DbxClientV2 intance to process further tasks
	 * 
	 * @param token
	 * @param locate
	 * @return
	 */
	DbxClientV2 initializeClient(String token, String locate);
}
