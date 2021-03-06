package tn.optile.task.client;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.FullAccount;

public class DropboxClientImpl implements IDropboxClient {

	private DbxWebAuth webAuth;
	private final String CLIENT_IDENTIFIER = "examples-authorize";
	private DbxRequestConfig requestConfig;

	public DropboxClientImpl() {
		requestConfig = new DbxRequestConfig(CLIENT_IDENTIFIER);
	}

	public String getAuthorizeUrl(String appKey, String secretKey) {
		DbxAppInfo appInfo = new DbxAppInfo(appKey, secretKey);
		DbxWebAuth webAuth = new DbxWebAuth(requestConfig, appInfo);
		this.webAuth = webAuth;
		DbxWebAuth.Request webAuthRequest = DbxWebAuth.newRequestBuilder().withNoRedirect().build();
		return webAuth.authorize(webAuthRequest);
	}

	public DbxAuthFinish authenticate(String code) {
		DbxAuthFinish authFinish = null;
		try {
			authFinish = webAuth.finishFromCode(code);
		} catch (DbxException ex) {
			System.err.println("Error in DbxWebAuth.authorize: " + ex.getMessage());
			System.exit(1);
		}
		return authFinish;

	}

	public FullAccount getUserAccount(String token, String locate) throws DbxApiException, DbxException {
		DbxClientV2 dbxClient = initializeClient(token, locate);
		return dbxClient.users().getCurrentAccount();
	}

	public DbxClientV2 initializeClient(String token, String locale) {
		if (locale == null || locale.isEmpty())
			return new DbxClientV2(requestConfig, token);
		else {
			DbxRequestConfig requestConfig = DbxRequestConfig.newBuilder(CLIENT_IDENTIFIER)
					.withUserLocale(locale)
					.build();

			return new DbxClientV2(requestConfig, token);
		}
	}

}
