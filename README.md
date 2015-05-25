Eionet Documentation Module
===========================

Introduction
------------
The Eionet Documentation Module is a package to support documentation pages.
To use it you integrate the sample JSP file into your web application.

The package relies on a database table called 'documentation' that must be stored in a
database and the content of the pages are stored on the file system.

In addition, one must implement the following interfaces: 

* _eionet.doc.dal.sql.SqlConnectionProvider_, for database connectivity 
* _eionet.doc.io.FileStorageInfoProvider_, for filesystem accessibility

To build:

```
mvn install
```
or
```
mvn deploy
```

