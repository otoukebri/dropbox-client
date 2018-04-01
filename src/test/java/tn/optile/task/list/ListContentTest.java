package tn.optile.task.list;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DbxUserFilesRequests;
import com.dropbox.core.v2.files.ListFolderErrorException;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;

import tn.optile.task.client.DropboxClientImpl;
import tn.optile.task.client.list.IListContent;
import tn.optile.task.client.list.ListContentImpl;

@RunWith(JUnit4.class)
public class ListContentTest {

	IListContent listContent;

	@Before
	public void setUp() throws Exception {
		DropboxClientImpl nativeDropboxDbxMock = nativeDropboxImplMock();
		listContent = new ListContentImpl(nativeDropboxDbxMock);
	}

	@Test
	public void testlistContent() throws ListFolderErrorException, DbxException {
		ListFolderResult listFolderResult = listContent.listContent("", "", "");
		List<Metadata> entries = listFolderResult.getEntries();
		assertEquals(1, entries.size());
		assertEquals("test", entries.get(0).getName());
	}

	private DropboxClientImpl nativeDropboxImplMock() throws Exception {
		DropboxClientImpl nativeDropboxDbxMock = mock(DropboxClientImpl.class);
		List<Metadata> entries = Arrays.asList(new Metadata("test", "/toto", "dzedz", "dzd"));
		DbxClientV2 dbxClientV2 = mock(DbxClientV2.class);
		DbxUserFilesRequests dbxUserFilesRequests = mock(DbxUserFilesRequests.class);
		ListFolderResult listFolderResult = mock(ListFolderResult.class);

		when(nativeDropboxDbxMock.initializeClient(anyString(), anyString())).thenReturn(dbxClientV2);
		when(dbxClientV2.files()).thenReturn(dbxUserFilesRequests);
		when(dbxUserFilesRequests.listFolder(anyString())).thenReturn(listFolderResult);			
		when(listFolderResult.getEntries()).thenReturn(entries);
		return nativeDropboxDbxMock;
	}
}
