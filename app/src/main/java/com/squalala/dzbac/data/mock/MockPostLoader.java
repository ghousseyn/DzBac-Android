package com.squalala.dzbac.data.mock;

import com.squalala.dzbac.data.Post;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : MockItemLoader.java
 * Date : 8 juil. 2014
 * 
 */
public class MockPostLoader {
	
	public PostBuilder newItem() {
		return new PostBuilder();
	}
	
	public static class PostBuilder {
		
		private int hearts;
		private int views;
		private int comments;
		private int width;
		private int height;
		
		private String idItem;
        private String idMembre;
		private String urlPresentation;
		private String titre;
		private String matiere;
		private String type;
		private String pseudo;
		private String urlAvatar;

		private int levelContribution;

		private boolean isVideo;
		private boolean isAudio;

		public PostBuilder isVideo(boolean isVideo) {
			this.isVideo = isVideo;
			return this;
		}

		public PostBuilder isAudio(boolean isAudio) {
			this.isAudio = isAudio;
			return this;
		}
		
		public PostBuilder titre(String titre) {
			this.titre = titre;
			return this;
		}
		
		public PostBuilder views(int views) {
			this.views = views;
			return this;
		}
		
		public PostBuilder comments(int comments) {
			this.comments = comments;
			return this;
		}

        public PostBuilder idMembre(String idMembre) {
            this.idMembre = idMembre;
            return this;
        }

		public PostBuilder hearts(int hearts) {
			this.hearts = hearts;
			return this;
		}
		
		public PostBuilder type(String ville) {
			this.type = ville;
			return this;
		}
		
		public PostBuilder width(int width) {
			this.width = width;
			return this;
		}
		
		public PostBuilder height(int height) {
			this.height = height;
			return this;
		}
		
		public PostBuilder idItem(String idItem) {
			this.idItem = idItem;
			return this;
		}
		
		public PostBuilder urlPresentation(String urlPresentation) {
			this.urlPresentation = urlPresentation;
			return this;
		}
		
		public PostBuilder matiere(String categorie) {
			this.matiere = categorie;
			return this;
		}

		public PostBuilder urlAvatar(String urlAvatar)  {
			this.urlAvatar = urlAvatar;
			return this;
		}

		public PostBuilder pseudo(String pseudo) {
			this.pseudo = pseudo;
			return this;
		}

		public PostBuilder levelContribution(int levelContribution) {
			this.levelContribution = levelContribution;
			return this;
		}
		
		
		public Post build() {
			return new Post(idItem,
					urlPresentation,
					titre,
					type, 
					matiere,
					hearts,
					views,
					comments, 
					width,
					height,
                    idMembre,
					isVideo,
					isAudio,
					urlAvatar,
					pseudo,
					levelContribution);
		}
		
	}

	
}
