package com.directors.learningTest.geoApiTest;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class TextTranslateTest {

    @Test
    public void translateTextList() {
        String defaultPath = "C:\\Users\\admin\\Downloads\\위경도_좌표\\";
        String fileType = ".csv";
        List<String> fileList = Arrays.asList(
                "서울", "경기", "인천", "강원", "충북", "충남", "대전", "전북", "전남", "광주", "경북", "경남", "부산", "울산", "대구", "제주"
        );
        // Depth 2~3개 -> "세종" // 별도 처리 필요
        for (String file : fileList) {
            translateText(defaultPath + file + fileType, defaultPath + file + "_좌표" + fileType, false);
        }
        translateText(defaultPath + "세종" + fileType, defaultPath + "세종_좌표" + fileType, true);
    }


    public static void translateText(String readFile, String writeFile, Boolean isSejong) {
        StringBuffer translatedText = new StringBuffer();
        List<String> lines = readFileToLines(readFile);

        for (String line : lines) {
            String[] split = line.split(",");

            if (isSejong) {
                if (split.length == 7 && split[5].length() != 0 && split[6].length() != 0) {
                    if (split[2].length() != 0) {
                        translatedText.append(split[0]).append(' ')
                                .append(split[1]).append(' ')
                                .append(split[2]).append(",")
                                .append(split[2]).append(",")
                                .append(split[5]).append(',')
                                .append(split[6]).append('\n');
                    } else if (split[1].length() != 0) {
                        translatedText.append(split[0]).append(' ')
                                .append(split[1]).append(",")
                                .append(split[1]).append(",")
                                .append(split[5]).append(',')
                                .append(split[6]).append('\n');
                    }
                }
            } else {
                if (split.length == 7 && split[5].length() != 0 && split[6].length() != 0) {
                    if (split[3].length() != 0) {
                        translatedText.append(split[0]).append(' ')
                                .append(split[1]).append(' ')
                                .append(split[2]).append(' ')
                                .append(split[3]).append(",")
                                .append(split[3]).append(",")
                                .append(split[5]).append(",")
                                .append(split[6]).append('\n');
                    } else if (split[2].length() != 0) {
                        translatedText.append(split[0]).append(' ')
                                .append(split[1]).append(' ')
                                .append(split[2]).append(",")
                                .append(split[2]).append(",")
                                .append(split[5]).append(',')
                                .append(split[6]).append('\n');
                    }
                }
            }
        }
        writeFile(writeFile, translatedText.toString());
    }

    public static List<String> readFileToLines(String path) {
        List<String> lines = null;

        try {
            lines = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    public static void writeFile(String path, String text) {
        try {
            Files.write(Paths.get(path), text.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
