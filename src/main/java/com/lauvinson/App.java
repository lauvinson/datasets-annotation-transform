package com.lauvinson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class App extends SpringBootServletInitializer
{

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
    {
        return application.sources(AppConfiguration.class);
    }

//    public static void main(String[] args) throws Exception
//    {
//        SpringApplication application = new SpringApplication(App.class);
//        application.setApplicationContextClass(AnnotationConfigWebApplicationContext.class);
//        SpringApplication.run(App.class, args);
//    }

    /**
     * Caltech256 transform to baidu easydl
     */
    public static void main(String[] args) throws IOException {
        Path outRoot = Paths.get("D:\\vinson\\Desktop\\飞浆\\out");
        File root = Paths.get("D:\\vinson\\Desktop\\飞浆\\data_set\\image_\\256_ObjectCategories").toFile();
        File[] files = root.listFiles();
        for (File file : files) {
            String clazz = file.getName().substring(file.getName().indexOf(".") + 1);
            File[] images = file.listFiles();
            for (File image : images) {
                if (image.isDirectory()) {
                    continue;
                }
                String imageName = image.getName();
                if (!imageName.contains(".")) {
                    continue;
                }
                String imageNameNoReg = imageName.substring(0, imageName.lastIndexOf("."));
                String label = "{\"labels\": [{\"name\": \""+clazz+"\"}]}";
                String labelFileName = imageNameNoReg + ".json";

                //image
                FileInputStream in = new FileInputStream(image);
                outRoot.resolve(imageName).toFile().createNewFile();
                FileOutputStream out = new FileOutputStream(outRoot.resolve(imageName).toFile());// 指定要写入的图片
                int n = 0;// 每次读取的字节长度
                byte[] bb = new byte[1024];// 存储每次读取的内容
                while ((n = in.read(bb)) != -1) {
                    out.write(bb, 0, n);// 将读取的内容，写入到输出流当中
                }

                in.close();
                out.close();

                //label
                outRoot.resolve(labelFileName).toFile().createNewFile();
                FileWriter labelWriter = new FileWriter(outRoot.resolve(labelFileName).toFile(),false);
                labelWriter.write(label);
                labelWriter.close();
            }
        }
    }
}
