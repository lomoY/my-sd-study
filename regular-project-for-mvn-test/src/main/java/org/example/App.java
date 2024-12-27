package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        System.out.println( "Hello Wlkjorldhjg!" );
        System.out.println(11888822);
        System.out.println("Current Working Directory: " + System.getProperty("user.dir"));
        writeObject();
    }

    static void readFile() throws IOException {

        byte[] data = Files.readAllBytes(Paths.get("src/main/resources/jokerData/testdata.json"));
        ObjectMapper objectMapper = new ObjectMapper();
        Human human = objectMapper.readValue(data,Human.class);
    }

    static void writeObject() throws IOException{
        try {
            class Person {
                public String name;
                public int age;

                public Person(String name, int age) {
                    this.name = name;
                    this.age = age;
                }
            }
            Person person = new Person("John Doe", 30);
            String resourcePath = "src/main/resources/person.json";
            // Ensure the directory exists
            File outputFile = new File(resourcePath);
            File parentDir = outputFile.getParentFile();
            if (!parentDir.exists()) {
                boolean dirCreated = parentDir.mkdirs();
                if (!dirCreated) {
                    throw new IOException("Failed to create directory: " + parentDir);
                }
            }

            // Write object as JSON using ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(outputFile, person);

            System.out.println("Object written to: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
