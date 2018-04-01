package tn.optile.task.client.list;

import java.util.Arrays;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderErrorException;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.google.inject.Inject;

import tn.optile.task.client.DropboxClientImpl;

public class ListContentImpl implements IListContent {

	private DropboxClientImpl nativeDropbox;

	@Inject
	public ListContentImpl(DropboxClientImpl nativeDropbox) {
		this.nativeDropbox = nativeDropbox;
	}

	public ListFolderResult listContent(String token, String path, String locale)
			throws ListFolderErrorException, DbxException {
		DbxClientV2 dbxClient = nativeDropbox.initializeClient(token, locale);
		Metadata metadata = dbxClient.files().getMetadata(path);
		if (metadata instanceof FileMetadata) {
			return new ListFolderResult(Arrays.asList(metadata), "__", false);

		} else {
			return dbxClient.files().listFolder(path);
		}
	}

}
