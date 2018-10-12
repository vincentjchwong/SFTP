# SFTP

This project allows users to send and receive files from a client to a server through the use of a SFTP.



## Getting Started

These instructions will get you a copy of the project up and running on your local machine.

### Prerequisites

You will need a Java IDE which can compile with external jars (as gson is used) such as Eclipse



### Importing the Project

I will go through the set up using Eclipse as it is one of the most popular IDEs

Open Eclipse to your desired Workspace

Import the project by going File > Import... > General > Existing Projects into Workspace

Click next then browse to the folder where you have saved the project

Click ok then Finish

### Adding GSON (External Jar)

The external jar used in this project is GSON, a Java library created by Google  that allows you to handle JSON files with ease

To add GSON to the workspace, click Window > Preferences > Java > Build Path > User Libraries

Click New and add a name for the library (for example gson_lib) then click ok

Select the newly made library (gson_lib) and select Add External JARs and find the gson-2.6.2.jar file provided in the project files

Apply and close

You should now see no errors

If you are having trouble, please refer to the link below
https://medium.com/programmers-blockchain/importing-gson-into-eclipse-ec8cf678ad52



## Test Case

This is a waklthrough test case for all implemented commands
(Note: As there are many commands, I have not included fail cases as this would be a menial task and would subsequently take thrice as long to read. There are many safeguards implemented against incorrect commands or arguments and I urge you to test them at points at which you deem necessary. Thank you for understanding)
(Note: I have added server console print statements to help aid in tracking the status of the server)
(Note: I recommend opening up the directories \serverFiles and \clientFiles contained within the Project folder so to see any real-time changes made)

