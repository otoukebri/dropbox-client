package tn.optile.task.client.info;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.users.FullAccount;

public interface IAccountInfo {
	
	/**
	 * Retrieves account informations that contains details about user's account
	 * 
	 * @param token : access token,	which could	be generated using auth command
	 * @param locale: the user’s locale, see specification
	 * @return
	 * @throws DbxApiException
	 * @throws DbxException
	 */
	public FullAccount getAccountInfo(String token, String locale) throws DbxApiException, DbxException;
}
