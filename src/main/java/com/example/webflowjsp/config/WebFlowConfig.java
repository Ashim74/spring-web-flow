package com.example.webflowjsp.config;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Validator;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;
import org.springframework.webflow.engine.builder.support.FlowDefinitionRegistryBuilder;
import org.springframework.webflow.engine.impl.FlowExecutionImplFactory;
import org.springframework.webflow.executor.FlowExecutor;
import org.springframework.webflow.executor.FlowExecutorFactoryBean;
import org.springframework.webflow.mvc.servlet.FlowHandlerAdapter;
import org.springframework.webflow.mvc.servlet.FlowHandlerMapping;
import org.springframework.webflow.mvc.servlet.FlowUrlHandler;
import org.springframework.webflow.mvc.view.MvcViewFactoryCreator;

@Configuration
public class WebFlowConfig implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Bean
    public InternalResourceViewResolver internalResourceViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        resolver.setOrder(0);
        return resolver;
    }

    @Bean
    public MvcViewFactoryCreator mvcViewFactoryCreator(ViewResolver viewResolver) {
        MvcViewFactoryCreator creator = new MvcViewFactoryCreator();
        creator.setViewResolvers(List.of(viewResolver));
        creator.setUseSpringBeanBinding(true);
        return creator;
    }

    @Bean
    public FlowBuilderServices flowBuilderServices(MvcViewFactoryCreator mvcViewFactoryCreator, Validator validator) {
        return FlowBuilderServices.builder()
                .setViewFactoryCreator(mvcViewFactoryCreator)
                .setValidator(validator)
                .build();
    }

    @Bean
    public FlowDefinitionRegistry flowDefinitionRegistry(FlowBuilderServices flowBuilderServices) {
        FlowDefinitionRegistryBuilder builder = new FlowDefinitionRegistryBuilder(applicationContext, flowBuilderServices);
        builder.addFlowLocation("classpath:/flows/transfer/transfer-flow.xml", "transfer");
        return builder.build();
    }

    @Bean
    public FlowExecutor flowExecutor(FlowDefinitionRegistry flowDefinitionRegistry) {
        FlowExecutorFactoryBean factoryBean = new FlowExecutorFactoryBean();
        factoryBean.setFlowDefinitionRegistry(flowDefinitionRegistry);
        factoryBean.setFlowExecutionFactory(new FlowExecutionImplFactory());
        try {
            factoryBean.afterPropertiesSet();
            FlowExecutor executor = factoryBean.getObject();
            if (executor == null) {
                throw new BeanCreationException("flowExecutor", "FlowExecutorFactoryBean returned null");
            }
            return executor;
        } catch (Exception ex) {
            throw new BeanCreationException("flowExecutor", "Unable to create FlowExecutor", ex);
        }
    }

    @Bean
    public FlowHandlerMapping flowHandlerMapping(FlowDefinitionRegistry flowDefinitionRegistry, FlowUrlHandler flowUrlHandler) {
        FlowHandlerMapping mapping = new FlowHandlerMapping();
        mapping.setOrder(-1);
        mapping.setFlowRegistry(flowDefinitionRegistry);
        mapping.setFlowUrlHandler(flowUrlHandler);
        return mapping;
    }

    @Bean
    public FlowHandlerAdapter flowHandlerAdapter(FlowExecutor flowExecutor, FlowBuilderServices flowBuilderServices,
            FlowUrlHandler flowUrlHandler) {
        FlowHandlerAdapter adapter = new FlowHandlerAdapter();
        adapter.setFlowExecutor(flowExecutor);
        adapter.setFlowUrlHandler(flowUrlHandler);
        adapter.setFlowBuilderServices(flowBuilderServices);
        adapter.setSaveOutputToFlashScopeOnRedirect(true);
        return adapter;
    }

    @Bean
    public FlowUrlHandler flowUrlHandler() {
        return new BasePathFlowUrlHandler("/app");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private static final class BasePathFlowUrlHandler implements FlowUrlHandler {

        private final String basePath;

        private BasePathFlowUrlHandler(String basePath) {
            this.basePath = basePath.endsWith("/") ? basePath.substring(0, basePath.length() - 1) : basePath;
        }

        @Override
        public String getFlowId(HttpServletRequest request) {
            String uri = request.getRequestURI();
            String contextPath = request.getContextPath();
            if (contextPath != null && !contextPath.isEmpty() && uri.startsWith(contextPath)) {
                uri = uri.substring(contextPath.length());
            }
            if (!uri.startsWith(basePath)) {
                return null;
            }
            String path = uri.substring(basePath.length());
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            return path;
        }

        @Override
        public String getFlowExecutionKey(HttpServletRequest request) {
            return request.getParameter("_flowExecutionKey");
        }

        @Override
        public String createFlowExecutionUrl(String flowId, String flowExecutionKey, HttpServletRequest request) {
            StringBuilder url = new StringBuilder();
            String contextPath = request.getContextPath();
            if (contextPath != null) {
                url.append(contextPath);
            }
            url.append(basePath).append('/').append(flowId);
            url.append("?_flowExecutionKey=").append(flowExecutionKey);
            return url.toString();
        }

        @Override
        public String createFlowDefinitionUrl(String flowId, AttributeMap<?> input, HttpServletRequest request) {
            StringBuilder url = new StringBuilder();
            String contextPath = request.getContextPath();
            if (contextPath != null) {
                url.append(contextPath);
            }
            url.append(basePath).append('/').append(flowId);
            return url.toString();
        }
    }
}
