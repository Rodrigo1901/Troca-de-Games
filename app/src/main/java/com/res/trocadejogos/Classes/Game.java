package com.res.trocadejogos.Classes;

public class Game {

    private String nome,imagem,proprietarios;

    public Game() {
    }

    public Game(String nome, String imagem, String proprietarios) {
        this.nome = nome;
        this.imagem = imagem;
        this.proprietarios = proprietarios;
    }

    public String getProprietarios() {
        return proprietarios;
    }

    public void setProprietarios(String proprietarios) {
        this.proprietarios = proprietarios;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
}
