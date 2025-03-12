package com.example.register;

public class Product {
    private String merk;
    private double harga;
    private int stok;
    private String foto;

    // Constructor
    public Product(String merk, double harga, int stok, String foto) {
        this.merk = merk;
        this.harga = harga;
        this.stok = stok;
        this.foto = foto;
    }

    // Getter
    public String getMerk() { return merk; }
    public double getHarga() { return harga; }
    public int getStok() { return stok; }
    public String getFoto() { return foto; }
}