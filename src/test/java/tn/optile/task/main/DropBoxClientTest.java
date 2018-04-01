package tn.optile.task.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxSessionStore;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.DbxWebAuth.BadRequestException;
import com.dropbox.core.DbxWebAuth.BadStateException;
import com.dropbox.core.DbxWebAuth.CsrfException;
import com.dropbox.core.DbxWebAuth.NotApprovedException;
import com.dropbox.core.DbxWebAuth.ProviderException;
import com.dropbox.core.http.HttpRequestor;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.DbxUserUsersRequests;
import com.dropbox.core.v2.users.FullAccount;

import tn.optile.task.client.NativeDropboxImpl;

public class DropBoxClientTest {

	private static final DbxRequestConfig CONFIG = DbxRequestConfig.newBuilder("DbxWebAuthTest/1.0")
			.withUserLocaleFrom(Locale.UK).build();
	private static final DbxAppInfo APP = new DbxAppInfo("test-key", "test-secret");

	@Before
	public void setUp() {
	}

	@Test
	public void getAuthorizeUrlTest() throws Exception {
		String redirectUri = "http://localhost/finish/with/state/test";
		DbxSessionStore sessionStore = new SimpleSessionStore();
		String state = "test-state";
		DbxWebAuth.Request request = DbxWebAuth.newRequestBuilder().withRedirectUri(redirectUri, sessionStore)
				.withState(state).build();

		String authorizationUrl = new DbxWebAuth(CONFIG, APP).authorize(request);
		assertNotNull(sessionStore.get());
		DbxAuthFinish expected = new DbxAuthFinish("test-access-token!", "test-user-id!", "test!", state);
		ByteArrayOutputStream body = new ByteArrayOutputStream();
		ByteArrayInputStream responseStream = new ByteArrayInputStream(("{" + "\"token_type\":\"Bearer\""
				+ ",\"access_token\":\"" + expected.getAccessToken() + "\"" + ",\"uid\":\"" + expected.getUserId()
				+ "\"" + ",\"account_id\":\"" + expected.getAccountId() + "\"" + "}").getBytes("UTF-8"));
		HttpRequestor.Response finishResponse = new HttpRequestor.Response(200, responseStream,
				new HashMap<String, List<String>>());
		HttpRequestor mockRequestor = mock(HttpRequestor.class);
		HttpRequestor.Uploader mockUploader = mock(HttpRequestor.Uploader.class);
		when(mockUploader.getBody()).thenReturn(body);
		when(mockUploader.finish()).thenReturn(finishResponse);
		when(mockRequestor.startPost(anyString(), anyListOf(HttpRequestor.Header.class))).thenReturn(mockUploader);
		NativeDropboxImpl nativeDropboxDbxMock = mock(NativeDropboxImpl.class);
		when(nativeDropboxDbxMock.getAuthorizeUrl(anyString(), anyString())).thenReturn(authorizationUrl);
		NativeDropboxImpl nativeDropboxImpl = new NativeDropboxImpl();
		assertEquals("https://www.dropbox.com/oauth2/authorize?response_type=code&client_id=app_key",
				nativeDropboxImpl.getAuthorizeUrl("app_key", "secret_key"));
	}

	@Test
	public void authenticateTest() throws Exception {

		String redirectUri = "http://localhost/finish/with/state/test";
		DbxSessionStore sessionStore = new SimpleSessionStore();
		String state = "test-state";

		DbxWebAuth.Request request = DbxWebAuth.newRequestBuilder().withRedirectUri(redirectUri, sessionStore)
				.withState(state).build();

		// simulate a web server that will not keep the DbxWebAuth
		// instance across requests
		String authorizationUrl = new DbxWebAuth(CONFIG, APP).authorize(request);
		String code = "test-code";

		assertNotNull(sessionStore.get());

		DbxAuthFinish expected = new DbxAuthFinish("test-access-token!", "test-user-id!", "test!", state);
		ByteArrayOutputStream body = new ByteArrayOutputStream();
		ByteArrayInputStream responseStream = new ByteArrayInputStream(("{" + "\"token_type\":\"Bearer\""
				+ ",\"access_token\":\"" + expected.getAccessToken() + "\"" + ",\"uid\":\"" + expected.getUserId()
				+ "\"" + ",\"account_id\":\"" + expected.getAccountId() + "\"" + "}").getBytes("UTF-8"));
		HttpRequestor.Response finishResponse = new HttpRequestor.Response(200, responseStream,
				new HashMap<String, List<String>>());

		HttpRequestor mockRequestor = mock(HttpRequestor.class);
		HttpRequestor.Uploader mockUploader = mock(HttpRequestor.Uploader.class);
		ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
		when(mockUploader.getBody()).thenReturn(body);
		when(mockUploader.finish()).thenReturn(finishResponse);
		when(mockRequestor.startPost(anyString(), anyListOf(HttpRequestor.Header.class))).thenReturn(mockUploader);

		DbxRequestConfig mockConfig = CONFIG.copy().withHttpRequestor(mockRequestor).build();

		DbxAuthFinish actual = new DbxWebAuth(mockConfig, APP).finishFromRedirect(redirectUri, sessionStore,
				params("code", "test-code", "state", extractQueryParam(authorizationUrl, "state")));
		NativeDropboxImpl nativeDropboxDbxMock = mock(NativeDropboxImpl.class);
		when(nativeDropboxDbxMock.authenticate(anyString())).thenReturn(actual);
		// DropBoxClient client = new DropBoxClient(NativeDropboxDbxMock);
		// System.out.println("client ==>!" + client.authenticate("xx"));
		// NativeDropboxImpl nativeDropboxImpl = new NativeDropboxImpl();
		// assertEquals("test-access-token!", nativeDropboxImpl.authenticate("xx"));

	}

