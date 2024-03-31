package com.javarush.task.task31.task3110.command;

import com.javarush.task.task31.task3110.ConsoleHelper;
import com.javarush.task.task31.task3110.ZipFileManager;
import com.javarush.task.task31.task3110.exception.PathIsNotFoundException;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ZipAddCommand extends ZipCommand { //Команда добавления файла в архив
    @Override
    public void execute() throws Exception {

        try {
            ConsoleHelper.writeMessage("Добавление файла в архив.");
            ZipFileManager zipFileManager = getZipFileManager();
            ConsoleHelper.writeMessage("Введите полное имя файла для добавления в архив:");
            Path path = Paths.get(ConsoleHelper.readString());
            zipFileManager.addFile(path);
        } catch (PathIsNotFoundException e) {
            ConsoleHelper.writeMessage("Вы неверно указали имя файла или директории.");
        }
    }
}
