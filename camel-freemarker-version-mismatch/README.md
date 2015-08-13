CAMEL-9076 - Camel Freemarker version mismatch issue reproducer
=================

Apache Karaf
-------------------------
You will need to compile this example first:
    mvn clean install

To install Apache Camel in Karaf you type in the shell

    features:chooseurl camel 2.15.3-SNAPSHOT
    features:install camel

First you need to install the following features in Karaf/ServiceMix with:

    features:install camel-blueprint
    features:install camel-freemarker

Then you can install the Camel example:

    osgi:install -s mvn:org.apache.camel/camel-freemarker-version-mismatch/2.15.3-SNAPSHOT

You'll get following error:

```
ERROR: Bundle camel-freemarker-version-mismatch [80] EventDispatcher: Error during dispatch. (java.lang.NoSuchFieldError: VERSION_2_3_21)
java.lang.NoSuchFieldError: VERSION_2_3_21
	at org.apache.camel.component.freemarker.FreemarkerComponent.getConfiguration(FreemarkerComponent.java:74)
	at org.apache.camel.component.freemarker.FreemarkerComponent.createEndpoint(FreemarkerComponent.java:48)
	at org.apache.camel.impl.DefaultComponent.createEndpoint(DefaultComponent.java:114)
	at org.apache.camel.impl.DefaultCamelContext.getEndpoint(DefaultCamelContext.java:558)
	at org.apache.camel.util.CamelContextHelper.getMandatoryEndpoint(CamelContextHelper.java:79)
	at org.apache.camel.model.RouteDefinition.resolveEndpoint(RouteDefinition.java:200)
	at org.apache.camel.impl.DefaultRouteContext.resolveEndpoint(DefaultRouteContext.java:107)
	at org.apache.camel.impl.DefaultRouteContext.resolveEndpoint(DefaultRouteContext.java:113)
	at org.apache.camel.model.SendDefinition.resolveEndpoint(SendDefinition.java:62)
	at org.apache.camel.model.SendDefinition.createProcessor(SendDefinition.java:56)
	at org.apache.camel.model.ProcessorDefinition.makeProcessor(ProcessorDefinition.java:505)
	at org.apache.camel.model.ProcessorDefinition.addRoutes(ProcessorDefinition.java:217)
	at org.apache.camel.model.RouteDefinition.addRoutes(RouteDefinition.java:1025)
	at org.apache.camel.model.RouteDefinition.addRoutes(RouteDefinition.java:185)
	at org.apache.camel.impl.DefaultCamelContext.startRoute(DefaultCamelContext.java:841)
	at org.apache.camel.impl.DefaultCamelContext.startRouteDefinitions(DefaultCamelContext.java:2911)
	at org.apache.camel.impl.DefaultCamelContext.doStartCamel(DefaultCamelContext.java:2634)
	at org.apache.camel.impl.DefaultCamelContext.access$000(DefaultCamelContext.java:167)
	at org.apache.camel.impl.DefaultCamelContext$2.call(DefaultCamelContext.java:2483)
	at org.apache.camel.impl.DefaultCamelContext$2.call(DefaultCamelContext.java:2479)
	at org.apache.camel.impl.DefaultCamelContext.doWithDefinedClassLoader(DefaultCamelContext.java:2502)
	at org.apache.camel.impl.DefaultCamelContext.doStart(DefaultCamelContext.java:2479)
	at org.apache.camel.support.ServiceSupport.start(ServiceSupport.java:61)
	at org.apache.camel.impl.DefaultCamelContext.start(DefaultCamelContext.java:2448)
	at org.apache.camel.blueprint.BlueprintCamelContext.start(BlueprintCamelContext.java:180)
	at org.apache.camel.blueprint.BlueprintCamelContext.maybeStart(BlueprintCamelContext.java:212)
	at org.apache.camel.blueprint.BlueprintCamelContext.serviceChanged(BlueprintCamelContext.java:150)
	at org.apache.felix.framework.util.EventDispatcher.invokeServiceListenerCallback(EventDispatcher.java:943)
	at org.apache.felix.framework.util.EventDispatcher.fireEventImmediately(EventDispatcher.java:794)
	at org.apache.felix.framework.util.EventDispatcher.fireServiceEvent(EventDispatcher.java:544)
	at org.apache.felix.framework.Felix.fireServiceEvent(Felix.java:4445)
	at org.apache.felix.framework.Felix.registerService(Felix.java:3431)
	at org.apache.felix.framework.BundleContextImpl.registerService(BundleContextImpl.java:346)
	at org.apache.felix.framework.BundleContextImpl.registerService(BundleContextImpl.java:353)
	at org.apache.camel.blueprint.BlueprintCamelContext.init(BlueprintCamelContext.java:100)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:606)
	at org.apache.aries.blueprint.utils.ReflectionUtils.invoke(ReflectionUtils.java:297)
	at org.apache.aries.blueprint.container.BeanRecipe.invoke(BeanRecipe.java:958)
	at org.apache.aries.blueprint.container.BeanRecipe.runBeanProcInit(BeanRecipe.java:712)
	at org.apache.aries.blueprint.container.BeanRecipe.internalCreate2(BeanRecipe.java:824)
	at org.apache.aries.blueprint.container.BeanRecipe.internalCreate(BeanRecipe.java:787)
	at org.apache.aries.blueprint.di.AbstractRecipe$1.call(AbstractRecipe.java:79)
	at java.util.concurrent.FutureTask.run(FutureTask.java:262)
	at org.apache.aries.blueprint.di.AbstractRecipe.create(AbstractRecipe.java:88)
	at org.apache.aries.blueprint.container.BlueprintRepository.createInstances(BlueprintRepository.java:245)
	at org.apache.aries.blueprint.container.BlueprintRepository.createAll(BlueprintRepository.java:183)
	at org.apache.aries.blueprint.container.BlueprintContainerImpl.instantiateEagerComponents(BlueprintContainerImpl.java:682)
	at org.apache.aries.blueprint.container.BlueprintContainerImpl.doRun(BlueprintContainerImpl.java:377)
	at org.apache.aries.blueprint.container.BlueprintContainerImpl.run(BlueprintContainerImpl.java:269)
	at org.apache.aries.blueprint.container.BlueprintExtender.createContainer(BlueprintExtender.java:294)
	at org.apache.aries.blueprint.container.BlueprintExtender.createContainer(BlueprintExtender.java:263)
	at org.apache.aries.blueprint.container.BlueprintExtender.modifiedBundle(BlueprintExtender.java:253)
	at org.apache.aries.util.tracker.hook.BundleHookBundleTracker$Tracked.customizerModified(BundleHookBundleTracker.java:500)
	at org.apache.aries.util.tracker.hook.BundleHookBundleTracker$Tracked.customizerModified(BundleHookBundleTracker.java:433)
	at org.apache.aries.util.tracker.hook.BundleHookBundleTracker$AbstractTracked.track(BundleHookBundleTracker.java:725)
	at org.apache.aries.util.tracker.hook.BundleHookBundleTracker$Tracked.bundleChanged(BundleHookBundleTracker.java:463)
	at org.apache.aries.util.tracker.hook.BundleHookBundleTracker$BundleEventHook.event(BundleHookBundleTracker.java:422)
	at org.apache.felix.framework.util.SecureAction.invokeBundleEventHook(SecureAction.java:1127)
	at org.apache.felix.framework.util.EventDispatcher.createWhitelistFromHooks(EventDispatcher.java:696)
	at org.apache.felix.framework.util.EventDispatcher.fireBundleEvent(EventDispatcher.java:484)
	at org.apache.felix.framework.Felix.fireBundleEvent(Felix.java:4429)
	at org.apache.felix.framework.Felix.startBundle(Felix.java:2100)
	at org.apache.felix.framework.BundleImpl.start(BundleImpl.java:976)
	at org.apache.felix.framework.BundleImpl.start(BundleImpl.java:963)
	at org.apache.karaf.shell.osgi.InstallBundle.doExecute(InstallBundle.java:51)
	at org.apache.karaf.shell.console.OsgiCommandSupport.execute(OsgiCommandSupport.java:38)
	at org.apache.felix.gogo.commands.basic.AbstractCommand.execute(AbstractCommand.java:35)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:606)
	at org.apache.aries.proxy.impl.ProxyHandler$1.invoke(ProxyHandler.java:54)
	at org.apache.aries.proxy.impl.ProxyHandler.invoke(ProxyHandler.java:119)
	at org.apache.karaf.shell.console.commands.$BlueprintCommand928158108.execute(Unknown Source)
	at org.apache.felix.gogo.runtime.CommandProxy.execute(CommandProxy.java:78)
	at org.apache.felix.gogo.runtime.Closure.executeCmd(Closure.java:477)
	at org.apache.felix.gogo.runtime.Closure.executeStatement(Closure.java:403)
	at org.apache.felix.gogo.runtime.Pipe.run(Pipe.java:108)
	at org.apache.felix.gogo.runtime.Closure.execute(Closure.java:183)
	at org.apache.felix.gogo.runtime.Closure.execute(Closure.java:120)
	at org.apache.felix.gogo.runtime.CommandSessionImpl.execute(CommandSessionImpl.java:92)
	at org.apache.karaf.shell.console.jline.Console.run(Console.java:195)
	at java.lang.Thread.run(Thread.java:745)
```
