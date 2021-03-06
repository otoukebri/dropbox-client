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

import tn.optile.task.client.DropboxClientImpl;
import tn.optile.task.client.info.AccountInfoImpl;
import tn.optile.task.client.info.IAccountInfo;
import tn.optile.task.common.CommonTest;

@RunWith(JUnit4.class)
public class AccountInfoTest extends CommonTest {

	private IAccountInfo accountInfo;

	@Before
	public void setUp() throws Exception {
		DropboxClientImpl nativeDropboxDbxMock = nativeDropboxImplMock();
		accountInfo = new AccountInfoImpl(nativeDropboxDbxMock);

	}

	@Test
	public void testInfoOk() throws Exception {
		assertEquals("accountIdcccccccccccaccountIdccccccccccc", accountInfo.getAccountInfo(anyString(), 
				anyString()).getAccountId());
		assertEquals("jsmith@company.com", accountInfo.getAccountInfo(anyString(), anyString()).getEmail());
		assertEquals("touka", accountInfo.getAccountInfo(anyString(), anyString()).getName().getDisplayName());
		assertEquals("givenName", accountInfo.getAccountInfo(anyString(), anyString()).getName().getGivenName());
	}

	private DropboxClientImpl nativeDropboxImplMock() throws Exception {
		DbxClientV2 dbxClientV2 = mock(DbxClientV2.class);
		DbxUserUsersRequests dbxUserUsersRequests = mock(DbxUserUsersRequests.class);
		FullAccount fullAccount = getFullAccount();
		when(dbxClientV2.users()).thenReturn(dbxUserUsersRequests);
		DropboxClientImpl NativeDropboxDbxMock = mock(DropboxClientImpl.class);
		when(NativeDropboxDbxMock.getUserAccount(anyString(), anyString())).thenReturn(fullAccount);
		return NativeDropboxDbxMock;
	}

}
