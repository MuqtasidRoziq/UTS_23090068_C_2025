package CRUD_23090068_C_2025;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CRUD_23090068_C_2025 {
    static final String uri = "mongodb://localhost:27017";
    static final String dbName = "uts_23090068_C_2025";
    static final String collectionName = "coll_23090068_C_2025";

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
            System.out.println("\n===== CRUD DATA SISWA =====");
            System.out.println("1. Tambah Data Siswa");
            System.out.println("2. Lihat Data Siswa");
            System.out.println("3. Ubah Data Siswa");
            System.out.println("4. Hapus Data Siswa");
            System.out.println("5. Cari Data Siswa");
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

    // Fungsi Create - Menambah data siswa dengan variasi struktur
    static void createData(Scanner input) {
        String lanjut;
        int docCount = 0;
        do {
            docCount++;
            System.out.println("\nInput Data Siswa ke-" + docCount);

            // Input data inti
            System.out.print("Nama: ");
            String nama = input.nextLine();
            System.out.print("NIS: ");
            String nis = input.nextLine();

            // Struktur dokumen berbeda berdasarkan urutan
            Document doc;
            if (docCount % 3 == 1) {
                // Dokumen 1: Nama, NIS, Kelas
                System.out.print("Kelas: ");
                String kelas = input.nextLine();
                doc = new Document("nama", nama)
                        .append("nis", nis)
                        .append("kelas", kelas);
            } else if (docCount % 3 == 2) {
                // Dokumen 2: Nama, NIS, Kelas, Alamat
                System.out.print("Kelas: ");
                String kelas = input.nextLine();
                System.out.print("Alamat: ");
                String alamat = input.nextLine();
                doc = new Document("nama", nama)
                        .append("nis", nis)
                        .append("kelas", kelas)
                        .append("alamat", alamat);
            } else {
                // Dokumen 3: Nama, NIS, Kelas, Nilai
                System.out.print("Kelas: ");
                String kelas = input.nextLine();
                System.out.print("Nilai: ");
                double nilai;
                try {
                    nilai = input.nextDouble();
                    input.nextLine(); // Buang newline
                } catch (Exception e) {
                    System.out.println("Nilai harus berupa angka!");
                    input.nextLine(); // Buang input yang salah
                    return;
                }
                doc = new Document("nama", nama)
                        .append("nis", nis)
                        .append("kelas", kelas)
                        .append("nilai", nilai);
            }

            collection.insertOne(doc);
            System.out.println("Data berhasil ditambahkan!");

            // Konfirmasi tambah data lagi
            System.out.print("Tambah data lagi? (Yes/No): ");
            lanjut = input.nextLine().trim().toLowerCase();
        } while (lanjut.equals("yes"));
    }

    // Fungsi Read - Menampilkan semua data dengan nomor urut
    static List<Document> readData() {
        System.out.println("\n=== Data Siswa ===");
        List<Document> documents = new ArrayList<>();
        int index = 1;
        for (Document doc : collection.find()) {
            documents.add(doc);
            System.out.println("[" + index + "] " + doc.toJson());
            index++;
        }
        if (documents.isEmpty()) {
            System.out.println("Tidak ada data!");
        }
        return documents;
    }

    // Fungsi Update - Ubah semua field berdasarkan nomor urut
    static void updateData(Scanner input) {
        // Tampilkan data dengan nomor urut
        List<Document> documents = readData();
        if (documents.isEmpty()) {
            return;
        }

        // Pilih data berdasarkan nomor urut
        System.out.print("Masukkan nomor urut siswa yang akan diupdate: ");
        int index;
        try {
            index = input.nextInt() - 1;
            input.nextLine(); // Buang newline
        } catch (Exception e) {
            System.out.println("Nomor urut harus berupa angka!");
            input.nextLine(); // Buang input yang salah
            return;
        }

        if (index < 0 || index >= documents.size()) {
            System.out.println("Nomor urut tidak valid!");
            return;
        }

        // Ambil dokumen yang akan diupdate
        Document existingDoc = documents.get(index);
        String existingNama = existingDoc.getString("nama");

        // Input data baru
        System.out.print("Masukkan Nama baru: ");
        String namaBaru = input.nextLine();
        System.out.print("Masukkan NIS baru: ");
        String nisBaru = input.nextLine();
        System.out.print("Masukkan Kelas baru: ");
        String kelasBaru = input.nextLine();

        // Update field inti
        Document updateFields = new Document("nama", namaBaru)
                .append("nis", nisBaru)
                .append("kelas", kelasBaru);

        // Update field tambahan jika ada
        if (existingDoc.containsKey("alamat")) {
            System.out.print("Masukkan Alamat baru: ");
            String alamatBaru = input.nextLine();
            updateFields.append("alamat", alamatBaru);
        } else if (existingDoc.containsKey("nilai")) {
            System.out.print("Masukkan Nilai baru: ");
            double nilaiBaru;
            try {
                nilaiBaru = input.nextDouble();
                input.nextLine(); // Buang newline
            } catch (Exception e) {
                System.out.println("Nilai harus berupa angka!");
                input.nextLine(); // Buang input yang salah
                return;
            }
            updateFields.append("nilai", nilaiBaru);
        }

        // Update dokumen di MongoDB
        collection.updateOne(Filters.eq("nama", existingNama), new Document("$set", updateFields));
        System.out.println("Data berhasil diupdate.");
    }

    // Fungsi Delete - Menghapus dokumen berdasarkan nomor urut
    static void deleteData(Scanner input) {
        // Tampilkan data dengan nomor urut
        List<Document> documents = readData();
        if (documents.isEmpty()) {
            return;
        }

        // Pilih data berdasarkan nomor urut
        System.out.print("Masukkan nomor urut siswa yang akan dihapus: ");
        int index;
        try {
            index = input.nextInt() - 1;
            input.nextLine(); // Buang newline
        } catch (Exception e) {
            System.out.println("Nomor urut harus berupa angka!");
            input.nextLine(); // Buang input yang salah
            return;
        }

        if (index < 0 || index >= documents.size()) {
            System.out.println("Nomor urut tidak valid!");
            return;
        }

        // Ambil dokumen yang akan dihapus
        Document docToDelete = documents.get(index);
        String namaToDelete = docToDelete.getString("nama");

        // Hapus dokumen
        long deletedCount = collection.deleteOne(Filters.eq("nama", namaToDelete)).getDeletedCount();
        if (deletedCount > 0) {
            System.out.println("Data berhasil dihapus.");
        } else {
            System.out.println("Data tidak ditemukan!");
        }
    }

    // Fungsi Searching - Cari berdasarkan kata kunci pada nama
    static void searchData(Scanner input) {
        System.out.print("Masukkan kata kunci nama: ");
        String keyword = input.nextLine();

        boolean found = false;
        for (Document doc : collection.find(Filters.regex("nama", ".*" + keyword + ".*", "i"))) {
            System.out.println(doc.toJson());
            found = true;
        }
        if (!found) {
            System.out.println("Data dengan kata kunci " + keyword + " tidak ditemukan!");
        }
    }
}