>Run TCPServer and TCPClient
(You may want to open up a second console by clicking the Open Console icon, the little window icon with a plus (top right of the console view) > New Console View. You can drag and tile them side-by-side then click the icon to the left of the Open Console icon. For clarity, follow this link https://stackoverflow.com/questions/7261151/in-eclipse-can-i-have-multiple-console-views-at-once-each-showing-a-different)
You should see the following messages:
Server: Server is waiting for incoming socket connection
Client: Client is currently not connected. Please type "connect" to connect to the server

###connect

>Here you should be able to enter the "connect" command into the client
(For ease of repetitive testing, I have implemented a "connect" command that allows the client to connect to the server after it has disconnected)
You should see the following message:
Client: FROM SERVER: +localhost SFTP Service

###USER

>You are now able to log in using USER <userid>. The userids available are: owner | admin | user
>>owner requires the userid, account and password
>>admin requires the userid and account
>>user requires the userid only

If you pass in USER owner, you should see the following messages:
Client: FROM SERVER: +User-id valid, send account and password

###ACCT and PASS

>You must now enter the account or password using ACCT <account> or PASS <password> respectively. Whichever you do not enter will be requested next

If you enter ACCT owner, you should see the following message:
Client: FROM SERVER: +Account valid, send password

>You must now enter the password using PASS pass

You should see the following message:
Client: FROM SERVER: !Logged in

###TYPE

>If you enter TYPE < A | B | C >, the system will change the currentType to ASCII, Binary or Continuous respectively
>>By default, the system is set to Binary

If you enter A, you should see the following message:
Client: FROM SERVER: +Using Ascii mode

[Fail case: If you enter TYPE or TYPE G, you will receive the error message: FROM SERVER: -Type not valid]

>If you enter LIST < F | V > <directory-path>, you will be show the files contained in the directory
>>Entering LIST < F | V > without a directory-path will return the files contained in the current directory
>>LIST V returns the files with extra information including size (in bytes), last modified time, Read-only status and the owner of the file

###LIST

(Open \serverFiles and take notice of the existing files and directories)

If you enter LIST F Directory1, you should see the following message:
Client: FROM SERVER: +PS: <Directory1>
		DirectoryFile1.txt
		
If you enter LIST V, you should see the following message:
Client: FROM SERVER: +PS: <serverFiles>
		Directory1 0 2018-08-21T03:29:59.185789Z Read-only:[false] LAPTOP-LAVGC45K\Vince (User)
		File 5.txt 3 2018-08-21T04:19:42.733686Z Read-only:[false] LAPTOP-LAVGC45K\Vince (User)
		File1.txt 4 2018-08-20T10:20:55.216599Z Read-only:[false] LAPTOP-LAVGC45K\Vince (User)
		File2.txt 40 2018-08-21T04:13:48.736391Z Read-only:[false] LAPTOP-LAVGC45K\Vince (User)
		File3.txt 5 2018-08-21T04:19:22.746086Z Read-only:[false] LAPTOP-LAVGC45K\Vince (User)
		File4.txt 7 2018-08-21T04:19:27.798806Z Read-only:[false] LAPTOP-LAVGC45K\Vince (User)

[Fail case: If you enter LIST G or LIST F Directory2, you will receive the error message: FROM SERVER: -List command not valid]

###CDIR

>If you enter CDIR <directory-path>, you will be able to change directory
>>Entering CDIR without a directory-path will move up 1 directory level unless you are in the top level

If you enter CDIR Directory1, you should see the following message:
Client: FROM SERVER: !Change working dir to ./serverFiles/Directory1

If you enter CDIR, you should see the following message:
Client: FROM SERVER: !Change working dir to ./serverFiles

If you enter CDIR again, you should see the following message:
Client: FROM SERVER: -Can't connect to directory because: This is the top level of the directory

[Fail case: If you enter CDIR Directory2, you will receive the error message: FROM SERVER: -Can't connect to directory because: Directory does not exist]

###KILL

>If you enter KILL <file-spec>, you will be able to delete the requested file

(Open \serverFiles and take notice of File1.txt)

If you enter KILL File1.txt, you should see the following message:
Client: FROM SERVER: +File1.txt deleted

(You should be able to see that File1.txt is now missing from \serverFiles)

[Fail case: If you enter KILL asdf.txt, you will receive the error message: FROM SERVER: -Not deleted because: File does not exist]

###NAME

>If you enter NAME <file-spec>, you will be able to rename a file

If you enter NAME File2.txt, you should see the following message:
Client: FROM SERVER: +File exists, send TOBE <new-name>

(Open \serverFiles and take notice of File2.txt)

[Fail case: If you enter NAME asdf.txt, you will receive the error message: FROM SERVER: -Can't find asdf.txt]

If you enter TOBE newname.txt, you should see the following message:
Client: FROM SERVER: +File2.txt renamed to newname.txt

(You should be able to see that File2.txt has been replaced by newName.txt in \serverFiles)

[Fail case: If you enter TOBE, you will receive the error message: FROM SERVER: -File wasn't renamed because: Invalid new name]

###DONE

>If you enter DONE, you will be able to disconnect from the server

You should see the following message:
Client: FROM SERVER: +Ending connection
		Client is currently not connected. Please type "connect" to connect to the server

>If you enter "connect", you will be able to reconnect to the server
>>You may want to try a different log in this time

If you enter USER user, you should see the following message:
Client: FROM SERVER: !user logged in

###RETR

>If you enter RETR <file-spec>, you will be able to retrieve a file from the server
>>The response, if successful, if the size of the file in bytes

If you enter RETR File3.txt, you should see the following message:
Client: FROM SERVER:  5

[Fail case: If you enter RETR asdf.txt, you will receive the error messages: FROM SERVER: -File doesn't exist,
FROM SERVER: +RETR aborted]

>If you enter SEND, the file will be transferred. Otherwise, entering STOP will abort the transfer

If you enter SEND, you should see the following message:
Client: FROM SERVER: +File was sent

[Fail case: If you enter STOP, you will receive the error message: FROM SERVER: +RETR aborted]

If you now send RETR File3.txt again, you should see the following message:
FROM SERVER:  5
Client: FROM CLIENT: File already exists or there is not enough space
FROM SERVER: +RETR aborted



## Authors

* **Vincent Wong** - *vwon870* -(https://github.com/vincentjchwong)
