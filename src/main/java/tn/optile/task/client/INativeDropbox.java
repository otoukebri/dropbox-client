package tn.optile.task.client;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.FullAccount;

public interface INativeDropbox {
	 String getAuthorizeUrl(String appKey, String secretKey);
	 DbxAuthFinish authenticate(String code);
	 FullAccount getUserAccount(String token, String locate) throws DbxApiException, DbxException;
	 DbxClientV2 initializeClient(String token, String locate);
}
