package com.example.denuncia.adapter;

import com.example.denuncia.model.Denuncia;

import java.util.ArrayList;
import java.util.List;

public class DenunciaService {

    public static List<Denuncia> denuncias=new ArrayList<>();

    public static void addDenuncia(Denuncia denuncia){
        denuncias.add(denuncia);
    }
    public static void updateDenuncia(Denuncia denuncia){
        denuncias.set(denuncias.indexOf(denuncia),denuncia);
    }
}
