# Features

- auth	-	authenticates	and	authorizes	the	access	to	Dropbox	account	
- info	-	retrieves	and	prints	user's	account	information	
- list	-	prints	files	and	folders	information	for	specified	path

# Usage

- java -jar dropbox-client.jar auth {appKey} {appSecret}
- java -jar dropbox-client.jar info {accessToken} {locale}
- java -jar dropbox-client.jar list {accessToken} {path} {locale}
