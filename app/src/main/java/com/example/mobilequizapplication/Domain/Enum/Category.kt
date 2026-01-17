package com.example.mobilequizapplication.Domain.Enum

import com.google.gson.annotations.SerializedName

enum class Category(val id: Int, val displayName: String) {
    @SerializedName("General Knowledge")
    GeneralKnowledge(9, "General Knowledge"),
    @SerializedName("Entertainment: Books")
    Books(10, "Books"),
    @SerializedName("Entertainment: Film")
    Film(11, "Movies"),
    @SerializedName("Entertainment: Music")
    Music(12, "Music"),
    @SerializedName("Entertainment: Musicals & Theatres")
    MusicalsAndTheatres(13, "Musical And Theatres"),
    @SerializedName("Entertainment: Television")
    Television(14, "Television"),
    @SerializedName("Entertainment: Video Games")
    VideoGames(15, "Video Games"),
    @SerializedName("Entertainment: Board Games")
    BoardGames(16, "Board Games"),
    @SerializedName("Science & Nature")
    ScienceAndNature(17, "Science And Nature"),
    @SerializedName("Science: Computers")
    Computers(18, "Computers"),
    @SerializedName("Science: Mathematics")
    Mathematics(19, "Mathematics"),
    @SerializedName("Mythology")
    Mythology(20, "Mythology"),
    @SerializedName("Sports")
    Sports(21, "Sports"),
    @SerializedName("Geography")
    Geography(22, "Geography"),
    @SerializedName("History")
    History(23, "History"),
    @SerializedName("Politics")
    Politics(24, "Politics"),
    @SerializedName("Art")
    Art(25, "Art"),
    @SerializedName("Celebrities")
    Celebrities(26, "Celebrities"),
    @SerializedName("Animals")
    Animals(27, "Animals"),
    @SerializedName("Vehicles")
    Vehicles(28, "Vehicles"),
    @SerializedName("Entertainment: Comics")
    Comics(29, "Comics"),
    @SerializedName("Entertainment: Gadgets")
    Gadgets(30, "Gadgets"),
    @SerializedName("Entertainment: Japanese Anime & Manga")
    AnimeAndManga(31, "Anime And Manga"),
    @SerializedName("Entertainment: Cartoon & Animations")
    CartoonAndAnimations(32, "Cartoon And Animations")
}
