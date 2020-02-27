package cn.haml.context;

import cn.hamal.core.Node;
import cn.hamal.core.URL;
import cn.hamal.core.constant.ProtocolProperty;
import cn.hamal.core.exception.ConfilictServiceException;
import cn.hamal.core.extension.ExtensionLoader;
import cn.hamal.core.util.AnnotationUtils;
import cn.hamal.rpc.Invoker;
import cn.hamal.rpc.InvokerFactory;
import cn.hamal.rpc.annotation.Application;
import cn.hamal.rpc.annotation.Service;
import cn.hamal.rpc.config.ApplicationConfig;
import cn.hamal.rpc.config.ReferenceConfig;
import cn.hamal.rpc.config.ServiceConfig;
import cn.hamal.rpc.container.ServiceContainer;
import cn.hamal.rpc.protocol.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bdq
 * @since 2020/2/27
 */
public class ContextManager {
    private static final Logger log = LoggerFactory.getLogger(ContextManager.class);
    private Protocol protocol;
    private ApplicationConfig applicationConfig;
    private ServiceContainer serviceContainer;

    public ContextManager(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
        this.protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getExtension(applicationConfig.getProtocol());
    }

    public void open() {
        try {
            protocol.open(applicationConfig.toURL());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public void close() {
        serviceContainer.getAll().values().forEach(Node::destroy);
        protocol.destroy();
        if (log.isInfoEnabled()) {
            log.info("closed context!");
        }
    }

    public <T> T getProxy(ReferenceConfig<T> referenceConfig) {
        URL url = referenceConfig.toURL();
        url.addParam(ProtocolProperty.LOAD_BALANCER, applicationConfig.getLoadbalancer());
        url.addParam(ProtocolProperty.SERIALIZER, applicationConfig.getSerilizer());
        if (applicationConfig.isDirect()) {
            url.addParam(ProtocolProperty.DIRECT, true);
            url.addParam(ProtocolProperty.HOST, applicationConfig.getHost());
            url.addParam(ProtocolProperty.PORT, applicationConfig.getPort());
            url.addParam(ProtocolProperty.SERVER, applicationConfig.getServer());
            url.addParam(ProtocolProperty.PROTOCOL, applicationConfig.getProtocol());
        }
        Invoker<T> invoker = protocol.refer(referenceConfig.getServiceInterface(), url);
        return InvokerFactory.getProxy(invoker);
    }

    public <T> void registerService(T instance) {
        Service service = AnnotationUtils.getMergedAnnotation(instance.getClass(), Service.class);
        assert service != null;
        ServiceConfig<T> serviceConfig = new ServiceConfig<>(service);
        registerService(instance, serviceConfig);
    }

    public <T> void registerService(T instance, ServiceConfig<T> serviceConfig) {
        serviceConfig.setHost(applicationConfig.getHost());
        serviceConfig.setPort(applicationConfig.getPort());
        Class<T> serviceInterface = serviceConfig.getServiceInterface();
        Invoker<T> invoker = InvokerFactory.getServiceInvoker(instance, serviceInterface, serviceConfig.toURL());
        try {
            serviceContainer.regsiter(serviceInterface.getCanonicalName(), invoker);
        } catch (ConfilictServiceException e) {
            throw new IllegalStateException(e);
        }
    }

    public static ContextManager build(Class<?> clazz) {
        Application application = AnnotationUtils.getMergedAnnotation(clazz, Application.class);
        assert application != null;

        ApplicationConfig applicationConfig = new ApplicationConfig(application);
        ContextManager contextManager = new ContextManager(applicationConfig);

        contextManager.serviceContainer = ExtensionLoader.getExtensionLoader(ServiceContainer.class)
                .getExtension(application.container());

        if (application.startUp()) {
            contextManager.open();
        }

        return contextManager;
    }
}
