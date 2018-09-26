# web-server-fire-squad
James Andrews
Michael Swanson

Github repo link: https://github.com/sfsu-csc-667-fall-2018/web-server-fire-squad/graphs/contributors

Rubric Copy


Problems encountered during implementation
We had a hard time implementation script execution in cgi.  We were able to get it partially working, but it would break other http methods.
Project Overview
The architecture of our project is is based around the ConfigurationReader.java class which works uses the Factory Design pattern to return different config files how we see fit in other classes.

The Config.java class itself is an abstract class with two abstract methods, Load() and lookUP().  MimeTypes.java, HttpdConf.java, HTaccess.java and HTpasswd.java extend Config and parse the config files and place the values of the files into hashmaps.



Build Instructions
The entry point to the application is WebServer.Java.
With the JDK installed, use the command*
> javac WebServer.java
to compile all required classes. To start the server, use
> java WebServer
The server operates on a default port of 8096, to change the port, you need to change the key value pair found in the configuration file
Listen <PORT NUMBER>

Note*: During development we’ve had trouble occasionally with classes not compiling after changes, if you encounter this behavior, use
> find . -type f -name "*.class" -delete
to delete all .class files present in the directory tree starting from your current directory. Use this to quickly delete all .class files and then recompile again.

WebServer
WebSerber.java initializes the main ServerWorker thread found in local package worker.
Worker
There are two classes within the worker package, ServerWorker and Worker.

ServerWorker
	ServerWorker is a Runnable thread that conducts the main operation of the server, it is responsible for
Binding the server to a port
Creating a thread pool of Worker objects
Accepting client connections
When the ServerWorker accepts a client connection, it passes the Socket of the client to a Worker thread that handles the job of dealing with the client connections http request.

Worker
Worker is a Runnable thread that conducts the operations of
Parsing HTTP requests
Creating a Resource
Verifying user authentication
Sending HTTP responses

Worker steps through all the checks detailed in the Server Workflow PDF provided by the course instructor.

When a client Socket is handed to a Worker, it is popped from the thread pool queue and immediately parses whatever HTTP request that is sent over that socket with the RequestParser class.

Worker parses the HTTP request into a Request object, Request objects are containers of information sent by the HTTP request. RequestParser validates the format of the HTTP request and sends a BadRequest flag to Request if the HTTP request is unreadable. More information on Request is in the Request section of the documentation.

Worker constructs a Resource based off the URI contained in the Request, this Resource is a representation of the requested item that the server provides to the client. Resource determines if a file is protected and needs authentication. 

Worker will check the headers in the Request for authentication info, if none is provided, the Worker will respond with a 401 Response. The user’s browser will ask for authentication, and send another Request with the provided authentication which is verified by the AccessCheck class within the accesscheck package. If the authentication is still invalid, the Worker will respond with a 403 Response.

If the Request has provided proper authentication the worker will send an appropriate Response in HTTP format based on the Request sent.

Worker will then close the connection and reenter the thread pool queue, and wait for another available connection to be handed to process.

Request
Package request contains two classes, Request and RequestParser. Request is strictly a container for field information that is sent through the HTTP request. This HTTP request is parsed by RequestParser. Request contains fields for
Method/ Verb, URI, HTTP Version
Headers
Body
BadRequest flag

RequestParser is called within the constructor of Request, it parses the HTTP Request by utilizing a BufferedReader to read the input stream. RequestParser populates Request fields while doing so. 

RequestParser also validates the HTTP request and sends a BadRequest flag to Request if the HTTP request is unreadable.

RequestParser kills itself once it’s done parsing the HTTP request.

Resource
Resource is a one class package that represents the data stored on the server that is requested for by the client. Resource is built from the URI provided by the Request class.

Resource does several checks to determine whether the URI provided by the Request class is aliased, script-aliased, or protected. Resource determines if a directory is protected by the presence of an .htaccess file, which contains the absolute path to .htpasswd, which contains stored encrypted passwords. Resource also has methods that check the lastModified property of files hosted by the server which is used by the Worker to determine caching.
Response
The response package contains all the packages that correlate to HTTP responses.  The responses are served via a ResponseFactory.  The ResponseFactory takes in a Resource as a parameter,  parses the Resource to get the method then passes the method through a switch statement to determine which new response to return.

Response.java is an Abstract class that contains an abstract send method, all the different types of response headers a http method can have, and a validFile check.

Each Response subclass extends response and implements the send method, which writes the headers and resources to the Outputsream according to what the http method is.
 
AccesCheck
AccessCheck is another one class package. AccessCheck takes the authentication header from Request. AccessCheck uses Base64 and SHA256 to compare the authentication info provided by the request against the password stored in .htpasswd, within the conf folder.

AccessCheck first decodes the Base64 password provided by the authentication header, then encrypts it with SHA256 and makes a comparison with the stored encypted password.

AccessCheck needs the path to the .htaccess file as part of it’s constructor. It uses the path to construct a Config class of type Htaccess. Htaccess contains the path to the passwords file. AccessCheck constructs a Config object of type Htpasswd after it obtains the htaccess file.

Logger
The logger package contains the Logger class that writes to a /logs/log.txt file specified by httpd.conf. The log() function takes three arguments,
Request request
Resource resource
String username
The logger uses these parameters to derive the information necessary for the logfile to properly log in Common Log Format specified here,
https://httpd.apache.org/docs/1.3/logs.html
The username field is provided by AccesCheck which derives it from the authentication header provided by the request, if provided. Username defaults to UNKNOWN_USER specified in the worker class.

Caching
Caching is accomplished in our server by taking the lastModified() property of the requested Resource provided by the Resource class. LocalDateTIme objects are constructed within the Worker thread based off of the If-Modified-Since value provided.
These two LocalDateTime objects are compared and if the Resources lastModified() DateTime is before the IMS DateTime then a 304 Response is sent in place of the usual Response code.

Test Plan
Postman, Chrome
Tested cache - IMS headers in GET request
Tested multithreading - Chrome -> multithreading button

Most classes also have an in house unit test written for them.

Accesscheck - tested on Postman and Chrome
Able to properly display all requirements except scripts on both Postman and Chrome


