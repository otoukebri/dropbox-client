package tn.optile.task.info;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.DbxUserUsersRequests;
import com.dropbox.core.v2.users.FullAccount;

import tn.optile.task.client.NativeDropboxImpl;
import tn.optile.task.client.info.IAccountInfo;
import tn.optile.task.client.info.AccountInfoImpl;

@RunWith(JUnit4.class)
public class AccountInfoTest {

	private IAccountInfo accountInfo;

	@Before
	public void setUp() throws Exception {
		NativeDropboxImpl nativeDropboxDbxMock = nativeDropboxImplMock();
		accountInfo = new AccountInfoImpl(nativeDropboxDbxMock);

	}

	@Test
	public void testInfoOk() throws Exception {
		assertEquals("jsmith@company.com", accountInfo.getAccountInfo(anyString()).getEmail());

	}

	private NativeDropboxImpl nativeDropboxImplMock() throws Exception {
		DbxClientV2 dbxClientV2 = mock(DbxClientV2.class);
		DbxUserUsersRequests dbxUserUsersRequests = mock(DbxUserUsersRequests.class);
		FullAccount fullAccount = mock(FullAccount.class);
		when(dbxClientV2.users()).thenReturn(dbxUserUsersRequests);
		when(dbxUserUsersRequests.getCurrentAccount()).thenReturn(fullAccount);
		when(fullAccount.getEmail()).thenReturn("jsmith@company.com");
		NativeDropboxImpl NativeDropboxDbxMock = mock(NativeDropboxImpl.class);
		when(NativeDropboxDbxMock.getUserAccount(anyString())).thenReturn(fullAccount);
		return NativeDropboxDbxMock;
	}

}
