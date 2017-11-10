package com.squalala.dzbac.data.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : api_message.java
 * Date : 23 juin 2014
 * 
 */
public class ApiResponse {
	
	public static class BasicResponse {
		@SerializedName("state_bann")
		public String stateBann;
		@SerializedName("message")
		public String message;
		@SerializedName("success")
		public String success;
		@SerializedName("error")
		public String error;
		@SerializedName("id_membre")
		public String id_membre;
	}

	public static class StateUser {
		@SerializedName("min_app_version")
		public int minVersionApp;
		@SerializedName("nombre_notifications")
		public String nbreNotification;
		@SerializedName("nombre_message_non_lu")
		public String nbreMessageNonLu;
		@SerializedName("state_bann")
		public String stateBann;
		@SerializedName("prenium")
		public int codePrenium;
		@SerializedName("level_contribution")
		public String levelContribution;
	}

	public static class RankUser {
		@SerializedName("id_membre")
		public String id_membre;
		@SerializedName("level")
		public int level;
		@SerializedName("username")
		public String username;
		@SerializedName("url_avatar")
		public String urlAvatar;
		@SerializedName("likes")
		public String likes;
        @SerializedName("level_contribution")
        public int levelContribution;
	}
	
	public static class Login {
		@SerializedName("id_membre")
		public String id_membre;
		@SerializedName("url_avatar")
		public String url_avatar;
        @SerializedName("url_background")
        public String url_background;
		@SerializedName("username")
		public String pseudo;
		@SerializedName("message")
		public String message;
		@SerializedName("error")
		public String error;
		@SerializedName("level")
		public int level;
	}
	
	public static class Items {

        @SerializedName("level_contribution")
        public int levelContribution;
		
		@SerializedName("views")
		public int vues;
		@SerializedName("likes")
		public int like;
		@SerializedName("comments")
		public int nombre_commentaire;
		
		@SerializedName("width_url_image_presentation")
		public int width_url_image_presentation;
		@SerializedName("height_url_image_presentation")
		public int height_url_image_presentation;

		@SerializedName("is_video")
		public int isVideo;

		@SerializedName("is_audio")
		public int isAudio;

		@SerializedName("url_avatar")
		public String url_avatar;
		@SerializedName("username")
		public String pseudo;
		
		@SerializedName("type")
		public String type;
		@SerializedName("subject")
		public String matiere;
		@SerializedName("url_presentation")
		public String url_presentation;
		@SerializedName("title")
		public String title;
		@SerializedName("id")
		public String id;
        @SerializedName("id_membre")
        public String id_membre;
	}
	
	public static class Commentaire {

		public Commentaire() {}

		@SerializedName("id")
		public String id; // id du commentaire
		@SerializedName("id_membre")
		public String id_membre;
		@SerializedName("url_avatar")
		public String url_avatar;
        @SerializedName("url")
        public List<?> url_images;
		@SerializedName("username")
		public String pseudo;
		@SerializedName("level")
		public int level;
		@SerializedName("audio")
		public String urlAudio;
		@SerializedName("message")
		public String commentaire;
		@SerializedName("date_creation")
		public String date_creation;
		@SerializedName("level_contribution")
		public int levelContribution;
	}
	
	public static class Message extends Commentaire {
		@SerializedName("date_last_view")
		public String date_last_view;
		@SerializedName("message_lu")
		public String message_lu;
	}
	
	public static class Commentaires {
		@SerializedName("comments")
		public List<Commentaire> posts;
		@SerializedName("state_group")
		public boolean state_group;
	}
	
	public static class Messages {
		@SerializedName("conversation")
		public List<Message> conversation;
		@SerializedName("nombre_message_non_lu")
		public int nombre_message_non_lu;
	}
	
	public static class PostsItems {
		@SerializedName("posts")
		public List<Items> posts;
	}
	
	public static class ContentItem {
		@SerializedName("contents")
		public List<Item> posts;
	}
	
	public static class Item {

        @SerializedName("level_contribution")
        public int levelContribution;
		// Info sur le posteur
		@SerializedName("username")
		public String pseudo;
		@SerializedName("id_membre")
		public String id_membre;
		@SerializedName("url_avatar")
		public String url_avatar;

        @SerializedName("filename")
        public String filename;

		@SerializedName("tags")
		public String tags;

		// Info sur l'item

