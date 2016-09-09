package com.bus.compiler;

import com.bus.ring.Ring;
import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by handgunbreak@gmail.com on 16/7/16.
 * Mail: handgunbreak@gmail.com
 * Copyright: handgunbreak@gmail.com(2016)
 * Description: Processor类
 */
@SuppressWarnings("unused")
@AutoService(Processor.class)
public class RingProcessor extends AbstractProcessor {

    private static LinkedHashSet<Class<? extends Annotation>> mOptionalAnnotationHashSet = new LinkedHashSet<>();
    private ProcessingEnvironment mProcessingEnvironment;

    /**
     * 初始化，系统调用
     *
     * @param processingEnvironment 编译期解析环境
     */
    @Override
    public void init(ProcessingEnvironment processingEnvironment) {

        super.init(processingEnvironment);
        this.mProcessingEnvironment = processingEnvironment;
        MessageUtil.setMessageUtil(processingEnvironment.getMessager());
    }

    /**
     * 编译期注解解析方法
     *
     * @param annotations 被注解元素集
     * @param roundEnv    env
     * @return 处理结果
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        RingAnnotationParser.getInstance().parser(roundEnv, mProcessingEnvironment.getFiler(), mProcessingEnvironment.getTypeUtils(), mProcessingEnvironment.getElementUtils());
        return true;
    }

    /**
     * 定义支持注解类型
     *
     * @return 注解类型集
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {

        Set<String> annotationTypes = new LinkedHashSet<>();
        annotationTypes.add(Ring.class.getCanonicalName());
        return annotationTypes;
    }

    /**
     * 支持注解类型编译版本号
     *
     * @return 版本号
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {

        return SourceVersion.latest();
    }

}
