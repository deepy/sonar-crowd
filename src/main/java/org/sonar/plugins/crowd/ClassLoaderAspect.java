package org.sonar.plugins.crowd;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This will wrap all the public methods from this package and enforce the current thread
 * to use the class {@link ClassLoader} to execute the method before reverting back the
 * thread {@link ClassLoader}.
 * This is to add support for Java 11 where JAXB has been removed.
 * Had to add that as from "not really a good idea" in
 * https://stackoverflow.com/questions/51518781/jaxb-not-available-on-tomcat-9-and-java-9-10
 */
@Aspect
public class ClassLoaderAspect {
  private static final Logger LOG = LoggerFactory.getLogger(ClassLoaderAspect.class);

  @Pointcut("execution(public * org.sonar.plugins.crowd.*.*(..))")
  public void adjustClassLoader() {
    //pointcut nothing to do
  }

  @Around("adjustClassLoader()")
  public Object logging(ProceedingJoinPoint thisJoinPoint) throws Throwable {
    final ClassLoader classClassLoader = this.getClass().getClassLoader();
    LOG.debug("Uses class ClassLoader {} {}", thisJoinPoint.getSignature(), classClassLoader);
    final ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();
    try {
      // This will enforce the crowClient to use the plugin classloader
      Thread.currentThread().setContextClassLoader(classClassLoader);
      return thisJoinPoint.proceed();
    } finally {
      // Bring back the original class loader for the thread
      Thread.currentThread().setContextClassLoader(threadClassLoader);
      LOG.debug("Uses thread ClassLoader {} {}", thisJoinPoint.getSignature(), threadClassLoader);
    }
  }
}

