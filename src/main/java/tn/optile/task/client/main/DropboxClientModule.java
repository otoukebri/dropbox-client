package tn.optile.task.client.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import tn.optile.task.client.INativeDropbox;
import tn.optile.task.client.NativeDropboxImpl;
import tn.optile.task.client.auth.AuthenticationImpl;
import tn.optile.task.client.auth.IAuthentication;
import tn.optile.task.client.info.AccountInfoImpl;
import tn.optile.task.client.info.IAccountInfo;
import tn.optile.task.client.list.IListContent;
import tn.optile.task.client.list.ListContentImpl;

public class DropboxClientModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(INativeDropbox.class).to(NativeDropboxImpl.class);
		bind(IAuthentication.class).to(AuthenticationImpl.class);
		bind(IAccountInfo.class).to(AccountInfoImpl.class);
		bind(IListContent.class).to(ListContentImpl.class);
	}
	
	
	@Provides
	public BufferedReader provideConsoleReader(){
		return  new BufferedReader(new InputStreamReader(System.in));
	}
}
