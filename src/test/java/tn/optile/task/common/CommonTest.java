package tn.optile.task.common;

import com.dropbox.core.v2.common.RootInfo;
import com.dropbox.core.v2.users.FullAccount;
import com.dropbox.core.v2.users.Name;
import com.dropbox.core.v2.userscommon.AccountType;

public class CommonTest {
	
	public FullAccount getFullAccount() {
		Name name = new Name("givenName", "surname", "familiarName", "touka", "abbreviatedName");
		FullAccount FullAccount = com.dropbox.core.v2.users.FullAccount
				.newBuilder("accountIdcccccccccccaccountIdccccccccccc", name, "jsmith@company.com", true, false, "FR", "", false, AccountType.BASIC, new RootInfo("xx","xx")).build();
		return FullAccount;
	}		

}
