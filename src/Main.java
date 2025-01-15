import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    static class DrHouse{
        int id;
        String Patient;
        String Symptom;
        String Diagnose;
        Date Datum;
        String Krankenhaus;

//        public DrHouse(int id, String patient, String symptom, String diagnose, Date datum, String krankenhaus) {
//            this.id = id;
//            Patient = patient;
//            Symptom = symptom;
//            Diagnose = diagnose;
//            Datum = datum;
//            Krankenhaus = krankenhaus;
//        }

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

        public Date getDatum() {
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

        try {
            List<DrHouse> tasks = parseJsonFile(filePath);
            tasks.forEach(System.out::println);

            System.out.println("Patients with name starting with 'A':");
            filterPatientsByChar('A', tasks);
            System.out.println("\nPatients with symptom 'Fieber' sorted by date:");
            sortSymptomFiberByDate(tasks);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static List<DrHouse> parseJsonFile(String filePath) throws IOException {
        // Read JSON content from file
        Path path = Path.of(filePath);
        // Parse the content into a JSON array
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            if (!reader.readLine().contains("[")) {
                throw new IOException("Invalid log file");
            }

            ArrayList<DrHouse> logs = new ArrayList<>();
            String nextLine = reader.readLine();

            while (!nextLine.contains("]")) {
                logs.add(readLog(reader, nextLine));

                nextLine = reader.readLine();
            }

            return logs;
        }
    }

    private static DrHouse readLog(BufferedReader reader, String firstLine) throws IOException {
        if (!firstLine.contains("{")) {
            throw new IOException("Invalid log file");
        }

        DrHouse log = new DrHouse();

        for (int i = 0; i < 7; i++) {
            readField(reader, log);
        }

        if (!reader.readLine().contains("}")) {
            throw new IOException("Invalid log file");
        }

        return log;
    }

    private static void readField(BufferedReader reader, DrHouse log) throws IOException {
        String line2 = reader.readLine();
        String fieldName = line2.split(":")[0].replace("\"", "").trim();

        switch (fieldName) {
            case "Id": {
                String s = line2.split(":")[1].replace(",", "").replace("\"", "").trim();
                int id = Integer.valueOf(s);
                log.id = id;
                break;
            }
            case "Patient": {
                String s = line2.split(":")[1].replace(",", "").replace("\"", "").trim();
                log.Patient = s;
                break;
            }
            case "Symptom": {
                String s = line2.split(":")[1].replace(",", "").replace("\"", "").trim();
                log.Symptom = s;
                break;
            }
            case "Diagnose": {
                String s = line2.split(":")[1].replace(",", "").replace("\"", "").trim();
                log.Diagnose = s;
                break;
            }
            case "Datum": {
                String s = line2.split(":")[1].replace(",", "").replace("\"", "").trim();
                log.Datum = new Date(s);
                break;
            }
            case "Krankenhaus": {
                String s = line2.split(":")[1].replace(",", "").replace("\"", "").trim();
                log.Krankenhaus = s;
                break;
            }

        }

    }

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

    public static void saveNrOfPatientsPerHospital(List<DrHouse> gruppen){
        gruppen.stream()
                .collect(Collectors.groupingBy(DrHouse::getKrankenhaus, Collectors.counting()))
                .forEach((key, value) -> System.out.println(key + ": " + value));
    }
}