package com.aires;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URL;
import java.util.Enumeration;

@B
public class Test {

	public static void main(String[] args) throws Exception {
		Enumeration<URL>urls = Test.class.getClassLoader().getResources("com");
		while(urls.hasMoreElements()){
			System.out.println(urls.nextElement());
		}
		
		System.out.println(Test.class.getAnnotation(A.class));
		System.out.println(B.class.getAnnotation(A.class));
		System.out.println(Test.class.getAnnotation(B.class).getClass().getInterfaces()[0].getAnnotation(A.class));
	}

}

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface A{
	
}
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@A
@interface B{
	
}
