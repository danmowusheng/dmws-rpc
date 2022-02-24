package com.dmws.rpc.core.spring;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-12-03 15:23
 * @description： Rpc类扫描
 **/
public class RpcScanner extends ClassPathBeanDefinitionScanner {
    public RpcScanner(BeanDefinitionRegistry registry, Class<? extends Annotation>... annotationTypes) {
        /**
        * TODO: 需要弄清楚这里的用意是什么
        *
        * @author: LJ
        * @Date: 2021/12/3
        **/
        super(registry);
        for(Class<? extends Annotation> annotationType : annotationTypes){
            super.addIncludeFilter(new AnnotationTypeFilter(annotationType));
        }
    }
}
