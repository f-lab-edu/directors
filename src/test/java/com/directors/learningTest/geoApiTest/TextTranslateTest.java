package com.directors.learningTest.geoApiTest;

import org.junit.jupiter.api.Test;
import org.locationtech.proj4j.BasicCoordinateTransform;
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.ProjCoordinate;

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
                    ProjCoordinate projCoordinate = castPoint(split[5], split[6]);
                    if (split[2].length() != 0) {
                        translatedText.append(split[0]).append(' ')
                                .append(split[1]).append(' ')
                                .append(split[2]).append(",")
                                .append(split[2]).append(",")
                                .append(projCoordinate.x).append(",")
                                .append(projCoordinate.y).append('\n');
                    } else if (split[1].length() != 0) {
                        translatedText.append(split[0]).append(' ')
                                .append(split[1]).append(",")
                                .append(split[1]).append(",")
                                .append(projCoordinate.x).append(",")
                                .append(projCoordinate.y).append('\n');
                    }
                }
            } else {
                if (split.length == 7 && split[5].length() != 0 && split[6].length() != 0) {
                    ProjCoordinate projCoordinate = castPoint(split[5], split[6]);
                    if (split[3].length() != 0) {
                        translatedText.append(split[0]).append(' ')
                                .append(split[1]).append(' ')
                                .append(split[2]).append(' ')
                                .append(split[3]).append(",")
                                .append(split[3]).append(",")
                                .append(projCoordinate.x).append(",")
                                .append(projCoordinate.y).append('\n');
                    } else if (split[2].length() != 0) {
                        translatedText.append(split[0]).append(' ')
                                .append(split[1]).append(' ')
                                .append(split[2]).append(",")
                                .append(split[2]).append(",")
                                .append(projCoordinate.x).append(",")
                                .append(projCoordinate.y).append('\n');
                    }
                }
            }
        }
        writeFile(writeFile, translatedText.toString());
    }

    public static ProjCoordinate castPoint(String x, String y) {
        double dbLon = Double.parseDouble(x);
        double dbLat = Double.parseDouble(y);

        CRSFactory factory = new CRSFactory();
        CoordinateReferenceSystem wgs84 = factory.createFromParameters("EPSG:4326", "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs");
        CoordinateReferenceSystem utmK = factory.createFromParameters("UTM-K", "+proj=tmerc +lat_0=38 +lon_0=128 +k=0.9996 +x_0=400000 +y_0=600000 +ellps=GRS80 +units=m +no_defs");

        ProjCoordinate before = new ProjCoordinate(dbLon, dbLat);
        ProjCoordinate after = new ProjCoordinate();

        return new BasicCoordinateTransform(wgs84, utmK).transform(before, after);
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
