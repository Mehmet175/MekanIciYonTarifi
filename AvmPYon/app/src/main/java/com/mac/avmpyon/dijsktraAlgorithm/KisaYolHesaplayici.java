package com.mac.avmpyon.dijsktraAlgorithm;

import android.util.Log;

import com.mac.avmpyon.model.Dugum;
import com.mac.avmpyon.model.KomsuDugum;

import java.util.ArrayList;
import java.util.List;

public class KisaYolHesaplayici {
    ArrayList<Dugum> dugums;
    String baslangicNoktasi;
    String bitisNoktasi;

    public KisaYolHesaplayici(ArrayList<Dugum> dugums, String baslangicNoktasi, String bitisNoktasi) {
        this.dugums = dugums;
        this.baslangicNoktasi = baslangicNoktasi;
        this.bitisNoktasi = bitisNoktasi;
    }

    public List<Dugum> hesapla() {
        ArrayList<Vertex> vertices = new ArrayList<>();
        for (Dugum d : dugums) {
            Vertex v = new Vertex(d.getAd());
            vertices.add(v);
        }
        for (Vertex v : vertices) {
            for (Dugum d : dugums) {
                if (v.getName().equals(d.getAd())) {
                    List<KomsuDugum> kds = d.getKomsuDugums();
                    for (KomsuDugum k : kds) {
                        for (Vertex v_ : vertices) {
                            if (v_.getName().equals(k.getKomsuDugumAd())) {
                                v.addNeighbour(new Edge(k.getMesafe(), v, v_));
                            }
                        }
                    }
                }
            }
        }

        Dijkstra dijkstra = new Dijkstra();
        for (Vertex v : vertices) {
            if (v.getName().equals(baslangicNoktasi)) {
                dijkstra.computePath(v);
            }
        }

        List<Vertex> siraliListe = null;

        for (Vertex v : vertices) {
            if (v.getName().equals(bitisNoktasi)) {
                siraliListe = dijkstra.getShortestPathTo(v);
            }
        }
        return vertexToDugum(siraliListe);
    }

    private List<Dugum> vertexToDugum(List<Vertex> siraliListe) {
        ArrayList<Dugum> siraliDugums = new ArrayList<>();
       for (Vertex v : siraliListe) {
           for (Dugum d : dugums) {
               if (v.getName().equals(d.getAd())) {
                   siraliDugums.add(d);
               }
           }
       }
        return siraliDugums;
    }

    private void kerterizHesapla(List<Vertex> siraliListe) {
        for (int i = 0; i < siraliListe.size(); i++) {
            Vertex v = siraliListe.get(i);
            for (Dugum d : dugums) {
                if (v.getName().equals(d.getAd())) {
                    for (KomsuDugum k : d.getKomsuDugums()) {
                        try {
                            if (k.getKomsuDugumAd().equals(siraliListe.get(i+1).getName())) {
                                Log.e("Kerteriz", k.getKerteriz() +  " derece");
                            }
                        } catch (IndexOutOfBoundsException e) {
                            Log.e("Sona", "Ulaştık");
                        }

                    }
                }
            }
        }
    }


}
