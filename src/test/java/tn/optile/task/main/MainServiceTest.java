package tn.optile.task.main;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.dropbox.core.v2.users.FullAccount;

import tn.optile.task.client.DropboxClientImpl;
import tn.optile.task.client.auth.AuthenticationImpl;
import tn.optile.task.client.info.AccountInfoImpl;
import tn.optile.task.client.list.ListContentImpl;
import tn.optile.task.client.main.DropboxClientService;
import tn.optile.task.common.CommonTest;

@RunWith(JUnit4.class)
public class MainServiceTest extends CommonTest{

	private DropboxClientService mainService;
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	@Before
	public void setUp() {
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
		DropboxClientImpl nativeDropbox = new DropboxClientImpl();
		mainService = new DropboxClientService(new AuthenticationImpl(nativeDropbox,new BufferedReader(new InputStreamReader(System.in))),
				new AccountInfoImpl(nativeDropbox), new ListContentImpl(nativeDropbox));
	}

	@Test(expected = Exception.class)
	public void testInvalidCallService() throws Exception {
		String args[] = {};
		mainService.callService(args);
	}

	@Test(expected = Exception.class)
	public void testCallService() throws Exception {
		String args[] = { "invalid_command" };
		mainService.callService(args);
	}

	@Test(expected = Exception.class)
	public void testCallInvalidAuthService() throws Exception {
		String args[] = { "auth" };
		mainService.callService(args);
	}

	@Test // (expected = Exception.class)
	public void testCallInfoService() throws Exception {
		String args[] = { "info", "UFKsPn3tfIQAAAAAAAAIbIEFqzE4BZ6X9JAZwC8OmGrPa4CjKN1J2wzY7xIZ5bDh" };
		mainService.callService(args);
	}

	@Test(expected = Exception.class)
	public void testInvalidCallInfoService() throws Exception {
		String args[] = { "info" };
		mainService.callService(args);
	}

	@Test(expected = Exception.class)
	public void testCallListService() throws Exception {
		String args[] = { "list", "UFKsPn3tfIQAAAAAAAAIbIEFqzE4BZ6X9JAZwC8OmGrPa4CjKN1J2wzY7xIZ5bDh" };
		mainService.callService(args);
	}

	@Test(expected = Exception.class)
	public void testInvalidCallListService() throws Exception {
		String args[] = { "list" };
		mainService.callService(args);
	}

	@Test
	public void testInfoOk() throws Exception {
		AuthenticationImpl authenticationImpl = mock(AuthenticationImpl.class);
		AccountInfoImpl accountInfoImpl = mock(AccountInfoImpl.class);
		ListContentImpl listContentImpl = mock(ListContentImpl.class);
		DropboxClientService mainService = new DropboxClientService(authenticationImpl, accountInfoImpl, listContentImpl);
		FullAccount fullAccount =  getFullAccount();//mock(FullAccount.class);
		when(fullAccount.getEmail()).thenReturn("jsmith@company.com");		
		when(accountInfoImpl.getAccountInfo(anyString(), anyString())).thenReturn(fullAccount);
		String args[] = { "info" , "UFKsPn3tfIQAAAAAAAAIbIEFqzE4BZ6X9JAZwC8OmGrPa4CjKN1J2wzY7xIZ5bDh" };
		mainService.callService(args);

	}

	@Test
	public void testAuthOk() throws Exception {
		AuthenticationImpl authenticationImpl = mock(AuthenticationImpl.class);
		AccountInfoImpl accountInfoImpl = mock(AccountInfoImpl.class);
		ListContentImpl listContentImpl = mock(ListContentImpl.class);
		DropboxClientService mainService = new DropboxClientService(authenticationImpl,
		accountInfoImpl, listContentImpl);
		when(authenticationImpl.authenticate(anyString())).thenReturn("some_url");		
		when(authenticationImpl.authorize(anyString(), anyString())).thenReturn("some_token");
		when(authenticationImpl.readArgument()).thenReturn("some_token1");
		String args[] = { "auth" , "UFKsPn3tfIQAAAAAAAAIbIEFqzE4BZ6X", "9JAZwC8OmGrPa4CjKN1J2wzY7xIZ5bDh" };
		mainService.callService(args);

	}

	
}
