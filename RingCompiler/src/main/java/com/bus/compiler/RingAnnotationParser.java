package com.bus.compiler;import com.bus.ring.Ring;import javax.annotation.processing.Filer;import javax.annotation.processing.RoundEnvironment;import javax.lang.model.element.*;import javax.lang.model.util.Elements;import javax.lang.model.util.Types;import java.util.*;import static javax.lang.model.element.ElementKind.CLASS;import static javax.lang.model.element.ElementKind.METHOD;import static javax.lang.model.element.Modifier.PRIVATE;import static javax.lang.model.element.Modifier.STATIC;/** * Created by handgunbreak@gmail.com on 16/7/16. * Mail: handgunbreak@gmail.com * Copyright: handgunbreak@gmail.com(2016) * Description: 注解解析类 */class RingAnnotationParser {    private static RingAnnotationParser mInstance;    private Elements mElementUtil;    private RingAnnotationParser() {    }    /**     * 单例     *     * @return RpcParser实例     */    static RingAnnotationParser getInstance() {        if (mInstance == null) {            synchronized (RingAnnotationParser.class) {                if (mInstance == null) {                    mInstance = new RingAnnotationParser();                }            }        }        return mInstance;    }    /**     * 解析     *     * @param roundEnvironment env     * @param filer            filer     * @param types            types     * @param elements         elements     */    public void parser(RoundEnvironment roundEnvironment, Filer filer, Types types, Elements elements) {        mElementUtil = elements;        //缓存RpcAnnotatedInterfaceInfo对象        Map<TypeElement, RingMethods> ringMethodsLinkedHashMap = new LinkedHashMap<>();        //解析@Rpc注解的接口类        for (Element element : roundEnvironment.getElementsAnnotatedWith(Ring.class)) {            //@Ring 注解所在类对象            TypeElement ringTypeElement = (TypeElement) element.getEnclosingElement();            //注解所在类对象必须是类            if (ringTypeElement.getKind() != CLASS) {                MessageUtil.error(element, String.format(Locale.getDefault(), "%s must be class.", element.getSimpleName().toString()));                return;            }            //类修饰符不能是private及static            Set<Modifier> modifiers = ringTypeElement.getModifiers();            if (modifiers.contains(PRIVATE) || modifiers.contains(STATIC)) {                MessageUtil.error(element, "%s cannot be private or static class.", element.getSimpleName().toString());                continue;            }            //Ring 只支持方法注解            if (!(element instanceof ExecutableElement) || element.getKind() != METHOD) {                MessageUtil.error(element, String.format(Locale.getDefault(), "%s not method type.", element.getSimpleName().toString()));                continue;            }            Ring ringAnnotation = element.getAnnotation(Ring.class);            //方法Element            ExecutableElement ringExecutableElement = (ExecutableElement) element;            //方法Element 修饰符            modifiers = ringExecutableElement.getModifiers();            if (modifiers.contains(PRIVATE) || modifiers.contains(STATIC)) {                MessageUtil.error(element, "%s cannot be private or static method.", element.getSimpleName().toString());                continue;            }            //针对一个RingTypeElement即注解所在对象, 生成RingMethods对象            RingMethods ringMethods = ringMethodsLinkedHashMap.get(ringTypeElement);            if (ringMethods == null) {                String packageName = getPackageName(ringTypeElement);                ringMethods = new RingMethods(getClassName(ringTypeElement, packageName), packageName);                ringMethodsLinkedHashMap.put(ringTypeElement, ringMethods);                ringMethods.mRingMethodInfo = new ArrayList<>();            }            RingMethodInfo ringMethodInfo = new RingMethodInfo();            ringMethods.mRingMethodInfo.add(ringMethodInfo);            //方法名            ringMethodInfo.mMethodName = ringExecutableElement.getSimpleName().toString();            //过滤关键字            ringMethodInfo.mFilter = ringAnnotation.filter();            //线程运行            ringMethodInfo.mDispatchPolicy = ringAnnotation.policy();            //方法参数            List<? extends VariableElement> parameters = ringExecutableElement.getParameters();            //方法参数,现只支持一个参数            if (parameters != null && parameters.size() == 1) {                VariableElement variableElement = parameters.get(0);                ringMethodInfo.mParamName = variableElement.toString();                ringMethodInfo.mParamCanonicalName = variableElement.asType().toString();            }        }        RingAnnotatedClassGenerator.brewJava(ringMethodsLinkedHashMap, filer);    }    /**     * 获取类名     *     * @param type        TypeElement对象     * @param packageName 包名     * @return 类名     */    private String getClassName(TypeElement type, String packageName) {        return type.getQualifiedName().toString().substring(packageName.length() + 1);    }    /**     * 获取某个类型的包名     *     * @param type TypeElement对象     * @return 包名     */    private String getPackageName(TypeElement type) {        return mElementUtil.getPackageOf(type).getQualifiedName().toString();    }}