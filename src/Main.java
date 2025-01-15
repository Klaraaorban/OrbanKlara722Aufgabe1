import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    /**
     * Class representing a DrHouse object
     */
    static class DrHouse{
        int id;
        String Patient;
        String Symptom;
        String Diagnose;
        String Datum;
        String Krankenhaus;


        public DrHouse(int id, String patient, String symptom, String diagnose, String datum, String krankenhaus) {
            this.id = id;
            Patient = patient;
            Symptom = symptom;
            Diagnose = diagnose;
            Datum = datum;
            Krankenhaus = krankenhaus;
        }

        public int getId() {
            return id;
        }

        public String getPatient() {
            return Patient;
        }

        public String getSymptom() {
            return Symptom;
        }

        public String getDiagnose() {
            return Diagnose;
        }

        public String getDatum() {
            return Datum;
        }

        public String getKrankenhaus() {
            return Krankenhaus;
        }

        @Override
        public String toString() {
            return "DrHouse{" +
                    "id=" + id +
                    ", Patient='" + Patient + '\'' +
                    ", Symptom='" + Symptom + '\'' +
                    ", Diagnose='" + Diagnose + '\'' +
                    ", Datum=" + Datum +
                    ", Krankenhaus='" + Krankenhaus + '\'' +
                    '}';
        }
    }

    public static void main(String[] args) {
        String filePath = "fallakten.json"; // Path to the JSON file

        List<DrHouse> tasks = readFromFileJson(filePath);
        tasks.forEach(System.out::println);
        System.out.println("Patients with name starting with 'J':");
        filterPatientsByChar('J', tasks);
        System.out.println("\nPatients with symptom 'Fieber' sorted by date:");
        sortSymptomFiberByDate(tasks);

    }
//
//    public static List<DrHouse> parseJsonFile(String filePath) throws IOException {
//        // Read JSON content from file
//        Path path = Path.of(filePath);
//        // Parse the content into a JSON array
//        try (BufferedReader reader = Files.newBufferedReader(path)) {
//
//
//            ArrayList<DrHouse> logs = new ArrayList<>();
//            String nextLine = reader.readLine();
//
//            while (!nextLine.contains("]")) {
//                logs.add(readLog(reader, nextLine));
//
//                nextLine = reader.readLine();
//            }
//
//            return logs;
//        }
//    }
//
//    private static DrHouse readLog(BufferedReader reader, String firstLine) throws IOException {
//
//        DrHouse log = new DrHouse();
//
//        for (int i = 0; i < 6; i++) {
//            readField(reader, log);
//        }
//
//        if (!reader.readLine().contains("}")) {
//            throw new IOException("Invalid log file1");
//        }
//
//        return log;
//    }
//
//    private static void readField(BufferedReader reader, DrHouse log) throws IOException {
//        String line2 = reader.readLine();
//        String fieldName = line2.split(":")[0].replace("\"", "").trim();
//
//        switch (fieldName) {
//            case "Id": {
//                String s = line2.split(":")[1].replace(",", "").replace("\"", "").trim();
//                int id = Integer.valueOf(s);
//                log.id = id;
//                break;
//            }
//            case "Patient": {
//                String s = line2.split(":")[1].replace(",", "").replace("\"", "").trim();
//                log.Patient = s;
//                break;
//            }
//            case "Symptom": {
//                String s = line2.split(":")[1].replace(",", "").replace("\"", "").trim();
//                log.Symptom = s;
//                break;
//            }
//            case "Diagnose": {
//                String s = line2.split(":")[1].replace(",", "").replace("\"", "").trim();
//                log.Diagnose = s;
//                break;
//            }
//            case "Datum": {
//                String s = line2.split(":")[1].replace(",", "").replace("\"", "").trim();
//                log.Datum = s;
//                break;
//            }
//            case "Krankenhaus": {
//                String s = line2.split(":")[1].replace(",", "").replace("\"", "").trim();
//                log.Krankenhaus = s;
//                break;
//            }
//
//        }
//        String readLine = reader.readLine();
//
//    }

    public static void filterPatientsByChar(Character c, List<DrHouse> gruppen) {
        gruppen.stream().filter(gruppe -> gruppe.getPatient().charAt(0) == c)
                .map(DrHouse::getPatient).forEach(System.out::println);
    }

    public static void sortSymptomFiberByDate(List<DrHouse> gruppen){
        gruppen.stream()
                .filter(gruppe -> gruppe.getSymptom().equals("Fieber"))
                .sorted((a, b) -> a.getDatum().compareTo(b.getDatum()))
                .forEach(gruppe -> System.out.println(gruppe.getPatient()));
    }

//    public static void saveNrOfPatientsPerHospital(List<DrHouse> gruppen){
//        Map<String, Integer> hausPunkte = gruppen.stream()
//                .collect(Collectors.groupingBy(DrHouse::getPatient));
//
//    }

    public static List<DrHouse> readFromFileJson(String file) {
        List<DrHouse> avengers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonContent.append(line);
            }
            String jsonString = jsonContent.toString();
            jsonString = jsonString.substring(1, jsonString.length() - 1); // Remove the outer brackets
            String[] jsonObjects = jsonString.split("\\},\\{");
            for (String jsonObject : jsonObjects) {
                jsonObject = jsonObject.replace("{", "").replace("}", "");
                String[] fields = jsonObject.split(",");
                int id = Integer.parseInt(fields[0].split(":")[1].trim());
                String patient = fields[1].split(":")[1].replace("\"", "").trim();
                String symptom = fields[2].split(":")[1].replace("\"", "").trim();
                String diagnose = fields[3].split(":")[1].replace("\"", "").trim();
                String datum = fields[4].split(":")[1].replace("\"", "").trim();
                String Krankenhaus = fields[5].split(":")[1].trim();
                DrHouse avenger = new DrHouse(id, patient, symptom, diagnose, datum, Krankenhaus);
                avengers.add(avenger);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return avengers;
    }

}