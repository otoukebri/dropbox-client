package tn.optile.task.client.auth;

import java.io.IOException;

public interface IAuthentication {

	public String authorize(String appKey, String secretKey);

	public String authenticate(String key);

	public String readArgument() throws IOException;
}
