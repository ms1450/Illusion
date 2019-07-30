________________________________________________________________________________________________________________________


                                 _____ _             _____ _ _           _
                                /__   \ |__   ___    \_   \ | |_   _ ___(_) ___  _ __
                                  / /\/ '_ \ / _ \    / /\/ | | | | / __| |/ _ \| '_ \
                                 / /  | | | |  __/ /\/ /_ | | | |_| \__ \ | (_) | | | |
                                 \/   |_| |_|\___| \____/ |_|_|\__,_|___/_|\___/|_| |_|


________________________________________________________________________________________________________________________

What is it?
------------------------------------------------------------------------------------------------------------------------
The Illusion is a two way communication that is equipped with end to end dynamic encryption.

What is it? (Advanced Level)
------------------------------------------------------------------------------------------------------------------------
For those who need more details, Illusion is a collection of four encryption methods:
                               -AES Encryption
                               -Binary Reverse Encryption
                               -ROT 13 and ROT 5 Encryption
                               -Base64 Encryption
These four types of encryption are dynamically applied to the text sent by the clients. The order in which these
encryption's are applied get changed over random intervals. The Clients also possess no information about the other
client connected to the server, their only point of interaction is with the server itself. The server acts as the
mediator, allowing text to be sent from only one client at a time. Even though the server has encrypted information from
one client that gets sent to the other client, it itself does not have any information stored for the current
set or the keys present with the client therefore the is no way the server can decrypt the messages and a man in the
middle attack can occur.


How do I use it?
------------------------------------------------------------------------------------------------------------------------
There are two runnable classes, the first is the Illusion_Server which is present inside the Server Package. During
runtime, this class will ask the user for a port number to be assigned to that server. Once the port is entered, the
server will automatically connect to the clients that are online would continue to work until a client disconnects.
The other runnable class, Illusion_Client is inside the Client package, during runtime it will asks for the IP address
of the running server, the port number of that server and the encryption code that only the other clients knows about.
Once the client gets connected to the server, if a ">" pops on the terminal then that would signify that the server is
waiting for input from that client. The client can then input his text and on pressing enter, the text will get
encrypted dynamically depending on the current configuration and sent to the other client that is connected with the
same server connected. The exchange of messages will occur alternatively. To end the conversation, the client can type
"TERMINATE" during his turn and end the connection securely.

TLDR, just start the server class do as it says, then start two instances of the client class and enter the information
it asks about.

More Questions?
------------------------------------------------------------------------------------------------------------------------
Throw them at me on ms1450@rit.edu

Ciao.


