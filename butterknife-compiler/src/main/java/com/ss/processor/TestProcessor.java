package com.ss.processor;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.ss.aptlib.BindView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;

//注册处理器，告知APT，AutoCrate是由TestProcess处理
@AutoService(Processor.class)
public class TestProcessor extends AbstractProcessor {

    private Filer filer;
    private Elements elementsUtils;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        //定义需要处理的注解
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(BindView.class.getCanonicalName());
        return annotations;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        elementsUtils = processingEnv.getElementUtils();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        //文件内容--java代码生成工具javapoet
        //MethodSpec:定义方法
        //TypeSec:定义类
        //JavaFile：生成.java文件
        Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(BindView.class);
        Map<Element, List<Element>> elementListMap = new HashMap<>();
        List<Element> list;
        for (Element element : set) {
            Element enclosingElement = element.getEnclosingElement();
            if (elementListMap.get(enclosingElement) == null) {
                list = new ArrayList<>();
                elementListMap.put(enclosingElement, list);
            } else {
                list = elementListMap.get(enclosingElement);
            }
            list.add(element);
        }

        for (Map.Entry<Element, List<Element>> entry : elementListMap.entrySet()) {
            Element enclosingElement = entry.getKey();
            List<Element> elements = entry.getValue();

            String name = enclosingElement.getSimpleName().toString();
            TypeName enclosingTypeName = ClassName.bestGuess(name);
            //生成target属性
            FieldSpec targetField = FieldSpec.builder(enclosingTypeName, "target")
                    .addModifiers(Modifier.PRIVATE)
                    .build();

            //一个参数的构造方法
            ClassName UiThreadClassName = ClassName.get("androidx.annotation", "UiThread");
            MethodSpec constructor1 = MethodSpec.constructorBuilder()
                    .addAnnotation(UiThreadClassName)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(enclosingTypeName, "target")
                    .addStatement("this(target, target.getWindow().getDecorView())")
                    .build();

            //两个参数的构造方法
            ClassName viewClassName = ClassName.get("android.view", "View");
            MethodSpec.Builder constructor2Builder = MethodSpec.constructorBuilder()
                    .addAnnotation(UiThreadClassName)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(enclosingTypeName, "target")
                    .addParameter(viewClassName, "source")
                    .addStatement("this.target = target");
            for (Element element : elements) {
                constructor2Builder.addStatement("target.$L = source.findViewById($L)", element.getSimpleName(), element.getAnnotation(BindView.class).value());
            }

            MethodSpec constructor2 = constructor2Builder.build();

            //unbind方法
            ClassName callSuperClassName = ClassName.get("androidx.annotation", "CallSuper");
            MethodSpec.Builder unbindBuilder = MethodSpec.methodBuilder("unbind")
                    .addAnnotation(Override.class)
                    .addAnnotation(callSuperClassName)
                    .returns(TypeName.VOID)
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("$T target = this.target", enclosingTypeName)
                    .addStatement("if (target == null) throw new IllegalStateException(\"Bindings already cleared.\")")
                    .addStatement("this.target = null");
            for (Element element : elements) {
                unbindBuilder.addStatement("target.$L = null", element.getSimpleName());
            }

            MethodSpec unbind = unbindBuilder.build();
            //unBinder接口名
            ClassName unBinderInterfaceName = ClassName.get("com.example.apt", "Unbinder");

            TypeSpec typeSpec = TypeSpec.classBuilder(name + "_ViewBinding")
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(unBinderInterfaceName)
                    .addField(targetField)
                    .addMethod(constructor1)
                    .addMethod(constructor2)
                    .addMethod(unbind)
                    .build();

            String packageName = elementsUtils.getPackageOf(enclosingElement).getQualifiedName().toString();
            JavaFile javaFile = JavaFile.builder(packageName, typeSpec).build();
            try {
                javaFile.writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    @Override
    public Set<String> getSupportedOptions() {
        return super.getSupportedOptions();
    }
}
