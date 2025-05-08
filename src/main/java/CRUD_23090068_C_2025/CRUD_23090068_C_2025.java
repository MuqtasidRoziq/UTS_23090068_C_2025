package CRUD_23090068_C_2025;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.Scanner;

public class CRUD_23090068_C_2025 {
    static final String uri = "mongodb://localhost:27017";
    static final String dbName = "uts_23090068_C_2025";
    static final String collectionName = "cool_23090068_C_2025";

    static MongoCollection<Document> collection;

    public static void main(String[] args) {
        // Koneksi ke MongoDB
        MongoClient client = MongoClients.create(uri);
        MongoDatabase database = client.getDatabase(dbName);
        collection = database.getCollection(collectionName);

        Scanner input = new Scanner(System.in);
        int pilihan;

        // Menu Utama
        do {
            System.out.println("\n===== CRUD PESANAN ES TEH KITA =====");
            System.out.println("1. Tambah Pesanan");
            System.out.println("2. Lihat Pesanan");
            System.out.println("3. Ubah Pesanan");
            System.out.println("4. Hapus Pesanan");
            System.out.println("5. Cari Pesanan");
            System.out.println("0. Keluar");
            System.out.print("Pilih: ");
            pilihan = input.nextInt();
            input.nextLine(); // Buang newline

            switch (pilihan) {
                case 1 -> createData(input);
                case 2 -> readData();
                case 3 -> updateData(input);
                case 4 -> deleteData(input);
                case 5 -> searchData(input);
                case 0 -> System.out.println("Keluar...");
                default -> System.out.println("Pilihan tidak valid!");
            }
        } while (pilihan != 0);

        input.close();
        client.close();
    }

    // Fungsi Create - Menambah 3 dokumen berbeda
    static void createData(Scanner input) {
        for (int i = 0; i < 3; i++) {
            System.out.println("\nInput Data ke-" + (i + 1));
            System.out.print("Nama: ");
            String nama = input.nextLine();
            System.out.print("Prodi: ");
            String prodi = input.nextLine();
            System.out.print("IPK: ");
            double ipk = input.nextDouble();
            input.nextLine(); // buang newline

            Document doc = new Document("nama", nama)
                    .append("prodi", prodi)
                    .append("ipk", ipk);

            collection.insertOne(doc);
            System.out.println("Data berhasil ditambahkan!");
        }
    }

    // Fungsi Read - Menampilkan semua data
    static void readData() {
        System.out.println("\n=== Data Mahasiswa ===");
        for (Document doc : collection.find()) {
            System.out.println(doc.toJson());
        }
    }

    // Fungsi Update - Ubah field "ipk" berdasarkan nama
    static void updateData(Scanner input) {
        System.out.print("Masukkan nama mahasiswa yang akan diupdate: ");
        String nama = input.nextLine();
        System.out.print("Masukkan IPK baru: ");
        double ipkBaru = input.nextDouble();
        input.nextLine();

        collection.updateOne(Filters.eq("nama", nama), new Document("$set", new Document("ipk", ipkBaru)));
        System.out.println("Data berhasil diupdate.");
    }

    // Fungsi Delete - Menghapus dokumen berdasarkan nama
    static void deleteData(Scanner input) {
        System.out.print("Masukkan nama mahasiswa yang akan dihapus: ");
        String nama = input.nextLine();

        collection.deleteOne(Filters.eq("nama", nama));
        System.out.println("Data berhasil dihapus.");
    }

    // Fungsi Searching - Cari berdasarkan kata kunci pada nama
    static void searchData(Scanner input) {
        System.out.print("Masukkan kata kunci nama: ");
        String keyword = input.nextLine();

        for (Document doc : collection.find(Filters.regex("nama", ".*" + keyword + ".*", "i"))) {
            System.out.println(doc.toJson());
        }
    }
}
