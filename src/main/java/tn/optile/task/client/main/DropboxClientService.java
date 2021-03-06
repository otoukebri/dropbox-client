package tn.optile.task.client.main;

import java.io.IOException;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.ListFolderErrorException;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;
import com.google.inject.Inject;

import tn.optile.task.client.auth.IAuthentication;
import tn.optile.task.client.info.IAccountInfo;
import tn.optile.task.client.list.IListContent;
import tn.optile.task.client.utils.Utils;

/**
 * 
 * FSM that handles commands of the application
 * after validating input arguments 
 * 
 * @author otoukebri
 *
 */
public class DropboxClientService {

	private IAuthentication authentication;
	private IAccountInfo accountInfo;
	private IListContent listContent;

	private final String AUTHENTICATION_OPERATION = "auth";
	private final String INFO_OPERATION = "info";
	private final String LIST_OPERATION = "list";

	@Inject
	public DropboxClientService(IAuthentication authentication, IAccountInfo accountInfo, IListContent listContent) {
		this.authentication = authentication;
		this.accountInfo = accountInfo;
		this.listContent = listContent;
	}
	
	/**
	 * FSM to invoke different application services
	 * 
	 * @param args
	 * @throws Exception
	 */
	public void callService(String[] args) throws Exception {
		if (args.length == 0) {
			throw new Exception("Invalid command : valid commands are : auth, info, list");
		}
		String command = args[0].trim().toLowerCase();
		switch (command) {
		case AUTHENTICATION_OPERATION: {
			if (args.length == 3) {
				authenticateUser(args[1], args[2]);
				break;
			} else {
				throw new Exception("Invalid auth arguments command format should be : auth {appKey} {appSecret}");
			}
		}
		case INFO_OPERATION: {
			if (args.length >= 2) {
				String token = args[1];
				if (args.length == 2) {
					info(accountInfo.getAccountInfo(token, ""));
				} else {
					String locale = args[2];
					info(accountInfo.getAccountInfo(token, locale));
				}
				break;

			} else {
				throw new Exception("Invalid info arguments command format should be : info {accessToken} {locale}");
			}
		}
		case LIST_OPERATION: {
			if (args.length < 2) {
				throw new Exception("Invalid list arguments command format should be : list {accessToken} {path}");
			} else {
				if (args.length == 3) {
					String token = args[1];
					String path = args[2];
					list(listContent.listContent(token, path, ""), path);
				} else {
					String token = args[1];
					String path = args[2];
					String locale = args[3];					
					list(listContent.listContent(token, path, locale), path);
				}
			}
			break;
		}
		default:
			throw new Exception("Invalid command :  valid commands are: auth, info, list");
		}
	}
	
	/**
	 * Displays content of given path
	 * 
	 * @param result
	 * @param path
	 * @throws ListFolderErrorException
	 * @throws DbxException
	 */
	public void list(ListFolderResult result, String path) throws ListFolderErrorException, DbxException {
		System.out.println(path);
		while (true) {
			for (Metadata metadata : result.getEntries()) {
				if (metadata instanceof FileMetadata) {
					FileMetadata fileMetadata = (FileMetadata) metadata;
					StringBuilder sb = new StringBuilder();
					sb.append("- /" + metadata.getName() + "  " + "file " + " ");
					sb.append(Utils.formatFileSize(fileMetadata.getSize()) + " ");
					sb.append("modified at: " + fileMetadata.getServerModified() + " ");
					System.out.println(sb.toString());

				} else if (metadata instanceof FolderMetadata) {
					StringBuilder sb = new StringBuilder();
					sb.append("- /" + metadata.getName() + "  " + "folder " + " ");
					System.out.println(sb.toString());
				}
			}

			if (!result.getHasMore()) {
				break;
			}
		}

	}
	
	/**
	 * Displays informations about user's account
	 * 
	 * @param fullAccount
	 */
	public void info(FullAccount fullAccount) {
		System.out.println("User ID: " + fullAccount.getAccountId());
		System.out.println("Display name!: " + fullAccount.getName().getDisplayName());
		System.out.println("Name: " + fullAccount.getName().getGivenName() + " " + fullAccount.getName().getSurname()
				+ " " + fullAccount.getName().getFamiliarName());
		System.out.println("E-mail: " + fullAccount.getEmail());
		System.out.println("Country:" + fullAccount.getCountry());
		System.out.println("Referral link: " + fullAccount.getReferralLink());

	}
	
	/**
	 * Process authentication steps
	 * 
	 * @param appKey
	 * @param secretKey
	 * @return
	 * @throws IOException
	 */
	private String authenticateUser(String appKey, String secretKey) throws IOException {
		String authMsg = displayAuthenticateMessage();
		System.out.println(authMsg);
		String authenticationUrl = authentication.authorize(appKey, secretKey);
		System.out.println(authenticationUrl);
		String token = authentication.readArgument();
		String authenticationToken = authentication.authenticate(token);
		System.out.println("Your access token: " + authenticationToken);
		return authenticationToken;
	}
	
	/**
	 * Displays instructions of authentication command
	 *
	 */
	private String displayAuthenticateMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("");
		sb.append("1. Go to: <authorize URL>");
		sb.append("\n");
		sb.append("2. Click \"Allow\" (you might have to log in first)");
		sb.append("\n");
		sb.append("3. Copy the authorization code and paste it here:");
		sb.append("");
		return sb.toString();
	}

}
