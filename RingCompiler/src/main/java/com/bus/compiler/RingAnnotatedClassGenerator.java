package com.bus.compiler;

import com.bus.ring.*;
import com.squareup.javapoet.*;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by HandGunBreak on 2016/4/13 - 14:22.
 * Mail: handgunbreak@gmail.com
 * Copyright: 杭州医本健康科技有限公司(2015-2016)
 * Description: 被@RpcService注解的元素所在类的新生成类的自动生成类
 */
class RingAnnotatedClassGenerator {

    /**
     * 生成 @RpcService注解的Field所在类的新类($$ThunderBinder)
     *
     * @param ringMethodsMap map
     * @param filer          filter
     */
    static void brewJava(Map<TypeElement, RingMethods> ringMethodsMap, Filer filer) {

        for (RingMethods ringMethods : ringMethodsMap.values()) {

            List<MethodSpec> methodSpecs = new ArrayList<>();

            //@Ring注解的方法
            List<RingMethodInfo> ringMethodInfo = ringMethods.mRingMethodInfo;
            MethodSpec buildRingObserverMethodSpec = null;
            if (ringMethodInfo != null) {

                if (ringMethodInfo.size() == 1) {

                    //构建生成RingObserverMethod方法
                    buildRingObserverMethodSpec = createBuildRingObserverMethod(ringMethodInfo.get(0), filer, ringMethods.mBindTargetClassName);
                } else if (ringMethodInfo.size() > 1) {

                    //构建生成List<RingObserverMethod>方法
                    buildRingObserverMethodSpec = createBuildRingObserversMethod(ringMethodInfo, filer, ringMethods.mBindTargetClassName);
                }
            }
            if (buildRingObserverMethodSpec != null) {

                methodSpecs.add(buildRingObserverMethodSpec);
            }

            //生成bind方法、方法修饰符、返回值
            methodSpecs.add(createBindMethod(ringMethods, filer, ringMethods.mBindTargetClassName, buildRingObserverMethodSpec));
            //生成unbind方法、方法修饰符、返回值
            methodSpecs.add(createUnbindMethod(ringMethods, filer, ringMethods.mBindTargetClassName));

            //生成类
            TypeSpec classTypeSpec = TypeSpec.classBuilder(ringMethods.getBoundClassName())
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addJavadoc("@{ was auto generated.}\n", ringMethods.getBoundClassName())
                    .addMethods(methodSpecs)
                    .addSuperinterface(ParameterizedTypeName.get(ClassName.get(RingBind.class), TypeVariableName.get("T")))
                    .addTypeVariable(TypeVariableName.get("T", ClassName.bestGuess(ringMethods.mBindTargetClassName)))
                    .build();

            try {

                JavaFile javaFile = JavaFile.builder(ringMethods.mBindTargetPackageName, classTypeSpec).build();
                if (RingConstants.DEBUG_MODEL) {

                    javaFile.writeTo(System.out);
                    MessageUtil.warning(null, javaFile.toString());
                }
                javaFile.writeTo(filer);
            } catch (IOException ioException) {

                ioException.printStackTrace();
                MessageUtil.warning(null, ioException.toString());
            }
        }
    }

    /**
     * 生成Bind方法
     *
     * @param ringMethods RpcField
     * @param filer       Filer
     * @return bind方法描述
     */

    private static MethodSpec createBindMethod(RingMethods ringMethods, Filer filer, String className, MethodSpec methodSpec) {

        MethodSpec.Builder result = MethodSpec.methodBuilder("bind")
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("\n@bind method was auto generated in .\n", className)
                .addParameter(TypeVariableName.get("T"), "object", Modifier.FINAL);

        if (methodSpec != null) {

            result.addStatement("$T.register(object, $N(object))", TypeName.get(RingBus.class), methodSpec);
        } else {

            result.addStatement("$T.register(object, null)", TypeName.get(RingBus.class));
        }
        return result.build();
    }

