package tn.optile.task.client.info;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.users.FullAccount;
import com.google.inject.Inject;

import tn.optile.task.client.DropboxClientImpl;

public class AccountInfoImpl implements IAccountInfo {

	private DropboxClientImpl nativeDropbox;
	
	@Inject
	public AccountInfoImpl(DropboxClientImpl nativeDropbox) {
		this.nativeDropbox = nativeDropbox;
	}

	public FullAccount getAccountInfo(String token, String locale)
			throws DbxApiException, DbxException {
		return nativeDropbox.getUserAccount(token, locale);

	}

}
