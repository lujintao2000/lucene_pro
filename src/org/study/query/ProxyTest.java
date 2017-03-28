//package org.study.query;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.InvocationHandler;
//import java.lang.reflect.Method;
//import java.lang.reflect.Proxy;
//
//public class ProxyTest {
//
//	public static void main(String[] args){
//		Object query = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{Query.class}, new MyInvocationHandler());
//		String t = ((Query)query).search("大疆");
//		int a = 0;
//		a = a + 5;
//		System.out.println(a);
//	}
//	
//	private static class MyInvocationHandler implements InvocationHandler{
//
//		@Override
//		public Object invoke(Object proxy, Method arg1, Object[] arg2)
//				throws Throwable {
//			Class[] interface_array = proxy.getClass().getInterfaces();
//			Annotation[] annotations = arg1.getAnnotations();
//			return "hello";
//		}
//		
//	}
//	
//}
