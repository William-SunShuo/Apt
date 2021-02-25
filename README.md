# Apt

介绍下依赖库auto-service
在使用注解处理器需要先声明，步骤：  
1、需要在 processors 库的 main 目录下新建 resources 资源文件夹;  
2、在 resources文件夹下建立 META-INF/services 目录文件夹   
3、在 META-INF/services 目录文件夹下创建 javax.annotation.processing.Processor文件;   
4、在 javax.annotation.processing.Processor 文件写入注解处理器的全称，包括包路径;  
这样声明下来也太麻烦了？这就是用引入auto-service的原因。  通过auto-service中的@AutoService可以自动生成AutoService注解处理器是Google开发的，用来生成 META-INF/services/javax.annotation.processing.Processor 文件的



build.gradle(:apt-precesser) dependencies中必须添加 annotationProcessor 'com.google.auto.service:auto-service:1.0-rc2'

dependencies {
    implementation 'com.squareup:javapoet:1.8.0'
    implementation 'com.google.auto.service:auto-service:1.0-rc2'
    annotationProcessor  'com.google.auto.service:auto-service:1.0-rc2'
    implementation project(path: ':aptlib')
}
