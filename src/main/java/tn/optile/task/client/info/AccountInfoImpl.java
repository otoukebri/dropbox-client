package tn.optile.task.client.info;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.users.FullAccount;
import com.google.inject.Inject;

import tn.optile.task.client.NativeDropboxImpl;

public class AccountInfoImpl implements IAccountInfo {

	private NativeDropboxImpl nativeDropbox;
	
	@Inject
	public AccountInfoImpl(NativeDropboxImpl nativeDropbox) {
		this.nativeDropbox = nativeDropbox;
	}

	public FullAccount getAccountInfo(String token, String locale)
			throws DbxApiException, DbxException {
		return nativeDropbox.getUserAccount(token, locale);

	}

}
