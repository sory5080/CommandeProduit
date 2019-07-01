package gn.traore.commandeproduit;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import static gn.traore.commandeproduit.MainActivity.fragmentManager;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private EditText nom, prenom, email, phone, address, agent;
    private Button btnInscrire;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //On récupère les items du fragment
        recupItemsFragment(view);

        //On écoute le bouton
        btnInscrire.setOnClickListener(this);
    }

    private void recupItemsFragment(View view) {
        nom = view.findViewById(R.id.editNom);
        prenom = view.findViewById(R.id.editPrenom);
        email = view.findViewById(R.id.editEmail);
        phone = view.findViewById(R.id.editPhone);
        address = view.findViewById(R.id.editAdresse);
        agent = view.findViewById(R.id.editAgent);
        btnInscrire = view.findViewById(R.id.btnInscrire);
    }

    @Override
    public void onClick(View view) {
        //TODO Vérifier que le phone/certains champs ne sont pas vide
        String token = "token_id1";
        //On envoie les données à l'API d'inscription

        //Si on reçoit le token
        if (!token.isEmpty()) {
            //On le concatène avec le phone
            String identifiants = phone.getText() + ";" + token;

            //Et on le stocke dans le fichier "identifiants"
            try {
                OutputStreamWriter writer = new OutputStreamWriter(view.getContext().openFileOutput("identifiants", Context.MODE_PRIVATE));
                writer.write(identifiants);
                writer.close();

                //On crée le tableau des identifiants à passer au fragment Accueil.
                ArrayList<String> phoneToken = new ArrayList<>();
                phoneToken.add(phone.getText().toString());
                phoneToken.add(token);

                //On lance le fragment Accueil
                AccueilFragment accueilFragment = AccueilFragment.getInstance(phoneToken);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, accueilFragment)
                        .commit();

            } catch (FileNotFoundException e) {
                Toast.makeText(view.getContext(), "FileNotFoundException", Toast.LENGTH_LONG);
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //Sinon on affiche un message d'erreur
            Toast.makeText(view.getContext(), "Token vide !", Toast.LENGTH_LONG).show();
        }
    }
}
