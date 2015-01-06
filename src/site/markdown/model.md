## Model module goal

## Content

```txt
pl.matsuo.core
======================
.
├── conf
│   ├── ClassesAddingBeanFactoryPostProcessor.java
│   └── DbConfig.java
├── IQueryRequestParams.java
├── IRequestParams.java
├── ISearchRequestParams.java
├── model
│   ├── AbstractEntity.java
│   ├── annotation
│   │   └── ConstrainedByService.java
│   ├── api
│   │   ├── AbstractHasName.java
│   │   ├── HasId.java
│   │   ├── IHasName.java
│   │   ├── Initializer.java
│   │   └── TemporalEntity.java
│   ├── converter
│   │   ├── AutowiringConverter.java
│   │   ├── ConverterAutowiringContextListener.java
│   │   └── StringToTimeConverter.java
│   ├── execution
│   │   └── Execution.java
│   ├── interceptor
│   │   └── InterceptorComponent.java
│   ├── kv
│   │   ├── IKeyValueFacade.java
│   │   └── KeyValueEntity.java
│   ├── log
│   │   └── AccessLog.java
│   ├── message
│   │   ├── AbstractMessage.java
│   │   ├── MailMessage.java
│   │   ├── NoteMessage.java
│   │   ├── NotePriority.java
│   │   ├── NoteStatus.java
│   │   └── SmsMessage.java
│   ├── numeration
│   │   ├── Numeration.java
│   │   └── NumerationSchema.java
│   ├── organization
│   │   ├── AbstractParty.java
│   │   ├── address
│   │   │   ├── Address.java
│   │   │   ├── AddressUtil.java
│   │   │   └── CountryType.java
│   │   ├── initializer
│   │   │   └── CompanyInitializer.java
│   │   ├── OrganizationUnit.java
│   │   ├── Person.java
│   │   └── Sex.java
│   ├── print
│   │   ├── ICompanyPrintFacade.java
│   │   ├── initializer
│   │   │   └── PrintInitializer.java
│   │   ├── IPrintElementFacade.java
│   │   ├── IPrintFacade.java
│   │   ├── KeyValuePrintElement.java
│   │   ├── KeyValuePrint.java
│   │   └── PrintParty.java
│   ├── query
│   │   ├── AbstractQuery.java
│   │   ├── condition
│   │   │   ├── AbstractQueryFunction.java
│   │   │   ├── ComplexCondition.java
│   │   │   ├── Condition.java
│   │   │   ├── FromPart.java
│   │   │   ├── LeftJoinElement.java
│   │   │   ├── QueryFunction.java
│   │   │   ├── QueryPart.java
│   │   │   └── SelectPart.java
│   │   ├── QueryBuilder.java
│   │   └── Query.java
│   ├── report
│   │   └── IPrintsReportParams.java
│   ├── user
│   │   ├── GroupEnum.java
│   │   ├── Group.java
│   │   ├── initializer
│   │   │   └── UserInitializer.java
│   │   └── User.java
│   ├── util
│   │   └── EntityUtil.java
│   └── validation
│       ├── EntityReference.java
│       ├── EntityReferenceValidator.java
│       └── PasswordField.java
├── service
│   ├── db
│   │   ├── DatabaseImpl.java
│   │   ├── Database.java
│   │   ├── DatabaseMethods.java
│   │   ├── EntityInterceptorService.java
│   │   └── interceptor
│   │       ├── AbstractEntityInterceptor.java
│   │       ├── AuditTrailInterceptor.java
│   │       └── IdBucketInterceptor.java
│   ├── facade
│   │   └── IFacadeAware.java
│   ├── ListProvider.java
│   └── session
│       └── SessionState.java
├── test
│   └── data
│       └── TestSessionState.java
└── validation
    └── ValidationErrors.java
```

## Usage