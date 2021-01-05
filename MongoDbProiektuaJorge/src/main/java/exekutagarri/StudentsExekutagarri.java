/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exekutagarri;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import javax.swing.JFrame;
import model.Score;
import model.Student;
import org.bson.Document;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import views.BarraGrafikoa;

/**
 *
 * @author angulo.jorge
 */
public class StudentsExekutagarri {
    
    public static final String datubase = "Ikasleak";
    public static final String taula = "students";
    
    private static MongoClient mongoClient = MongoClients.create();
    private static CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));
    private static MongoDatabase db = mongoClient.getDatabase("Ikasleak").withCodecRegistry(pojoCodecRegistry);
    private static MongoCollection<Student> student_collection = db.getCollection("students", Student.class);
      
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean irten = false;
        int aukera; //Guardaremos la opcion del usuario
        JFrame barraGrafikoa = null; 
        
        while (!irten) {
            System.out.println("\n      IKASLEAK DATUBASEA");
            System.out.println("+------------------------------------+");
            System.out.println("|  1. Datuak Gorde                   |");
            System.out.println("|  2. Ezabatu bat                    |");
            System.out.println("|  3. Datu Guztiak Ikusi             |");
            System.out.println("|  4. Datuak Ikusi Zatika            |");
            System.out.println("|  5. Datu Bakarra Ikusi             |");
            System.out.println("|  6. Izen bat Aldatu                |");
            System.out.println("|  7. Jframe ikusi nota altuenak     |");
            System.out.println("|  8. Jframe ikusi nota baxuenak     |");
            System.out.println("|  9. Irten                          |");
            System.out.println("+------------------------------------+");

            System.out.println("Sartu zenbaki bat aukeratzeko: ");
            aukera = readInt(sc);

            switch (aukera) {
                case 1:
                    System.out.println("Sartu ikaslearen izena: ");
                    String name = sc.next();
                    double examnota;
                    double quiznota;
                    double homeworknota;
                    
                    try {
                        System.out.println("Sartu exam notaren media (0-100): ");
                        examnota = sc.nextDouble();
                        System.out.println("Sartu quiz notaren media (0-100): ");
                        quiznota = sc.nextDouble();
                        System.out.println("Sartu homework notaren media (0-100): ");
                        homeworknota = sc.nextDouble();
                    } catch (InputMismatchException e){
                        System.out.println("Zenbaki bat sartu behar duzu (0-100) artean!!");
                        break;
                    }
                    
                    if (examnota < 0 || examnota > 100 || quiznota < 0 || quiznota > 100 || homeworknota < 0 || homeworknota > 100) {
                        System.out.println("Zenbakia 0 eta 100 artean izan behar dira.");
                        break;
                    }
                    datuakGorde(name, examnota, quiznota, homeworknota);
                    break;
                case 2:
                    int ideza;
                    System.out.println("Sartu ikasle baten id-a ezabatzeko: ");
                    try{
                        ideza = sc.nextInt();
                    }catch(InputMismatchException e){
                        System.out.println("Zenbaki bat jarri behar duzu.");
                        break;
                    }                    
                    ezabatuBat(ideza);
                    break;
                case 3:
                    selectGuztia();
                    break;
                case 4:
                    int zenbat;
                    System.out.println("Sartu zenbat ikasle ikusi nahi duzu: ");
                    try{
                        zenbat = sc.nextInt();
                    }catch (InputMismatchException e){
                        System.out.println("Zenbaki bat jarri behar duzu.");
                        break;
                    }
                    for (Student student : datuakHartuZatika(zenbat, 0)) {
                        System.out.println(student);
                    }
                    break;
                case 5:
                    sc.nextLine(); // Para consumir el salto de linea anterior \n
                    System.out.println("Sartu ikasle baten izena: ");
                    String izena = sc.nextLine();
                    selectBakarraLehenengoa(izena);
                    break;
                case 6:
                    System.out.println("Sartu ikasle baten IDa izena aldatazeko: ");
                    int ikasleId = sc.nextInt();
                    System.out.println("Sartu zein izen nahi duzu: ");
                    String izenBerria = sc.next();
                    aldatuIkasleBatenIzena(ikasleId, izenBerria);
                    break;
                case 7:
                    if (barraGrafikoa != null) {
                        barraGrafikoa.dispose();
                    }
                    barraGrafikoa = new BarraGrafikoa(datuakHartuZatika(5, -1));
                    barraGrafikoa.setVisible(true);
                    break;
                case 8:
                    if (barraGrafikoa != null) {
                        barraGrafikoa.dispose();
                    }
                    barraGrafikoa = new BarraGrafikoa(datuakHartuZatika(5, 1));
                    barraGrafikoa.setVisible(true);
                    break;
                case 9:
                    irten = true;
                    break;
                default:
                    System.out.println("Zenbaki bat sartu behar duzu 1-9");
            }
        }
        
        sc.close();
    }
    
    private static int readInt(Scanner sc) {
        int value = -1;
        try {
            value = sc.nextInt();
        } catch (Exception e) {
            sc.nextLine(); // Limpio la linea que estaba mal (Limpiar el buffer)
            System.out.println("Zenbaki bat jarri behar duzu eta positiboa izan behar da.");
        }
        return value;
    }
    
    public static void datuakGorde(String name, double examnota, double quiznota, double homeworknota) {
        try {
            Score examn = new Score();
            examn.setType("exam");
            examn.setScore(examnota);
            
            Score quizn = new Score();
            quizn.setType("quiz");
            quizn.setScore(quiznota);
            
            Score homeworkn = new Score();
            homeworkn.setType("homework");
            homeworkn.setScore(homeworknota);

            //student_collection.find().max({ '_id': 1 }).hint({ '_id': 1});
            Bson sortBson = new BasicDBObject("_id", -1);
            Student lastStudent = student_collection.find().sort(sortBson).first();
            int id = 0;
            if (lastStudent != null) {
                id = lastStudent.getId() + 1;
            }

            Student newStudent = new Student( id, name, Arrays.asList(examn, quizn, homeworkn));

            student_collection.insertOne(newStudent);
            System.out.println("Ondo sartu da. ID honekin: "+ id);
            //System.out.println(students.find(eq("name", name)).first());
        } catch(Exception e) {
            System.err.println("Errore bat gertatu da.");
        }
    }
    
    public static void ezabatuBat(int id){
        try{            
            Student student = student_collection.find(eq("_id", id)).first();
            Document filterByGradeId = new Document("_id", student.getId());
            student_collection.deleteOne(filterByGradeId);
            System.out.println("Ondo ezabatu da");
        }catch(Exception e){
            System.err.println("Id hori ez da existitzen");
        }
    }
    
    public static void selectGuztia() {
        try {
            MongoCursor<Student> cursor = student_collection.find().iterator();        
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toString());
            }
            cursor.close();
        } catch (Exception e){
            System.err.println("Errore bat gertatu da");
        }
    }
    
    public static FindIterable<Student> datuakHartuZatika(int zenbat, int asc){
        FindIterable<Student> students = null;

        try {
            // MongoDB query
            // student_collection.find().max({ '_id': zenbat }).hint({ '_id': 1});

            Bson avgSortBson = new BasicDBObject("avgScore", asc);
            //Bson bson = new BsonDocument("_id", new BsonInt32(zenbat));
            //Bson hbson = new BsonDocument("_id", new BsonInt32(1));
            
            students = student_collection.find();
            
            if (asc != 0) {
                students = students.sort(avgSortBson);
            }

            students = students.limit(zenbat);
            
            /*for (Student student : students) {
                System.out.println(student);
            }*/

        } catch(Exception e) {
            System.err.println("Errore bat gertatu da");
        }
        
        return students;
    }
    
    public static void selectBakarraLehenengoa(String izena) {
        try{            
            Student student = student_collection.find(eq("name", izena)).first();
            
            if (student == null) {
                System.err.println("Ez dago izen hori datubasean");
            } else {
                System.out.println(student.toString());
            }
        }catch(NullPointerException e){
            System.err.println("Ez dago izen hori datubasean");
        }
    }

    public static void aldatuIkasleBatenIzena(int id, String izena){
        try{
            student_collection.updateOne(eq("_id", id), (set("name", izena)));
                    System.out.println("Izena aldatu egin da.");
        }catch (Exception e){
            System.err.println("Ez dago id hori datubasean");
        }
    }
}
