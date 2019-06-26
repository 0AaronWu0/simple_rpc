package com.viewscenes.netsupervisor.configurer.rpc;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

/**
 *
 * @author MACHENIKE
 * @date 2018-12-03
 */
public class ClassPathRpcScanner extends ClassPathBeanDefinitionScanner{

    private RpcFactoryBean<?> rpcFactoryBean = new RpcFactoryBean<Object>();

    private Class<? extends Annotation> annotationClass;
    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public ClassPathRpcScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

        if (beanDefinitions.isEmpty()) {
            logger.warn("No RPC mapper was found in '"
                    + Arrays.toString(basePackages)
                    + "' package. Please check your configuration.");
        } else {
            processBeanDefinitions(beanDefinitions);
        }

        return beanDefinitions;
    }

    public void registerFilters() {
        boolean acceptAllInterfaces = true;

        if (this.annotationClass != null) {
            addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
            acceptAllInterfaces = false;
        }

        if (acceptAllInterfaces) {
            addIncludeFilter(new TypeFilter() {
                @Override
                public boolean match(MetadataReader metadataReader,
                                     MetadataReaderFactory metadataReaderFactory) {
                    return true;
                }
            });
        }

        // exclude package-info.java
        addExcludeFilter(new TypeFilter() {
            @Override
            public boolean match(MetadataReader metadataReader,
                                 MetadataReaderFactory metadataReaderFactory)
                    throws IOException {
                String className = metadataReader.getClassMetadata()
                        .getClassName();
                return className.endsWith("package-info");
            }
        });
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {

        GenericBeanDefinition definition;

        for (BeanDefinitionHolder holder : beanDefinitions) {

            definition = (GenericBeanDefinition) holder.getBeanDefinition();
            //将接口的名称添加到构造参数
            definition.getConstructorArgumentValues().addGenericArgumentValue(definition.getBeanClassName());
            //设置BeanDefinition的class
            //BeanFactory.getBean的方法跟进去后有一个判断是不是FactroyBean类型的。如果是从FactroyBean.getObejct获取
            //rpcFactoryBean 实现了FactoryBean
            definition.setBeanClass(this.rpcFactoryBean.getClass());

            definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
            System.out.println(holder);
        }
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }
}
