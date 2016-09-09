package com.bus.compiler;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

/**
 * Created by handgunbreak@gmail.com on 16/7/16.
 * Mail: handgunbreak@gmail.com
 * Copyright: handgunbreak@gmail.com(2016)
 * Description: 消息打印工具类
 */
class MessageUtil {

    private static Messager messager;

    public static void setMessageUtil(Messager messager) {

        MessageUtil.messager = messager;
    }

    /**
     * 打印错误消息
     *
     * @param element 程序元素
     * @param message 格式化消息内容
     * @param args    参数
     */
    public static void error(Element element, String message, Object... args) {

        if (messager != null) {

            if (args.length > 0) {

                message = String.format(message, args);
            }
            messager.printMessage(Kind.ERROR, message, element);
        }
    }

    /**
     * 打印警告消息
     *
     * @param element 程序元素
     * @param message 格式化消息内容
     * @param args    参数
     */
    public static void warning(Element element, String message, Object... args) {

        if (messager != null) {

            if (args.length > 0) {

                message = String.format(message, args);
            }
            messager.printMessage(Kind.WARNING, message, element);
        }
    }


}
