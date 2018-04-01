package tn.optile.task.client.info;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.users.FullAccount;

public interface IAccountInfo {

	public FullAccount getAccountInfo(String token) throws DbxApiException, DbxException;
}
