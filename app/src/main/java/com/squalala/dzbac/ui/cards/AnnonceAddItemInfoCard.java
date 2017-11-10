package com.squalala.dzbac.ui.cards;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.google.gson.Gson;
import com.greenfrvr.hashtagview.HashtagView;
import com.squalala.dzbac.R;
import com.squalala.dzbac.ui.tags_selection.TagSelectionActivity;
import com.squalala.dzbac.utils.StringUtils;
import com.squalala.dzbac.utils.TagsUtils;
import com.squalala.dzbac.utils.Translater;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hugo.weaving.DebugLog;
import it.gmariotti.cardslib.library.internal.Card;


/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : AnnonceAddItemInfoCard.java
 * Date : 6 août 2014
 * 
 */
public class AnnonceAddItemInfoCard extends Card {

	public static final int CODE_TAG = 7000;
	
	
	@InjectView(R.id.edit_titre) BootstrapEditText editTitre;
	@InjectView(R.id.edit_description) BootstrapEditText editDescription;
	@InjectView(R.id.spinner_type) Spinner spinType;
	@InjectView(R.id.spinner_matiere) Spinner spinMatiere;
	@InjectView(R.id.spinner_filiere) Spinner spinFiliere;

	@InjectView(R.id.hashtags5)
	HashtagView hashtagView;

    @InjectView(R.id.txt_reglement)
    TextView txtReglement;

	@InjectView(R.id.tag_item_add)
	FontAwesomeText fontAddTag;

    private Translater translater;

    private List<String> tagsId;
	private List<String> tagsName = new ArrayList<>();

	
	public void setTitre(String titre) {
		editTitre.setText(StringUtils.toEmoji(titre));
	}
	
	public void setDescription(String description) {
		editDescription.setText(StringUtils.toEmoji(description));
	}
	
	
	public void setMatiere(String categorie) {
		ArrayAdapter<CharSequence>adapter = ArrayAdapter.createFromResource( // Pour pouvoir avoir l'index de la catégorie.
			    getContext(),                                   
			    R.array.subject,                 
			    android.R.layout.simple_list_item_1);   
		
		spinMatiere.setSelection(adapter.getPosition(categorie));
	}

	
	public void setTypePost(String categorie) {
		ArrayAdapter<CharSequence>adapter = ArrayAdapter.createFromResource( // Pour pouvoir avoir l'index de la catégorie.
			    getContext(),                                   
			    R.array.type_de_post,
			    android.R.layout.simple_list_item_1);

		spinType.setSelection(adapter.getPosition(categorie));
	}
	
	public AnnonceAddItemInfoCard(Context context, Translater translater) {
		super(context, R.layout.card_info_add_annonce_inner_main);
        this.translater = translater;
	}

	@DebugLog
	public void setTags(List<String> tags) {
		if (tags.size() > 0) {
			this.tagsName = tags;
			hashtagView.setData(tags);
			hashtagView.setVisibility(View.VISIBLE);

            getJsonTagsId();
		}

	}

	@DebugLog
	public void setTagsView(String strTagsId)
	{
		this.tagsId = (ArrayList) new ArrayList<>(Arrays.asList(strTagsId.replaceAll("\"", "").split(",")));

		// On convertit en tagsNames
        tagsName.clear();

		for (String tagId: tagsId) {
			tagsName.add(TagsUtils.getTagName(getContext(), tagId));
		}


		hashtagView.setData(tagsName);
		hashtagView.setVisibility(View.VISIBLE);
	}


	public String getJsonTagsId()
	{
        if (tagsName != null && tagsName.size() > 0)
        {
            List<String> tagList = new ArrayList<>();

            for (String tag: tagsName)
                tagList.add(TagsUtils.getIdTag(getContext(), tag));
            System.out.println(new Gson().toJson(tagList));

            return new Gson().toJson(tagList);
        }
        else
            return null;
    }
	
	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {
		
		ButterKnife.inject(this, parent);

		fontAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TagSelectionActivity.class);
                ((Activity) getContext()).startActivityForResult(intent, CODE_TAG);
            }
        });

        txtReglement.setPaintFlags(txtReglement.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtReglement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://squalala.xyz/reglement.php";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                getContext().startActivity(i);
            }
        });
		
		super.setupInnerViewElements(parent, view);
	}
	
	public String getMatiere() {

        return translater.translateToFrench(
                R.array.subject,
                spinMatiere.getSelectedItemPosition(),
                spinMatiere.getSelectedItem().toString());

	}

	// TODO à vérifier
	public String getTypePost() {

		return spinType.getSelectedItem().toString();

     /*   return translater.translateToFrench(
                R.array.type,
                spinType.getSelectedItemPosition(),
                spinType.getSelectedItem().toString());*/

	}

	public String getFiliere() {

        if (tagsName != null && tagsName.size() > 0)
        {
			System.out.println(tagsName);
            int tagId = Integer.valueOf(TagsUtils.getIdTag(getContext(), tagsName.get(0)));

            switch (tagId) {

                case 1: return "sc";
                case 2: return "mat";
                case 3: return "matech";
                case 4: return "let";
                case 5: return "ges";

            }
        }

		return "sc";
	}



	public String getTitre() {
		return editTitre.getText().toString().trim();
	}
	
	public String getDescription() {
		return editDescription.getText().toString().trim();
	}
	
	/**
	 * @return the editTitre
	 */
	public BootstrapEditText getEditTitre() {
		return editTitre;
	}

	/**
	 * @return the editDescription
	 */
	public BootstrapEditText getEditDescription() {
		return editDescription;
	}


}
