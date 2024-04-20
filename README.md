# Client Server Network

This java program allows clients to connect to a server for a simple file transfer system.

## Description

Clients are able to request a list of files on the server or are able to put a text file onto the server. The server utilises a fixed thread pool of 20 threads, allowing up to 20 clients to connect and make requests simultaneously.

## Getting Started

How to run the program
```
javac server.java
java server
javac client.java
```

```
java client list
```
or 
```
java client put file_name.txt
```
