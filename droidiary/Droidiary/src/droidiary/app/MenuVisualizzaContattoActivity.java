package droidiary.app;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.NumberKeyListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import droidiary.db.Contatto;
import droidiary.db.DroidiaryDatabaseHelper;

public class MenuVisualizzaContattoActivity extends Activity {
	/** Called when the activity is first created. */

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menuvisualizzacontatto);

		contatto = getIntent().getExtras().getString("droidiary.app.MenuRubricaActivity");

		System.out.println("Parametro Contatto: "+contatto);

		dbd = new DroidiaryDatabaseHelper(this); //collegamento database
		db=dbd.getWritableDatabase();
		try {
			dbd.openDataBase();
		}catch(SQLException sqle){

			throw sqle;

		}

		//id_account, nome, cognome, citta, cellulare, numeroCasa, email

		Cursor result=Contatto.getDatiFromString(db, contatto);

		if(result.moveToFirst()){
			codUtente=result.getString(1);
			id=result.getInt(0);
			System.out.println("Codice id: " + id);
			codUtente=result.getString(1);
			System.out.println("Codice Account: " + codUtente);
			TextView utente=(TextView) findViewById(R.id.Utente);
			utente.setText("Contatto: "+result.getString(2) + " " + result.getString(3));
			
			TextView nome= (TextView)findViewById(R.id.nomecontatto);
			nome.setText(result.getString(2));
			nome.setKeyListener(new NumberKeyListener() {
			    public int getInputType() {
			        return InputType.TYPE_NULL;
			    }

			    protected char[] getAcceptedChars() {
			        return new char[] {};
			    }
			});
			TextView cognome= (TextView)findViewById(R.id.cognomecontatto);
			cognome.setText(result.getString(3));
			cellulare= (TextView)findViewById(R.id.telefonocellularecontatto);
			cellulare.setText(result.getString(5));
			casa= (TextView)findViewById(R.id.telefonocasacontatto);
			casa.setText(result.getString(6));
			TextView citta= (TextView)findViewById(R.id.cittacontatto);
			citta.setText(result.getString(4));
			TextView email= (TextView)findViewById(R.id.emailcontatto);
			email.setText(result.getString(7));
		}
		
		ImageView img = (ImageView) findViewById(R.id.chiamatacasa);
		img.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	Intent dialIntent=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+(casa.getText()).toString()));
		    	startActivity(dialIntent);
		    }
		});
		
		ImageView img1 = (ImageView) findViewById(R.id.chiamatacellulare);
		img1.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	Intent dialIntent=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+(cellulare.getText()).toString()));
		    	startActivity(dialIntent);
		    }
		});
		
		Button modificaContatto=(Button) findViewById(R.id.modificacontatto);
		modificaContatto.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				Intent intent = new Intent(MenuVisualizzaContattoActivity.this, ModificaContattoActivity.class);
				intent.putExtra("droidiary.app.contatto", contatto);
				intent.putExtra("droidiary.app.codUtente", id);
				dbd.close();
				startActivity(intent);
			}
		});
		
		Button eliminaContatto=(Button) findViewById(R.id.eliminacontatto);
		eliminaContatto.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				int result=Contatto.eliminaContatto(db, id, Integer.parseInt(codUtente));
				if(result == 0)
					Toast.makeText(getApplicationContext(),  "Problema nell'eliminazione del contatto", Toast.LENGTH_LONG).show();
				else
				{
					Toast.makeText(getApplicationContext(),  "Contatto eliminato con successo", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(MenuVisualizzaContattoActivity.this, MenuRubricaActivity.class);
					intent.putExtra("droidiary.app.contatto", contatto);
					intent.putExtra("droidiary.app.codUtente", id);
					dbd.close();
					startActivity(intent);
				}
			}
		});
	}

	private DroidiaryDatabaseHelper dbd;
	private SQLiteDatabase db;
	private String codUtente;
	private int id;
	private String contatto;
	private TextView casa, cellulare;
}