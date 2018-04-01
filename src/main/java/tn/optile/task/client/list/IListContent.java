package tn.optile.task.client.list;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.ListFolderErrorException;
import com.dropbox.core.v2.files.ListFolderResult;

public interface IListContent {
	
	/**
	 * 
	 * Service to list content of path in Dropbox 
	 * @param token : access token,	which could	be generated using auth command
	 * @param path : path of the item to list details
	 * @param locale: -	the	user’s	locale,	see	specification
	 * @return list result containing Metadata
	 * @throws ListFolderErrorException
	 * @throws DbxException
	 */
	public ListFolderResult listContent(String token, String path, String locale) throws ListFolderErrorException, DbxException;
}