		@SerializedName("url_video")
		public String urlVideo;

		@SerializedName("audio")
		public String urlAudio;
		
		@SerializedName("title")
		public String titre;
		@SerializedName("content")
		public String description;

		@SerializedName("secteur")
		public String filiere;
		
		@SerializedName("type")
		public String type;
		@SerializedName("subject")
		public String matiere;
		
		@SerializedName("date_creation")
		public String date_creation;
		@SerializedName("url_presentation")
		public String url_image_presentation;
		
		// Nom de l'image de présentation
		@SerializedName("image_name")
		public String image_name;
		
		// Social 
		@SerializedName("like")
		public String like;
		@SerializedName("valeur_aime")
		public int valeur_aime;
		@SerializedName("valeur_favoris")
		public int valeur_favoris;

		@SerializedName("comments")
		public int nombre_commentaire;
		
		@SerializedName("type_service")
		public int type_service;
		
		@SerializedName("url")
		public List<?> url_images;
	}
	
	public static class User {

		public User() {}


        @SerializedName("level_contribution")
        public int levelContribution;

		@SerializedName("id_membre")
		public String idMembre;
		@SerializedName("username")
		public String userName;
		@SerializedName("localisation")
		public String localisation;
		@SerializedName("about")
		public String apropos;
		@SerializedName("level")
		public int level;
		@SerializedName("date_creation")
		public String dateCreation;
		@SerializedName("date_last_activity")
		public String dateLastActivity;
		@SerializedName("abonnes")
		public String abonnes;
		@SerializedName("is_following")
		public String isFollowing;
        @SerializedName("comments")
        public String commentaires;
        @SerializedName("posts")
        public String posts;
		@SerializedName("url_avatar_large")
		public String url_avatar_large;
		@SerializedName("url_background")
		public String url_image_fond;
		@SerializedName("url_avatar")
		public String url_avatar_thumbnail;
		@SerializedName("likes")
		public String likes;
	}
	
	public static class FilesResponse {
		@SerializedName("files")
		public List<Upload> files;
	 }
	
	public static class Upload {
		@SerializedName("name")
		public String name;
		@SerializedName("error")
		public String error;
		@SerializedName("size")
		public String size;
		@SerializedName("type")
		public String type;
		@SerializedName("thumbnailUrl")
		public String thumbnailUrl;
		@SerializedName("url")
		public String url;
	  }
	
	public static class ListConversation {
		@SerializedName("id")
		public String id; // représente id_header
		@SerializedName("id_membre")
		public String id_membre;
		@SerializedName("id_membre_2")
		public String id_membre_2;
		@SerializedName("pseudo")
		public String pseudo;
		@SerializedName("url_avatar")
		public String url_avatar;
		@SerializedName("id_message")
		public String id_message;
		@SerializedName("level")
		public int level;
		@SerializedName("date_creation")
		public String date_creation;
		@SerializedName("sujet")
		public String sujet;
		
		@SerializedName("message_lu")
		public int message_lu;

		@SerializedName("level_contribution")
		public int levelContribution;
	}
	
	public static class ListConversationPosts {
		@SerializedName("posts")
		public List<ListConversation> posts; 
	}

	public static class Rankings {
		@SerializedName("rankings")
		public List<RankUser> rankUsers;
	}

	public static class Users {
		@SerializedName("users")
		public List<User> rankUsers;
	}
	
	public static class NotificationsPosts {
		@SerializedName("posts")
		public List<NotificationResponse> posts;
	}

	public static class NotificationResponse {
		@SerializedName("notification")
		public String notification;
		@SerializedName("date_creation")
		public String date_creation;
		@SerializedName("id_content")
		public String id_item;
		@SerializedName("type")
		public String type;
		@SerializedName("state")
		public String state;
		@SerializedName("url_avatar")
		public String url_avatar;
		@SerializedName("verb")
		public String verb;
		@SerializedName("id_membre")
		public String id_membre;
		@SerializedName("nombre_notifications")
		public String nombre_notifications;
		@SerializedName("state_group")
		public boolean state_group;
		@SerializedName("level_contribution")
		public int levelContribution;
	}
	
	public static class UserPosts {
		@SerializedName("posts")
		public List<User> posts;
	}

    public static class FileUploadResponse extends BasicResponse {
        @SerializedName("filename")
        public String filename;
    }
}
