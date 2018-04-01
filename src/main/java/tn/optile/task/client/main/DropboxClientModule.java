package tn.optile.task.client.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import tn.optile.task.client.IDropboxClient;
import tn.optile.task.client.DropboxClientImpl;
import tn.optile.task.client.auth.AuthenticationImpl;
import tn.optile.task.client.auth.IAuthentication;
import tn.optile.task.client.info.AccountInfoImpl;
import tn.optile.task.client.info.IAccountInfo;
import tn.optile.task.client.list.IListContent;
import tn.optile.task.client.list.ListContentImpl;

/**
 * Configures classes dependencies
 * 
 * @author otoukebri
 *
 */
public class DropboxClientModule extends AbstractModule {
	
	/**
	 *  This tells Guice that whenever it sees a dependency on
	 *  IDropboxClient  it should satisfy the dependency using a DropboxClientImpl.
	 */
	@Override
	protected void configure() {
		bind(IDropboxClient.class).to(DropboxClientImpl.class);
		bind(IAuthentication.class).to(AuthenticationImpl.class);
		bind(IAccountInfo.class).to(AccountInfoImpl.class);
		bind(IListContent.class).to(ListContentImpl.class);
	}
	
	
	@Provides
	public BufferedReader provideConsoleReader(){
		return  new BufferedReader(new InputStreamReader(System.in));
	}
}
