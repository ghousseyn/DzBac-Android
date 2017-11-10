package com.squalala.dzbac.ui.filtre;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.SearchEvent;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.squalala.dzbac.DjihtiApp;
import com.squalala.dzbac.R;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.ui.cards.AnnonceAddItemInfoCard;
import com.squalala.dzbac.ui.cards.FiltreItemCard;
import com.squalala.dzbac.ui.cards.FiltrePostCard;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardView;
import it.neokree.materialtabs.MaterialTabHost;

/**
 * Created by Fayçal KADDOURI on 13/10/16.
 */
public class FiltrePostFragment extends Fragment
    implements TextWatcher {


        private static final String TAG = FiltreItemActivity.class.getSimpleName();

        private FiltreItemCard cardFiltre;
        private MainPreferences itemPreferences;

        @InjectView(R.id.edit_mot_cle_annonce) BootstrapEditText editMotCle;

        @InjectView(R.id.btn_default_filter)
        BootstrapButton btnDefaultFilter;

        @InjectView(R.id.btn_search)
        BootstrapButton btnSaveFiltre;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = null;

        root = inflater.inflate(R.layout.fragment_filtre_post, container, false);

        ButterKnife.inject(this, root);

        itemPreferences = new MainPreferences(getContext());
        cardFiltre = new FiltrePostCard(getActivity());
        ((FiltrePostCard) cardFiltre).setFragment(this);
        CardView cardViewFiltreAnnonce = (CardView) root.findViewById(R.id.card_filtre_annonce_id1);

        cardViewFiltreAnnonce.setCard((Card) cardFiltre);
        editMotCle.setText(itemPreferences.getMotCle());
        editMotCle.addTextChangedListener(this);

        btnDefaultFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Answers.getInstance().logSearch(new SearchEvent()
                        .putQuery("Filtre de recherche Post")
                        .putCustomAttribute("Init", "Filtre par défaut"));

                cardFiltre.setDefaultParams();
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        });

        btnSaveFiltre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cardFiltre.saveParams(getMotCle());

                Crouton.makeText(getActivity(), getString(R.string.filtre_saved), Style.INFO).show();

                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        });

        if (!itemPreferences.isTutoGuide(TAG)) {

            new ShowcaseView.Builder(getActivity())
                    .withMaterialShowcase()
                    .setStyle(R.style.CustomShowcaseTheme4)
                    .setContentTitle("C'est magique !")
                    .setContentText("Essaye par exemple de choisir le type 'Résumé' et de trier par Likes & vues et appuie sur \"Sauvegarder le filtre\" pour voir !")
                    .singleShot(2)
                    .build()
                    .show();


            itemPreferences.setTutoGuide(TAG, true);
        }

        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == AnnonceAddItemInfoCard.CODE_TAG) {
            cardFiltre.onSelectedTags(data.getStringArrayListExtra("tags"));
        }
    }

    private String getMotCle() {
        return editMotCle.getText().toString().trim();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before,
                              int count) {
        int nbChar = editMotCle.getText().toString().trim().length();
        if (nbChar >= 3)
            editMotCle.setSuccess();
        else if (nbChar < 3 && nbChar > 0)
            editMotCle.setDanger();
        else
            editMotCle.setDefault();
    }

    @Override
    public void afterTextChanged(Editable s) {}


}
