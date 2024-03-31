package com.javarush.task.task31.task3110;

import com.javarush.task.task31.task3110.exception.WrongZipFileException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Archiver {

    public static void main(String[] args) throws IOException {

        Operation operation = null;

        while(operation != Operation.EXIT) {
            try {
                operation = askOperation();
                CommandExecutor.execute(operation);
            } catch (WrongZipFileException e) {
                ConsoleHelper.writeMessage("Вы не выбрали файл архива или выбрали неверный файл.");
            } catch (Exception ex) {
                ConsoleHelper.writeMessage("Произошла ошибка. Проверьте введенные данные.");
            }
        }
    }

    public static Operation askOperation() throws IOException {

        ConsoleHelper.writeMessage("Выберите операцию:"
                + "\n0 - упаковать файлы в архив"
                + "\n1 - добавить файл в архив"
                + "\n2 - удалить файл из архива"
                + "\n3 - распаковать архив"
                + "\n4 - просмотреть содержимое архива"
                + "\n5 - выход");

        int numberOfEnum = ConsoleHelper.readInt();

        Map<Integer,Operation> listOfEnums = new HashMap<>();

        listOfEnums.put(0, Operation.CREATE);
        listOfEnums.put(1, Operation.ADD);
        listOfEnums.put(2, Operation.REMOVE);
        listOfEnums.put(3, Operation.EXTRACT);
        listOfEnums.put(4, Operation.CONTENT);
        listOfEnums.put(5, Operation.EXIT);

        return listOfEnums.get(numberOfEnum);
    }

}
