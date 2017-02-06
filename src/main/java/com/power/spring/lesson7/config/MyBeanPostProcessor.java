package com.power.spring.lesson7.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by shenli on 2017/2/6.
 */
@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("beanName = " + beanName);
        System.out.println("bean = " + bean);
        Method[] methods = bean.getClass().getMethods();
        Method[] declaredMethods = bean.getClass().getDeclaredMethods();
        if(beanName.equals("userServiceImpl")) {
            System.out.println("methods.length = " + methods.length + ",declaredMethods = " + declaredMethods.length);
            for (Method m : declaredMethods) {
                String name = m.getName();
                int modifiers = m.getModifiers();
                System.out.println("declaredMethods::ename=" + name + ",modifiers=" + modifiers);
            }
            for (Method m : methods) {
                String name = m.getName();
                int modifiers = m.getModifiers();
                System.out.println("methods::name=" + name + ",modifiers=" + modifiers);
            }
            //无法获取私有方法列表(类似javap -private)

        }
        return bean;
    }

}