	@Test
	public void getAcountInfoTest() throws DbxException, IOException, BadRequestException, BadStateException,
			CsrfException, NotApprovedException, ProviderException {
		DbxClientV2 dbxClientV2 = mock(DbxClientV2.class);
		DbxUserUsersRequests dbxUserUsersRequests = mock(DbxUserUsersRequests.class);
		FullAccount fullAccount = mock(FullAccount.class);
		when(dbxClientV2.users()).thenReturn(dbxUserUsersRequests);
		when(dbxUserUsersRequests.getCurrentAccount()).thenReturn(fullAccount);
		when(fullAccount.getEmail()).thenReturn("xxxxxx");
		NativeDropboxImpl nativeDropboxDbxMock = mock(NativeDropboxImpl.class);
		when(nativeDropboxDbxMock.getUserAccount(anyString(), anyString())).thenReturn(fullAccount);

		// DropBoxClient client = new DropBoxClient(nativeDropboxDbxMock);
		// System.out.println("getEmail ==>!" +
		// client.getAccountInfo(anyString()).getEmail());
		// NativeDropboxImpl nativeDropboxImpl = new NativeDropboxImpl();
		// assertEquals("xxxxxx",
		// nativeDropboxImpl.getUserAccount("some_token").getEmail());
	}

	private static Map<String, String[]> params(String... pairs) {
		if ((pairs.length % 2) != 0) {
			fail("pairs must be a multiple of 2.");
		}

		Map<String, String[]> query = new HashMap<String, String[]>();
		for (int i = 0; i < pairs.length; i += 2) {
			query.put(pairs[i], new String[] { pairs[i + 1] });
		}
		return query;
	}

	private static Map<String, List<String>> toParamsMap(URL url) {
		return toParamsMap(url.getQuery());
	}

	private static Map<String, List<String>> toParamsMap(String query) {
		try {
			String[] pairs = query.split("&");
			Map<String, List<String>> params = new HashMap<String, List<String>>(pairs.length);

			for (String pair : pairs) {
				String[] keyValue = pair.split("=", 2);
				String key = keyValue[0];
				String value = keyValue.length == 2 ? keyValue[1] : "";

				List<String> others = params.get(key);
				if (others == null) {
					others = new ArrayList<String>();
					params.put(key, others);
				}

				others.add(URLDecoder.decode(value, "UTF-8"));
			}

			return params;
		} catch (Exception ex) {
			// fail("Couldn't build query parameter map from: " + query, ex);
			return null;
		}
	}

	private static String extractQueryParam(String query, String param) {
		Map<String, List<String>> params = toParamsMap(query);

		if (!params.containsKey(param)) {
			fail("Param \"" + param + "\" not found in: " + query);
			return null;
		}

		List<String> values = params.get(param);
		if (values.size() > 1) {
			fail("Param \"" + param + "\" appears more than once in: " + query);
			return null;
		}

		return values.get(0);
	}

	private static Iterable<HttpRequestor.Header> anyHeaders() {
		return Matchers.<Iterable<HttpRequestor.Header>>any();
	}

	private static HttpRequestor.Response createSuccessResponse(byte[] body) throws IOException {
		return new HttpRequestor.Response(200, new ByteArrayInputStream(body),
				Collections.<String, List<String>>emptyMap());
	}

	// default config
	private static DbxRequestConfig.Builder createRequestConfig() {
		return DbxRequestConfig.newBuilder("sdk-test");
	}

	class SimpleSessionStore implements DbxSessionStore {
		private String token;

		public SimpleSessionStore() {
			this.token = null;
		}

		public String get() {
			return token;
		}

		// @Override
		public void set(String value) {
			this.token = value;
		}

		// @Override
		public void clear() {
			this.token = null;
		}
	}

}