    /**
     * 生成解绑方法
     *
     * @param filer     Filer
     * @param className 所丰类类名
     * @return unbind方法描述
     */
    private static MethodSpec createUnbindMethod(RingMethods ringMethods, Filer filer, String className) {

        MethodSpec.Builder result = MethodSpec.methodBuilder("unbind")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeVariableName.get("T"), "object", Modifier.FINAL)
                .addStatement("$T.unregister(object)", TypeName.get(RingBus.class));

        result.addJavadoc("\n@bind method was auto generated in $S.class.\n", className);

        return result.build();
    }

    /**
     * 生成Bind方法
     *
     * @param ringMethodInfoList RpcField
     * @param filer              Filer
     * @return buildRingObserver方法描述
     */
    private static MethodSpec createBuildRingObserversMethod(List<RingMethodInfo> ringMethodInfoList, Filer filer, String className) {

        //List<RingObserver>
        TypeName parameterTypeName = ParameterizedTypeName.get(ClassName.get("java.util", "List"), ClassName.get(RingObserver.class));

        MethodSpec.Builder result = MethodSpec.methodBuilder(RingConstants.BUILD_RING_OBSERVERS)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("\n@bind method was auto generated in .\n", className)
                .returns(parameterTypeName)
                .addParameter(TypeVariableName.get("T"), "object", Modifier.FINAL);

        //List<RingObserver> ringObservers = new ArrayList<>();
        result.addStatement("$T ringObservers = new $T<>()", parameterTypeName, ClassName.get("java.util", "ArrayList"));
        for (RingMethodInfo ringMethodInfo : ringMethodInfoList) {

            TypeSpec ringObserver = TypeSpec.anonymousClassBuilder(String.format(Locale.getDefault(), "%s, \"%s\",  %s.class, %s.%s,  \"%s\"", "object", ringMethodInfo.mFilter, ringMethodInfo.mParamCanonicalName, DispatchPolicy.class.getCanonicalName(), ringMethodInfo.mDispatchPolicy, ringMethodInfo.mMethodName))
                    .addSuperinterface(ParameterizedTypeName.get(ClassName.bestGuess(RingObserver.class.getCanonicalName()), TypeVariableName.get("T"), TypeVariableName.get(ringMethodInfo.mParamCanonicalName)))
                    .addMethod(MethodSpec.methodBuilder("call")
                            .addAnnotation(Override.class)
                            .addModifiers(Modifier.PUBLIC)
                            .addParameter(TypeVariableName.get(ringMethodInfo.mParamCanonicalName), "value")
                            .addStatement(String.format("object.%s(value)", ringMethodInfo.mMethodName))
                            .build())
                    .build();
            result.addStatement("ringObservers.add($L)", ringObserver);
        }
        result.addStatement("return ringObservers");
        return result.build();
    }

    private static MethodSpec createBuildRingObserverMethod(RingMethodInfo ringMethodInfo, Filer filer, String className) {

        MethodSpec.Builder result = MethodSpec.methodBuilder(RingConstants.BUILD_RING_OBSERVER)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("\n@bind method was auto generated in .\n", className)
                .returns(TypeName.get(RingObserver.class))
                .addParameter(TypeVariableName.get("T"), "object", Modifier.FINAL);

        TypeSpec ringObserver = TypeSpec.anonymousClassBuilder(String.format(Locale.getDefault(), "%s, \"%s\",  %s.class, %s.%s,  \"%s\"", "object", ringMethodInfo.mFilter, ringMethodInfo.mParamCanonicalName, DispatchPolicy.class.getCanonicalName(), ringMethodInfo.mDispatchPolicy, ringMethodInfo.mMethodName))
                .addSuperinterface(ParameterizedTypeName.get(ClassName.bestGuess(RingObserver.class.getCanonicalName()), TypeVariableName.get("T"), TypeVariableName.get(ringMethodInfo.mParamCanonicalName)))
                .addMethod(MethodSpec.methodBuilder("call")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(TypeVariableName.get(ringMethodInfo.mParamCanonicalName), "value")
                        .addStatement(String.format("object.%s(value)", ringMethodInfo.mMethodName))
                        .build())
                .build();


        result.addStatement("$T ringObserver = $L", RingObserver.class, ringObserver)
                .addStatement("return ringObserver");
        return result.build();
    }


}
