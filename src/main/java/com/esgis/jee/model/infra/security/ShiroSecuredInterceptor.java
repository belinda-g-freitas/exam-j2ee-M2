/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.esgis.jee.model.infra.security;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.Subject;

/**
 *
 * @author EL Capitain
 */
@Interceptor
@ShiroSecured
public class ShiroSecuredInterceptor implements Serializable {

    //private static final long serialVersionUID = 1L;

    @AroundInvoke
    public Object interceptShiroSecurity(InvocationContext context) throws Exception {
        Class<?> c = context.getTarget().getClass();
        Method m = context.getMethod();
        Subject subject = SecurityUtils.getSubject();

        if (!subject.isAuthenticated() && hasAnnotation(c, m, RequiresAuthentication.class)) {
            throw new UnauthenticatedException("Authentication required");
        }

        if (subject.getPrincipal() != null && hasAnnotation(c, m, RequiresGuest.class)) {
            throw new UnauthenticatedException("Guest required");
        }

        if (subject.getPrincipal() == null && hasAnnotation(c, m, RequiresUser.class)) {
            throw new UnauthenticatedException("User required");
        }

        RequiresRoles roles = getAnnotation(c, m, RequiresRoles.class);

        if (roles != null) {
            subject.checkRoles(Arrays.asList(roles.value()));
        }

        RequiresPermissions permissions = getAnnotation(c, m, RequiresPermissions.class);

        if (permissions != null) {
            subject.checkPermissions(permissions.value());
        }

        return context.proceed();
    }

    private static boolean hasAnnotation(Class<?> c, Method m, Class<? extends Annotation> a) {
        return m.isAnnotationPresent(a)
                || c.isAnnotationPresent(a)
                || c.getSuperclass().isAnnotationPresent(a);
    }

    private static <A extends Annotation> A getAnnotation(Class<?> c, Method m, Class<A> a) {
        return m.isAnnotationPresent(a) ? m.getAnnotation(a)
                : c.isAnnotationPresent(a) ? c.getAnnotation(a)
                : c.getSuperclass().getAnnotation(a);
    }

}
