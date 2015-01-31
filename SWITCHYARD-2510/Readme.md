
Reproducer for SWITCHYARD-2510
============

This application contains plain CDI bean, Bean Service and Transformer/Validator using bean resolution, and each of them have @PreDestroy method. They all have no scope specified, so @Dependent. Check if it's actually invoked.

![SWITCHYARD-2510](https://github.com/igarashitm/switchyard-issues/raw/master/SWITCHYARD-2510/bean-service.jpg)