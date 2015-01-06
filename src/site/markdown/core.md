## Core module goal

## Content

```txt
pl.matsuo.core
======================
.
├── conf
│   ├── DiscoverTypes.java
│   ├── GeneralConfig.java
│   ├── TestDataDiscoveryRegisteringBeanFactoryPostProcessor.java
│   └── TestDataExecutionConfig.java
├── exception
│   ├── RestProcessingException.java
│   └── UnauthorizedException.java
├── service
│   ├── execution
│   │   ├── ExecutionServiceImpl.java
│   │   └── IExecuteService.java
│   ├── facade
│   │   ├── FacadeBuilder.java
│   │   ├── FacadeBuilderMethods.java
│   │   ├── FacadeInvocationHandler.java
│   │   └── IFacadeBuilder.java
│   ├── i18n
│   │   ├── I18nServiceImpl.java
│   │   └── I18nService.java
│   ├── login
│   │   ├── CreateAccountData.java
│   │   ├── ILoginServiceExtension.java
│   │   ├── ILoginService.java
│   │   ├── LoginData.java
│   │   └── LoginService.java
│   ├── numeration
│   │   ├── MonthlyNumerationSchemaStrategy.java
│   │   ├── NumerationSchemaStrategy.java
│   │   ├── NumerationServiceImpl.java
│   │   ├── NumerationService.java
│   │   └── QuaterlyNumerationSchemaStrategy.java
│   ├── parameterprovider
│   │   ├── AbstractParameterProvider.java
│   │   ├── IParameterProvider.java
│   │   ├── KeyValueParameterProvider.java
│   │   └── MapParameterProvider.java
│   ├── permission
│   │   ├── IPermissionService.java
│   │   ├── model
│   │   │   └── Permissions.java
│   │   └── PermissionService.java
│   ├── print
│   │   ├── AbstractPrintService.java
│   │   ├── IPrintsRendererService.java
│   │   ├── PrintMethods.java
│   │   └── PrintsRendererService.java
│   ├── report
│   │   ├── AbstractReportService.java
│   │   ├── DataModelBuilder.java
│   │   ├── IReportService.java
│   │   └── PrintsReportService.java
│   └── user
│       ├── GroupsService.java
│       ├── IGroupProviderService.java
│       └── IGroupsService.java
└── test
    ├── AbstractPrintGeneratingTest.java
    ├── AbstractPrintTest.java
    ├── AbstractReportTest.java
    ├── CreatePdfResult.java
    ├── data
    │   ├── AbstractMediqTestData.java
    │   ├── AbstractTestData.java
    │   ├── AbstractUserTestData.java
    │   ├── GroupTestData.java
    │   ├── MediqTestData.java
    │   ├── PayersTestData.java
    │   ├── PersonTestData.java
    │   └── UserTestData.java
    └── NumerationConfig.java
```

## Usage