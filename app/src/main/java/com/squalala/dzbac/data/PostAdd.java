package com.squalala.dzbac.data;

import com.google.gson.annotations.SerializedName;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : AnnonceItem.java
 * Date : 8 août 2014
 * 
 */
public class PostAdd {
	
	@SerializedName("title")
	private String title;
	@SerializedName("content")
	private String description;
	@SerializedName("type")
	private String type;
	@SerializedName("subject")
	private String matiere;
    @SerializedName("filename")
    private String filename;
	@SerializedName("audio")
	private String audio;
	@SerializedName("url_presentation")
	private String urlPresentation;
	@SerializedName("url_video")
	private String urlVideo;
	@SerializedName("secteur")
	private String secteur;
	@SerializedName("tags_id")
	private String tagsId;

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getTagsId() {
		return tagsId;
	}

	public void setTagsId(String tagsId) {
		this.tagsId = tagsId;
	}

	public String getUrlVideo() {
		return urlVideo;
	}

	public void setUrlVideo(String urlVideo) {
		this.urlVideo = urlVideo;
	}

	public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMatiere() {
		return matiere;
	}
	public void setMatiere(String matiere) {
		this.matiere = matiere;
	}

	public String getSecteur() {
		return secteur;
	}

	public void setSecteur(String secteur) {
		this.secteur = secteur;
	}

	public String getUrlPresentation() {
		return urlPresentation;
	}

	public void setUrlPresentation(String urlPresentation) {
		this.urlPresentation = urlPresentation;
	}
	
}
