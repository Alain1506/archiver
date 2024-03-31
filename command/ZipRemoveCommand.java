package com.javarush.task.task31.task3110.command;

import com.javarush.task.task31.task3110.ConsoleHelper;
import com.javarush.task.task31.task3110.ZipFileManager;

import java.nio.file.Paths;

public class ZipRemoveCommand extends ZipCommand { //Команда удаления файла из архива
    @Override
    public void execute() throws Exception {

        ZipFileManager zipFileManager = getZipFileManager();
        ConsoleHelper.writeMessage("Какой файл нужно удалить?");
        String fileToRemove = ConsoleHelper.readString();
        zipFileManager.removeFile(Paths.get(fileToRemove));
        ConsoleHelper.writeMessage("Файл " + fileToRemove + " удален");

    }
}
