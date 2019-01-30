package org.windwant.spring.service;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchUnitRunner;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

/**
 * Created by Administrator on 18-12-3.
 */
@RunWith(ArchUnitRunner.class)
@AnalyzeClasses(packages = "org.windwant.spring.service")
public class ServiceArchTest {

    @ArchTest
    public void testService(JavaClasses ckasszs){
        JavaClasses imported = new ClassFileImporter().importPackages("org.windwant.spring");
        JavaClass service = imported.get(BootService.class);
        System.out.println(service.getName());

        //服务类位置检查
        ArchRule rule = classes().that().haveSimpleNameEndingWith("Impl").should()
                .resideInAPackage("org.windwant.spring.service.impl");
        rule.check(imported);

        //调用判断
        ArchRule rule1 = classes().that().resideInAPackage("org.windwant.spring.service.impl")
                        .should().onlyBeAccessed().byAnyPackage("org.windwant.spring.controller。rest");
        rule1.check(imported);

        //配置类位置判断
        ArchRule rule2 = classes().that().areAnnotatedWith(Configuration.class)
                .should().resideInAnyPackage("org.windwant.spring.config", "org.windwant.spring.websocket");
        rule2.check(imported);

        //工具类可访问性
        ArchRule rule3 = classes().that().resideInAnyPackage("org.windwant.spring.util")
                .should().notBePrivate();
        rule3.check(imported);
    }


    @Test
    public void testHello() throws Exception {
    }

    @Test
    public void testHellox() throws Exception {

    }

    @Test
    public void testTestMongo() throws Exception {

    }

    @Test
    public void testGetScoreById() throws Exception {

    }

    @Test
    public void testGetStuById() throws Exception {

    }

    @Test
    public void testGetStu() throws Exception {

    }
}