package tn.optile.task.auth;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxSessionStore;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.http.HttpRequestor;

import tn.optile.task.client.NativeDropboxImpl;
import tn.optile.task.client.auth.AuthenticationImpl;
import tn.optile.task.client.auth.IAuthentication;

@RunWith(JUnit4.class)
public class AuthenticationTest {

	private IAuthentication authentication;

	@Before
	public void setUp() throws Exception {
		NativeDropboxImpl nativeDropbox = mockTest();
		authentication = new AuthenticationImpl(nativeDropbox, new BufferedReader(new InputStreamReader(System.in)));
	}

	
	
	@Test
	public void readArgumentTest() throws Exception {
		NativeDropboxImpl nativeDropbox = mockTest();
		BufferedReader scanner = mock(BufferedReader.class);
		when(scanner.readLine()).thenReturn("token");
		IAuthentication authentication1 = new AuthenticationImpl(nativeDropbox, scanner);
		assertEquals("token", authentication1.readArgument());
	}	
	
	@Test
	public void getAuthorizeUrlTest() throws Exception {
		assertEquals("http://some/url", authentication.authorize("", ""));
	}

	@Test
	public void authenticate() {
		assertEquals("test-access-token!", authentication.authenticate("xx"));
	}

	private NativeDropboxImpl mockTest() throws Exception {
		DbxRequestConfig CONFIG = DbxRequestConfig.newBuilder("DbxWebAuthTest/1.0").withUserLocaleFrom(Locale.UK)
				.build();
		DbxAppInfo APP = new DbxAppInfo("test-key", "test-secret");
		String redirectUri = "http://localhost/finish/with/state/test";
		DbxSessionStore sessionStore = new SimpleSessionStore();
		String state = "test-state";

		DbxWebAuth.Request request = DbxWebAuth.newRequestBuilder().withRedirectUri(redirectUri, sessionStore)
				.withState(state).build();

		String authorizationUrl = new DbxWebAuth(CONFIG, APP).authorize(request);
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

		DbxRequestConfig mockConfig = CONFIG.copy().withHttpRequestor(mockRequestor).build();

		DbxAuthFinish actual = new DbxWebAuth(mockConfig, APP).finishFromRedirect(redirectUri, sessionStore,
				params("code", "test-code", "state", extractQueryParam(authorizationUrl, "state")));

		NativeDropboxImpl nativeDropboxDbxMock = mock(NativeDropboxImpl.class);
		when(nativeDropboxDbxMock.getAuthorizeUrl(anyString(), anyString())).thenReturn("http://some/url");
		when(nativeDropboxDbxMock.authenticate(anyString())).thenReturn(actual);
		return nativeDropboxDbxMock;
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

	private String extractQueryParam(String query, String param) {
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
			return null;
		}
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
