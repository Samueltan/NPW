Right now, the code won't work directly on another machine before being deployed correctly.
The following steps need to be done before it can run.
1. A tomcat web server needs to be setup properly (e.g. c:\program files\apache\tomcat)
2. The test.jsp file needs to be deployed to tomcat\webapps folder
3. Start up the tomcat server
4. Save the cmd.properties file to a specific location e.g. C:\temp\cmd.properties
5. Update the code in the following files for the cmd.properties file to the specific location mentioned above:
  tomcat\test.jsp: line 30
  \BaseLED\src\org\sunspotworld\SpotServer.java: line 67