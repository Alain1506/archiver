package com.javarush.task.task31.task3110.command;

import com.javarush.task.task31.task3110.ConsoleHelper;
import com.javarush.task.task31.task3110.ZipFileManager;
import com.javarush.task.task31.task3110.exception.PathIsNotFoundException;
import com.javarush.task.task31.task3110.exception.WrongZipFileException;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ZipExtractCommand extends ZipCommand {  //Команда распаковки архива
    @Override
    public void execute() throws Exception {

        try {
            ConsoleHelper.writeMessage("Распаковка архива.");
            ZipFileManager zipFileManager = getZipFileManager();
            ConsoleHelper.writeMessage("Введите полное имя директории, куда будем распаковывать архив:");
            Path path = Paths.get(ConsoleHelper.readString());
            zipFileManager.extractAll(path);
            ConsoleHelper.writeMessage("Архив был распакован.");
        } catch (WrongZipFileException e) {
            ConsoleHelper.writeMessage("Архив не существует.");
        }

    }
}
