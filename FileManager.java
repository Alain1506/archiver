package com.javarush.task.task31.task3110;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private Path rootPath;
    private List<Path> fileList; //список относительных путей файлов внутри rootPath

    public FileManager(Path rootPath) throws IOException {
        this.rootPath = rootPath;
        fileList = new ArrayList<>();
        collectFileList(rootPath);
    }

    public List<Path> getFileList() {
        return fileList;
    }

    private void collectFileList(Path path) throws IOException {

        if (Files.isRegularFile(path)) {
            fileList.add(rootPath.relativize(path));  //создаем список абсолютных путей файлов
        } else if (Files.isDirectory(path)) {
            DirectoryStream<Path> str = Files.newDirectoryStream(path);  //если передана папка, создаем список и из этих АП файлов
            for (Path path1 : str) {
                collectFileList(path1);
            }
            str.close();
        }
    }

}
