## Matsuo core

Implementation of core functions for server/web application.

## Get it now

Get it now!

```xml
<dependency>
  <groupId>pl.matsuo</groupId>
  <artifactId>matsuo-core</artifactId>
  <version>1.0-SNAPSHOT</version>
</skin>
```

## Configure

Configure

## Last

Last


## Utils

Provides common utilities used in all layers. Generally they do not depend on application layer implementation.

Most important are:

* **`ArrayUtil`**
* **`CollectionUtil`**
* **`DateUtil`**
* **`EnumUtil`**
* **`FunctionalUtil`**
* **`ReflectUtil`**
* **`RegexUtil`**
* **`SecurityUtil`**
* **`StringUtil`**

Utils should have as little dependencies as possible.
They don't do anything business related.
They are only helpers.

## Base model

Provides base model used in application.

Important concepts are:

* **`AbstractEntity`**
* **`Database`**
* **`QueryBuilder`**
* **`SessionState`**
* **`User`**
* **`Group`**
* **`AbstractMessage`**
* **`Initializer`**
* **`IRequestParams`**
* **`KeyValueEntity`**

Model should not provide any services, only architecture of data.
One exception is Database service. It is placed here for completeness of module.

## Common services

Provides implementation of services commonly used in applications.

You should start with getting knowledge about:

* **`DiscoverTypes`**
* **`RestProcessingException`**
* **`IExecuteService`**
* **`IFacadeBuilder`**
* **`I18nService`**
* **`ILoginService`**
* **`NumerationService`**
* **`IParameterProvider`**
* **`IPermissionService`**
* **`IPrintsRendererService`**
* **`IReportService`**
* **`IGroupsService`**

Core module provides services operating on model. They are view independent.

## REST interface

Commmon controllers used in applications. Login, messages and

* **`AbstractController`**
* **`AbstractSimpleController`**
* **`LoginController`**
* **`AbstractMessageController`**
* **`NumerationController`**
* **`NumerationSchemaController`**
* **`OrganizationUnitController`**
* **`PersonController`**
* **`PrintController`**
* **`AbstractPrintController`**
* **`BootstrapRendererController`**
* **`ReportsController`**
* **`UserController`**
* **`PermissionsFilter`**
* **`AccessLogFilter`**
* **`FacadeBuilderHandlerMethodArgumentResolver`**
* **`CustomDateFormat`**
* **`CustomJacksonModule`**
* **`WideSessionScope`**
* **`BootstrapRenderer`**

Web module provides interface for application.
Remember that this is REST/JSON based interface of server side application.
Do not place any user interface elements here.
They should be part of separate web application.

---

## More information

---

### About

Matsuo Core is developed by Matuo IT Marek Romanowski.

This is open source software licensed on LGPL version 3.0.
