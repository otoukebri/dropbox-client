package tn.optile.task.client.main;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {

	public static void main(String[] args) {
		Injector  injector  = Guice.createInjector(new DropboxClientModule());
		DropboxClientService mainService = injector.getInstance(DropboxClientService.class);
		try {
			mainService.callService(args);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}		
	}
}
