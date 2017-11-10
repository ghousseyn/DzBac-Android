package com.squalala.dzbac.data;


/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : Post.java
 * Date : 5 janv. 2015
 * 
 */
public class Post {
	
	public int hearts;
	public int views;
	public int comments; // nombre de commentaires
	public int width; // Largeur de l'image
	public int height;
	
	public final String id_item;
    public final String idMembre;
	public final String url_presentation;
	public final String titre;
	public final String type; // Aide, résumé, sujets
	public final String matiere;
	public final String urlAvatar;
	public final String pseudo;

	public final int levelContribution;

	public final boolean isVideo;
	public final boolean isAudio;
	
	public Post(String id, String url_presentation, String titre, String type, String matiere, 
			int hearts, int views, int comments, int width, int height, String idMembre, boolean isVideo, boolean isAudio,
				String urlAvatar, String pseudo, int levelContribution) {
		this.id_item = id;
		this.url_presentation = url_presentation;
		this.titre = titre;
		this.matiere = matiere;
		this.type = type;
		this.hearts = hearts;
		this.views = views;
		this.comments = comments;
		this.width = width;
		this.height = height;
        this.idMembre = idMembre;
		this.isVideo = isVideo;
		this.isAudio = isAudio;
		this.pseudo = pseudo;
		this.urlAvatar = urlAvatar;
		this.levelContribution = levelContribution;
	}
	
	
}
