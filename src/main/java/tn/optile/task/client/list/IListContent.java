package tn.optile.task.client.list;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.ListFolderErrorException;
import com.dropbox.core.v2.files.ListFolderResult;

public interface IListContent {

	public ListFolderResult listContent(String token, String path) throws ListFolderErrorException, DbxException;
}
