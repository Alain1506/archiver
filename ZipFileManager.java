package com.javarush.task.task31.task3110;

import com.javarush.task.task31.task3110.exception.PathIsNotFoundException;
import com.javarush.task.task31.task3110.exception.WrongZipFileException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipFileManager {

    private Path zipFile;

    public ZipFileManager(Path zipFile) {
        this.zipFile = zipFile;
    }

    public void createZip(Path source) throws Exception {

        if (Files.notExists(zipFile.getParent())) {
            Files.createDirectories(zipFile.getParent());
        }

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFile))) {

            if (Files.isRegularFile(source)) {
                addNewZipEntry(zipOutputStream, source.getParent(), source.getFileName());
            } else if (Files.isDirectory(source)) {
                FileManager fileManager = new FileManager(source);
                List<Path> fileNames = fileManager.getFileList();

                for (Path fileName : fileNames) {
                    addNewZipEntry(zipOutputStream, source, fileName);
                }
            } else {
                throw new PathIsNotFoundException();
            }

        }
    }

    private void addNewZipEntry(ZipOutputStream zipOutputStream, Path filePath, Path fileName) throws Exception {

        Path fullPath = filePath.resolve(fileName);  //создаем абсолютный путь файла для архивации

        try (InputStream inputStream = Files.newInputStream(fullPath)) {

            ZipEntry zipEntry = new ZipEntry(fileName.toString());
            zipOutputStream.putNextEntry(zipEntry); //начать запись

            copyData(inputStream, zipOutputStream);
            zipOutputStream.closeEntry(); //закончить запись
        }
    }

    private void copyData(InputStream in, OutputStream out) throws Exception {
        byte[] buffer = new byte[1000];
        int len;
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
    }

    public List<FileProperties> getFilesList() throws Exception {

        if (!Files.isRegularFile(zipFile)) {
            throw new WrongZipFileException();
        }

        List<FileProperties> list = new ArrayList<>();

        try (ZipInputStream inputStream = new ZipInputStream(Files.newInputStream(zipFile));
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            ZipEntry zipEntry;
            while ((zipEntry = inputStream.getNextEntry()) != null) {
                copyData(inputStream, baos);
                String name = zipEntry.getName(); //вычитываем проперти каждого файла
                long size = zipEntry.getSize();
                long compressedSize = zipEntry.getCompressedSize();
                int compressionMethod = zipEntry.getMethod();

                FileProperties fileProperties = new FileProperties(name, size, compressedSize, compressionMethod);
                list.add(fileProperties); //помещаем в список
            }
        }

        return list;
    }

    public void extractAll(Path outputFolder) throws Exception {

        if (!Files.isRegularFile(zipFile)) {
            throw new WrongZipFileException();
        }

        try (ZipInputStream inputStream = new ZipInputStream(Files.newInputStream(zipFile))) {

            if (Files.notExists(outputFolder)) {
                Files.createDirectories(outputFolder); // создаем путь для распаковки, если его не существует
            }

            ZipEntry zipEntry = inputStream.getNextEntry();
            while (zipEntry != null) {
                String name = zipEntry.getName();
                Path path = outputFolder.resolve(name); //создаем абсолютный путь файла

                Path parent = path.getParent();
                if (Files.notExists(parent)) {
                    Files.createDirectories(parent); //создаем еще путь, если родительской папки не существовало при создании пути outputFolder
                }

                try (OutputStream outputStream = Files.newOutputStream(path)) {
                    copyData(inputStream, outputStream);
                }
                zipEntry = inputStream.getNextEntry();

            }
        }
    }

    public void removeFiles(List<Path> pathList) throws Exception {

        if (!Files.isRegularFile(zipFile)) {
            throw new WrongZipFileException();
        }
        Path tempFile = Files.createTempFile(null, null);

        try (ZipInputStream inputStream = new ZipInputStream(Files.newInputStream(zipFile));
             ZipOutputStream outputStream = new ZipOutputStream(Files.newOutputStream(tempFile))) {

            ZipEntry zipEntry = inputStream.getNextEntry();

            while (zipEntry != null) {
                String name = zipEntry.getName();

                if (pathList.contains(Paths.get(name))) {
                    ConsoleHelper.writeMessage("Файл с именем " + name + " был удален");
                } else {
                    outputStream.putNextEntry(new ZipEntry(name));

                    copyData(inputStream, outputStream);

                    outputStream.closeEntry();
                    inputStream.closeEntry();
                }
                zipEntry = inputStream.getNextEntry();
            }
            Files.move(tempFile, zipFile, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public void removeFile(Path path) throws Exception {
        removeFiles(Collections.singletonList(path));
    }

    public void addFiles(List<Path> absolutePathList) throws Exception {

        if (!Files.isRegularFile(zipFile)) {
            throw new WrongZipFileException();
        }

        Path tempFile = Files.createTempFile(null, null); //создаем временный файл в директории по умолчанию

        try (ZipInputStream inputStream = new ZipInputStream(Files.newInputStream(zipFile));
             ZipOutputStream outputStream = new ZipOutputStream(Files.newOutputStream(tempFile))) {

            ZipEntry zipEntry = inputStream.getNextEntry(); //считываем файлы из имеющегося архива

            List<String> names = new ArrayList<>();

            while (zipEntry != null) {
                String name = zipEntry.getName();
                names.add(name);
                outputStream.putNextEntry(new ZipEntry(name)); //перезаписываем во временный файл
                copyData(inputStream, outputStream);
                outputStream.closeEntry();
                inputStream.closeEntry();
                zipEntry = inputStream.getNextEntry();
            }

            for (Path path : absolutePathList) {
                if (!Files.isRegularFile(path)) {
                    throw new PathIsNotFoundException();
                }
                if (names.contains(path.getFileName().toString())) {
                    ConsoleHelper.writeMessage("Данный файл уже находится в архиве");
                } else {
                    addNewZipEntry(outputStream, path.getParent(), path.getFileName());
                }
                ConsoleHelper.writeMessage("Файл " + path.getFileName() + " добавлен в архив");
            }
        }
        Files.move(tempFile, zipFile, StandardCopyOption.REPLACE_EXISTING); //возвращаем архив на место
    }


    public void addFile(Path absolutePath) throws Exception {
        addFiles(Collections.singletonList(absolutePath));
    }


}


