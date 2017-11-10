package com.squalala.dzbac.ui.cards;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.SearchEvent;
import com.greenfrvr.hashtagview.HashtagView;
import com.squalala.dzbac.R;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.ui.filtre.FiltrePostFragment;
import com.squalala.dzbac.ui.tags_selection.TagSelectionActivity;
import com.squalala.dzbac.utils.TagsUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : CardCategorie.java
 * Date : 26 sept 2014
 * 
 */
public class FiltrePostCard extends Card
 implements FiltreItemCard {
	
	private MainPreferences itemPreferences;
	
	private Activity activity;

	@InjectView(R.id.spinner_type_search) Spinner spinType;
	@InjectView(R.id.spinner_subject_search) Spinner spinSubject;
	@InjectView(R.id.spinner_format_search) Spinner spinFormat;
	@InjectView(R.id.spinner_order) Spinner spinOrder;
	
	@InjectView(R.id.toggle_filtre_plus_recente) ToggleButton toggle_filtre_plus_recente;
	@InjectView(R.id.toggle_filtre_a_proximite) ToggleButton toggle_filtre_a_proximite;
	@InjectView(R.id.toggle_filtre_selon_endroit) ToggleButton toggle_filtre_selon_endroit;
	@InjectView(R.id.toggle_filtre_plus_aime_actuellement) ToggleButton toggle_filtre_plus_aime_actuellent;
	@InjectView(R.id.toggle_filtre_plus_aime_generale) ToggleButton toggle_filtre_plus_aime_generale;
	@InjectView(R.id.toggle_filtre_plus_vus_actuellement) ToggleButton toggle_filtre_plus_vus_actuellement;
	@InjectView(R.id.toggle_filtre_plus_vus_generale) ToggleButton toggle_filtre_plus_vus_generale;
	@InjectView(R.id.btn_localisation_selon_endroit) BootstrapButton btn_localisation_endroit;

	@InjectView(R.id.hashtags5)
	HashtagView hashtagView;

	@InjectView(R.id.tag_item_add)
	FontAwesomeText fontAddTag;

	public static final int CODE_TAG = 7000;

	private FiltrePostFragment fragment;

	private ArrayList<String> tagsIdList = new ArrayList<>();

	public FiltrePostCard(Activity activity) {
		super(activity, R.layout.card_service_filtre_inner_main);
		this.activity = activity;
	}

	public void setFragment(FiltrePostFragment f) {
		this.fragment = f;
	}
	
	 @Override
	public void setupInnerViewElements(ViewGroup parent, View view) {
		 
		 ButterKnife.inject(this, parent);
		 
		 itemPreferences = new MainPreferences(getContext());
		 
	/*	toggle_filtre_plus_recente.setOnCheckedChangeListener(this);
		toggle_filtre_a_proximite.setOnCheckedChangeListener(this);
	    toggle_filtre_selon_endroit.setOnCheckedChangeListener(this);
		toggle_filtre_plus_aime_actuellent.setOnCheckedChangeListener(this);
		toggle_filtre_plus_aime_generale.setOnCheckedChangeListener(this);
		toggle_filtre_plus_vus_actuellement.setOnCheckedChangeListener(this);
		toggle_filtre_plus_vus_generale.setOnCheckedChangeListener(this);*/
		
		initComposants();

		 fontAddTag.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(getContext(), TagSelectionActivity.class);
                 fragment.startActivityForResult(intent, CODE_TAG);
             }
         });
		 
		super.setupInnerViewElements(parent, view);
	}

	@Override
	public void onSelectedTags(List<String> tags) {
		if (tags != null && tags.size() > 0) {

            if (tagsIdList != null)
                tagsIdList.clear();

            for (String tag: tags)
                tagsIdList.add(TagsUtils.getIdTag(getContext(), tag));

			hashtagView.setData(tags);
			hashtagView.setVisibility(View.VISIBLE);
		}
	}

	public void initComposants() {
		 spinType.setSelection(itemPreferences.getType(), true);
		 spinSubject.setSelection(itemPreferences.getSubject(), true);
		 spinOrder.setSelection(itemPreferences.getTypeOrder(), true);
		 spinFormat.setSelection(itemPreferences.getFormat(), true);

		 tagsIdList = itemPreferences.getTagsId();

        if (tagsIdList != null) {
            ArrayList<String> tagsNameList = new ArrayList<>();

            // On convertit nos tags id à tags name
            for (String tagId: tagsIdList) {
                tagsNameList.add(TagsUtils.getTagName(getContext(), tagId));
            }

            if (tagsIdList != null && tagsIdList.size() > 0) {
                hashtagView.setData(tagsNameList);
                hashtagView.setVisibility(View.VISIBLE);
            }
        }


	 }
	 
	 
	 /*@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			switch (buttonView.getId()) {
			case R.id.toggle_filtre_a_proximite:
				// Car on ne peut pas mettre "À proximité de moi" et "selon un endroit" en même temps
				if (isChecked)
					toggle_filtre_selon_endroit.setChecked(false); 
				
				break;
				
			case R.id.toggle_filtre_selon_endroit:
				
				if (isChecked)
				{
					btn_localisation_endroit.setVisibility(View.VISIBLE);
					toggle_filtre_a_proximite.setChecked(false);
				}
				else
					btn_localisation_endroit.setVisibility(View.GONE);
				
				break;

			case R.id.toggle_filtre_plus_recente:
				
				break;
				
			case R.id.toggle_filtre_plus_aime_generale:
				
				if(isChecked) {
					toggle_filtre_plus_vus_actuellement.setChecked(false);
					toggle_filtre_plus_aime_actuellent.setChecked(false);
				}
				
				break;
				
			case R.id.toggle_filtre_plus_aime_actuellement:
				
				if(isChecked) {
					toggle_filtre_plus_aime_generale.setChecked(false);
					toggle_filtre_plus_vus_generale.setChecked(false);
				}
				
				
				break;
				
			case R.id.toggle_filtre_plus_vus_generale:
				
				if(isChecked) {
					toggle_filtre_plus_vus_actuellement.setChecked(false);
					toggle_filtre_plus_aime_actuellent.setChecked(false);
				}
				
				
				break;
				
			case R.id.toggle_filtre_plus_vus_actuellement:
				
				if(isChecked) {
					toggle_filtre_plus_vus_generale.setChecked(false);
					toggle_filtre_plus_aime_generale.setChecked(false);
				}
				
				
				break;
			default:
				break;
			}
		}*/
	 
	 /**
	  *  Pour sauvegarder les pramètres 
	  */
	 public void saveParams(String mot_cle) {

		itemPreferences.setFormat(spinFormat.getSelectedItemPosition());
		itemPreferences.setType(spinType.getSelectedItemPosition());
		itemPreferences.setSubject(spinSubject.getSelectedItemPosition());
		itemPreferences.setTypeOrder(spinOrder.getSelectedItemPosition());
		itemPreferences.setMotCle(mot_cle);
		itemPreferences.setTagsId(new HashSet(tagsIdList));

         System.out.println("Tags id saved : " + tagsIdList.toString());

		 Answers.getInstance().logSearch(new SearchEvent()
				 .putQuery("Filtre de recherche")
				 .putCustomAttribute("Mot clé", mot_cle)
				 .putCustomAttribute("Type", spinType.getSelectedItem().toString())
				 .putCustomAttribute("Matière", spinSubject.getSelectedItem().toString())
				 .putCustomAttribute("Trier par", spinOrder.getSelectedItem().toString()));
	 }
	 
	 
	 /**
	  *  Remettre à zéro
	  */
	 public void setDefaultParams() {
		 hashtagView.setVisibility(View.GONE);
		 itemPreferences.setDefaultPreferences();
	 }
	    

}
