package com.zhjh.test;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/**
 * Created by 88888888 on 2020/12/3.
 */
@ContextConfiguration(locations= {"classpath:spring-application.xml"})
public class BasicTest extends AbstractTransactionalJUnit4SpringContextTests {

}
