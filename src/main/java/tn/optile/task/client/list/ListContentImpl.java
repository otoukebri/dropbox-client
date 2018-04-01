package tn.optile.task.client.list;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderErrorException;
import com.dropbox.core.v2.files.ListFolderResult;
import com.google.inject.Inject;

import tn.optile.task.client.NativeDropboxImpl;

public class ListContentImpl implements IListContent {
	
	private NativeDropboxImpl nativeDropbox;
	
	@Inject
	public ListContentImpl(NativeDropboxImpl nativeDropbox) {
		this.nativeDropbox =  nativeDropbox;
	}

	public ListFolderResult listContent(String token, String path) throws ListFolderErrorException, DbxException {
		DbxClientV2 dbxClient = nativeDropbox.initializeClient(token);
		ListFolderResult result = dbxClient.files().listFolder(path);
//		dbxClient.files().listFolderContinue(result.getCursor());
		return result;
	}
	
	
}
